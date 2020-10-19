package com.shenlan.robots.iot.command;

import com.shenlan.robots.iot.model.IotMessage;
import lombok.Data;

@Data
public class TestCommand  implements Command{
    private String msg;

    @Override
    public void createCommand(IotMessage message) {
        if(message == null) {
            return;
        }
        msg = message.getBody();
    }
}
