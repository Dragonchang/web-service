package com.shenlan.robots.iot.command;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DisconnectCommandProcess implements CommandProcess{

    public void process(Command message) {
        DisconnectCommand disconnectCommand = (DisconnectCommand) message;
        log.warn(" 设备："+disconnectCommand.getClientid()+" 下线了");
    }
}
