package ru.artsec.MqttExample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.artsec.MqttExample.service.MqttService;

@Controller
public class MqttController {

    final MqttService mqttService;
    Logger log = LoggerFactory.getLogger(MqttController.class);

    public MqttController(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    @GetMapping("/send")
    public String sendMessage(String topic, String payload) {
        try {
            log.info("Получен топик и ГРЗ. ТОПИК: \"" + topic + "\" ГРЗ: \"" + payload + "\"");
            log.info("Выполнение валидации.");

            mqttService.publish(topic, payload);
        } catch (Exception e) {
            log.error("Ошибка: " + e);
        }
        return "hello";
    }

    @GetMapping("/greeting")
    public String greeting(Model model){
        model.addAttribute("greeting", "Hello World");
        return "hello";
    }
}
