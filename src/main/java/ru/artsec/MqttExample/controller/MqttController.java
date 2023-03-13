package ru.artsec.MqttExample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.artsec.MqttExample.models.CvsModel;
import ru.artsec.MqttExample.service.MqttService;

@Controller
public class MqttController {
    Logger log = LoggerFactory.getLogger(MqttController.class);

    final MqttService mqttService;

    public MqttController(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    @ResponseBody
    @PostMapping("/send")
    public String sendMessage(Model model, HttpEntity<String> httpEntity) {
        try {
            String topic = "Parking/IntegratorCVS";
            String json = httpEntity.getBody();
            ObjectMapper mapper = new ObjectMapper();
            CvsModel models = mapper.readValue(json, CvsModel.class); // Модель пришедшего JSON
            log.info("Получен JSON = " + mapper.writeValueAsString(models));

            boolean flag = true;
            log.info("Получен POST запрос. TOPIC: " + topic + "PAYLOAD: " + models.getPlate().getPlate() + " CAM_NUMBER: " + models.getPlate().getCamera());
            model.addAttribute("topic", topic);
            model.addAttribute("payload", models.getPlate().getPlate());
            model.addAttribute("camNumber", models.getPlate().getCamera());
            mqttService.publish(topic, models.getPlate().getPlate(),models.getPlate().getCamera(), flag);
            return json;
        } catch (Exception e) {
            log.error("Ошибка: " + e);
        }
        return "Fail :(";
    }
}
