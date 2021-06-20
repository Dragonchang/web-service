package com.shenlan.robots.iot.topic;

public enum Topic {
  ROBOTS_SYSTEM_MESSAGE("$SYS/brokers/+/clients/#"), //系统topic，用于设备的上下线监控
  ROBOTS_SERVICE_CLEAN_PRE_POINT("robot-service/Service_Test_Subscribe");//测试订阅

  private String measurementName;

  Topic(String name) {
    this.measurementName = name;
  }

  public String getMeasurementName() {
    return measurementName;
  }

  public void setMeasurementName(String measurementName) {
    this.measurementName = measurementName;
  }
}
