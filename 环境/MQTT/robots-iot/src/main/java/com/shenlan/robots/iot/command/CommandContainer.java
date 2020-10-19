package com.shenlan.robots.iot.command;

import com.shenlan.robots.iot.topic.Topic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class CommandContainer {
    private static String DEVICE_CONNECTION_TOPIC_TAG = "connected";
    private static String DEVICE_DISCONNECTION_TOPIC_TAG = "disconnected";

    private HashMap<String, List<Class>> commandVendor = new HashMap<String, List<Class>>() {
        {
            put(DEVICE_CONNECTION_TOPIC_TAG, createEntity(ConnectCommand.class, ConnectCommandProcess.class));
            put(DEVICE_DISCONNECTION_TOPIC_TAG, createEntity(DisconnectCommand.class, DisconnectCommandProcess.class));
            put(Topic.ROBOTS_SERVICE_CLEAN_PRE_POINT.getMeasurementName(), createEntity(TestCommand.class, TestCommandProcess.class));
        }
    };

    private static List<Class> createEntity(Class command, Class process) {
        return new ArrayList<Class>(Arrays.asList(command, process));
    }

    public List<Class> getCommandByTopic(String topic) {
        if(topic == null || !commandVendor.containsKey(topic)) {
            log.warn("getCommandByTopic failed with topic: "+topic);
            return null;
        }
       return commandVendor.get(topic);
    }
}
