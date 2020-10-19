package com.shenlan.robots.iot.command;

import com.alibaba.fastjson.JSONObject;
import com.shenlan.robots.iot.model.IotMessage;
import lombok.Data;

@Data
public class DisconnectCommand implements Command{
    private String clientid;
    @Override
    public void createCommand(IotMessage message) {
        if(message == null) {
            return;
        }
        String msg = message.getBody();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        clientid = jsonObject.get("clientid").toString();
    }
}
