package com.shenlan.robots.iot.controller;

import com.shenlan.robots.iot.component.MqttSender;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * MQTT消息发送
 *
 * @author zhangfl
 * @date 2020-03-10 13:21
 **/
@RestController
@RequestMapping(value = "/")
public class MqttController {

    @Autowired
    private MqttSender mqttSender;

    /**
     * 发送MQTT消息到设备123456789
     *
     * @param message 消息内容
     * @return 返回
     */
    @GetMapping(value = "/mqtt", produces = "text/html")
    public ResponseEntity<String> sendMqtt(@RequestParam(value = "msg") String message) throws MqttException {
        mqttSender.send("robot-command/123456789", message, 2);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
