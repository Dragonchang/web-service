package com.shenlan.robots.iot.component;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.stereotype.Component;


@Component
public class ServerContext implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    public static MqttGateWay getGateWay() {
        return (MqttGateWay)applicationContext.getBean(MqttGateWay.class);
    }

    public static MqttPahoClientFactory getMqttFactory() {
        return (MqttPahoClientFactory)applicationContext.getBean(MqttPahoClientFactory.class);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (ServerContext.applicationContext == null)
            ServerContext.applicationContext = applicationContext;
    }
}
