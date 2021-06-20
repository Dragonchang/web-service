package com.shenlan.robots.iot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("mqtt")
public class MqttProperties {

    private String username;

    private String password;

    private String url;
}
