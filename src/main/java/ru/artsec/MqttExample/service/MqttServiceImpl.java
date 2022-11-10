package ru.artsec.MqttExample.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MqttServiceImpl {

    private final static Logger log = LoggerFactory.getLogger(MqttServiceImpl.class);
    final MqttService mqttService;

    public MqttServiceImpl(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    public void publishMessage(String topic, String payload, String camNumber, boolean flag) throws InterruptedException {
        mqttService.publish(topic, payload, camNumber,flag);
    }
}
