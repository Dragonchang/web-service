package com.shenlan.robots.iot.listener;


import com.shenlan.robots.iot.model.IotMessage;

public interface Processor {
    void process(IotMessage message);
    void executeMessage(IotMessage message);
    void processEnd(IotMessage message);

}
