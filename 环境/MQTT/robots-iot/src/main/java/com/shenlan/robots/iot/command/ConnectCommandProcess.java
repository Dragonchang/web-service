package com.shenlan.robots.iot.command;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectCommandProcess implements CommandProcess{

    public void process(Command message) {
        ConnectCommand connectCommand = (ConnectCommand) message;
        log.warn(" 设备："+connectCommand.getClientid()+" 上线了");
    }
}
