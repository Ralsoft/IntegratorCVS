package ru.artsec.MqttExample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.artsec.MqttExample.service.MqttServiceImpl;

@Controller
public class MqttController {
    Logger log = LoggerFactory.getLogger(MqttController.class);

    final MqttServiceImpl mqttService;

    public MqttController(MqttServiceImpl mqttService) {
        this.mqttService = mqttService;
    }

    @GetMapping("/send")
    public String sendMessage(String topic, String payload, String camNumber, Model model) {
        try {
            boolean flag = true;
            log.info("Получен GET запрос. TOPIC: " + topic + "PAYLOAD: " + payload + " CAM_NUMBER: " + camNumber);
            model.addAttribute("topic", topic);
            model.addAttribute("payload", payload);
            model.addAttribute("camNumber", camNumber);
            mqttService.publishMessage(topic, payload, camNumber, flag);
        } catch (Exception e) {
            log.error("Ошибка: " + e);
        }
        return "index";
    }
}
