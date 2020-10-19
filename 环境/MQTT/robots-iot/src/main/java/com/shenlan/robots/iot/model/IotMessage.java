package com.shenlan.robots.iot.model;

import lombok.Data;
import org.springframework.messaging.MessageHeaders;

@Data
public class IotMessage {
    private String topic;

    private String body;

    private MessageHeaders headers;

    private boolean isSystemTopic;
}
