package ru.artsec.MqttExample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.artsec.MqttExample.service.MqttServiceImpl;

@RestController
public class MqttController {
    Logger log = LoggerFactory.getLogger(MqttController.class);

    final MqttServiceImpl mqttService;

    public MqttController(MqttServiceImpl mqttService) {
        this.mqttService = mqttService;
    }

    @GetMapping("/send")
    public void sendMessage(String topic, String payload, int camNumber) {
        try {
            log.info("Получен топик и ГРЗ. TOPIC: " + topic + "PAYLOAD: " + payload + " CAM_NUMBER: " + camNumber);

            mqttService.publishMessage(topic, payload, camNumber);
        } catch (Exception e) {
            log.error("Ошибка: " + e);
        }
    }
}
