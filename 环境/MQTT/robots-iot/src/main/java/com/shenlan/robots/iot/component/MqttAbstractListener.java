package com.shenlan.robots.iot.component;

import com.shenlan.robots.iot.model.IotMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;

public abstract class MqttAbstractListener implements InitializingBean {
    @Autowired
    protected ApplicationContext applicationContext;

    @Value("${spring.profiles.active:native}")
    private String profile;

    @Value("${spring.application.name}")
    private String service;

    public abstract String[] subscribe();

    public abstract void receiveMessage(IotMessage paramIotMessage);

    public void afterPropertiesSet() throws Exception {
        String[] topics = subscribe();
        if (topics == null || topics.length == 0)
            return;
        for (int i = 0; i < topics.length; i++)
            topics[i] = "$queue/" + topics[i];
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext)this.applicationContext;
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)configurableApplicationContext.getBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MqttPahoMessageDrivenChannelAdapter.class);
        beanDefinitionBuilder.addConstructorArgValue(this.service + "-" + this.profile + "_in_" + System.currentTimeMillis());
        beanDefinitionBuilder.addConstructorArgValue(this.applicationContext.getBean("mqttClientFactory"));
        beanDefinitionBuilder.addConstructorArgValue(topics);
        beanDefinitionBuilder.addPropertyValue("completionTimeout", Integer.valueOf(10000));
        beanDefinitionBuilder.addPropertyValue("converter", new DefaultPahoMessageConverter());
        beanDefinitionBuilder.addPropertyValue("outputChannel", this.applicationContext.getBean("mqttInBoundChannel"));
        defaultListableBeanFactory.registerBeanDefinition("mqttInbound", (BeanDefinition)beanDefinitionBuilder.getRawBeanDefinition());
    }
}
