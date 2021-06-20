package com.shenlan.robots.iot.topic;

public class SubscribeTopics {
    public static String[] getTopics() {
        return new String[] {
                Topic.ROBOTS_SYSTEM_MESSAGE.getMeasurementName(),
                Topic.ROBOTS_SERVICE_CLEAN_PRE_POINT.getMeasurementName()
        };
    }
}
