package com.shenlan.robots.iot.component;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateWay {
    void sendToMqtt(@Header("mqtt_topic") String paramString1, String paramString2);

    void sendToMqtt(@Header("mqtt_topic") String paramString1, String paramString2, @Header("mqtt_qos") int paramInt);

    void sendToMqtt(@Header("mqtt_topic") String paramString1, String paramString2, @Header("mqtt_qos") int paramInt, @Header("mqtt_retained") boolean paramBoolean);
}
