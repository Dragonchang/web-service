package com.shenlan.robots.iot.config;

import com.shenlan.robots.iot.component.MqttAbstractListener;
import com.shenlan.robots.iot.model.IotMessage;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.converter.MessageConverter;

@Configuration
@IntegrationComponentScan
@Slf4j
@EnableConfigurationProperties(value = MqttProperties.class)
public class MqttConfiguration {
    /**
     * 订阅的bean名称
     */
    public static final String CHANNEL_NAME_IN = "mqttInboundChannel";
    /**
     * 发布的bean名称
     */
    public static final String CHANNEL_NAME_OUT = "mqttOutboundChannel";

    @Value("${spring.profiles.active:native}")
    private String profile;

    @Value("${spring.application.name}")
    private String service;

    @Autowired
    private MqttProperties mqttProperties;

    @Autowired
    protected ApplicationContext applicationContext;

    /**
     * MQTT连接器选项
     *
     * @return {@link MqttConnectOptions}
     */
    @Bean
    public MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setMaxInflight(600);
        options.setUserName(mqttProperties.getUsername());
        options.setPassword(mqttProperties.getPassword().toCharArray());
        options.setServerURIs(new String[]{mqttProperties.getUrl()});
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(20);
        return options;
    }

    /**
     * MQTT客户端
     *
     * @return {@link MqttPahoClientFactory}
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        DirectChannel dc = new DirectChannel();
        dc.subscribe(mqttOutboundHandler());
        return (MessageChannel)dc;
    }

    @Bean
    public MessageChannel mqttInBoundChannel() {
        DirectChannel dc = new DirectChannel();
        dc.subscribe(mqttInboundHandler());
        return (MessageChannel)dc;
    }

    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_IN)
    public MessageHandler mqttInboundHandler() {
        return new MessageHandler() {
            public void handleMessage(Message<?> message) throws MessagingException {
                IotMessage m = new IotMessage();
                m.setTopic(message.getHeaders().get("mqtt_receivedTopic").toString());
                m.setHeaders(message.getHeaders());
                Object payload = message.getPayload();
                m.setBody(payload.toString());
                MqttAbstractListener listener = (MqttAbstractListener)MqttConfiguration.this.applicationContext.getBean(MqttAbstractListener.class);
                try {
                    listener.receiveMessage(m);
                } catch (Exception e) {
                    log.error("ms mqttInboundHandler error class:{},method:{}, topic:{},message:{},error:{}", new Object[] { listener.getClass().getName(), "receiveMessage", m.getTopic(), m.getBody(), e.getMessage() });
                    e.printStackTrace();
                }
            }
        };
    }

    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_OUT)
    public MessageHandler mqttOutboundHandler() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(this.service + "-" + this.profile +  "_out_" + System.currentTimeMillis(), mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setConverter((MessageConverter)new DefaultPahoMessageConverter());
        return (MessageHandler)messageHandler;
    }
}

