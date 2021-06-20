package com.shenlan.robots.iot.command;

import com.shenlan.robots.iot.model.IotMessage;

public interface Command {
    void createCommand(IotMessage message);
}
