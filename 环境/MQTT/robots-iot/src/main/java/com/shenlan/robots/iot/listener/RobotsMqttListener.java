package com.shenlan.robots.iot.listener;

import com.shenlan.robots.iot.component.MqttAbstractListener;
import com.shenlan.robots.iot.model.IotMessage;
import com.shenlan.robots.iot.topic.SubscribeTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class RobotsMqttListener extends MqttAbstractListener {
    private static String SYSTEM_TOPIC_TAG = "$SYS/brokers";
    private static String SYSTEM_CLIENT_TAG = "clients";
    private static String DEVICE_CONNECTION_TOPIC_TAG = "connected";
    private static String DEVICE_DISCONNECTION_TOPIC_TAG = "disconnected";
    private static int clientsIndex = 4;

    @Autowired
    RobotsMqttProcessor robotsMqttProcessor;

    @Override
    public String[] subscribe() {
        return SubscribeTopics.getTopics();
    }

    @Override
    public void receiveMessage(IotMessage message) {
        if(message == null) {
            log.error("empty message!");
            return;
        }
        log.debug("接收消息主题 : " + message.getTopic());
        log.debug("接收消息内容 : " + message.getBody());
        if(filterValidMessage(message)) {
            log.info("iot receive message :" + message.getBody());
            robotsMqttProcessor.process(message);
        }
    }

    private boolean filterValidMessage(IotMessage message) {
        String topic = message.getTopic();
        if(StringUtils.isEmpty(topic)) {
            log.warn("filterValidMessage this is a empty topic");
            return false;
        }
        if(topic.startsWith(SYSTEM_TOPIC_TAG)) {
            String [] item = topic.split("/");
            if(item == null || item.length < clientsIndex
            || !item[clientsIndex -1].equals(SYSTEM_CLIENT_TAG)) {
                log.warn("filterValidMessage this is not off/on line topic");
                return false;
            }
            if(!topic.endsWith(DEVICE_CONNECTION_TOPIC_TAG)
            && !topic.endsWith(DEVICE_DISCONNECTION_TOPIC_TAG) ) {
                return false;
            }
            message.setSystemTopic(true);
        }
        log.info("iot receive message from topic:" + topic);
        return true;
    }
}
