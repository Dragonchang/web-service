package com.shenlan.robots.iot.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MqttSender {
    public boolean send(String topic, String message) {
        return send(topic, message, null);
    }

    public boolean send(String topic, String message, Integer qos) {
        try {
            if (null != qos) {
                ServerContext.getGateWay().sendToMqtt(topic, message, qos.intValue());
            } else {
                ServerContext.getGateWay().sendToMqtt(topic, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("ms MqttSender error topic:{},message:{},qos:{},error:{}", new Object[] { topic, message, qos, e.getMessage() });
            return false;
        }
        return true;
    }

    public boolean send(String topic, String message, Integer qos, boolean retained) {
        try {
            ServerContext.getGateWay().sendToMqtt(topic, message, qos.intValue(), retained);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("ms MqttSender error topic:{},message:{},qos:{},retained,error:{}", new Object[] { topic, message, qos, Boolean.valueOf(retained), e.getMessage() });
            return false;
        }
        return true;
    }
}
