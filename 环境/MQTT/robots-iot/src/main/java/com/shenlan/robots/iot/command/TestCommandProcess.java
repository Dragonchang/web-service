package com.shenlan.robots.iot.command;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestCommandProcess implements CommandProcess{

    public void process(Command message) {
        TestCommand testCommand = (TestCommand) message;
        log.warn("测试消息为："+testCommand.getMsg());
    }
}
