package ru.artsec.MqttExample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.artsec.MqttExample.models.CvsModel;
import ru.artsec.MqttExample.models.MQTTClientModel;
import ru.artsec.MqttExample.service.MqttService;
import ru.artsec.MqttExample.service.MqttServiceImpl;

import java.io.*;
import java.time.LocalDateTime;

@Controller
public class MqttController {
    Logger log = LoggerFactory.getLogger(MqttController.class);


    final MqttService mqttService;

    public MqttController(MqttService mqttService) {
        this.mqttService = mqttService;

        new File("images").mkdir();
    }

    public void test(){
        File file = new File("C:\\Users\\dana\\IdeaProjects\\IntegratorCVS\\src\\main\\resources\\answer.json");

        BufferedReader reader = null;
        try {
            reader = new BufferedReader
                    (new InputStreamReader(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            String json = reader.readLine();
            ObjectMapper mapper = new ObjectMapper();
            CvsModel model = mapper.readValue(json, CvsModel.class); // Модель пришедшего JSON

            var path = "";
            boolean flag = true;
            if(MqttServiceImpl.getConfigParam().getDoSaveFile()){
                path =  saveImage(
                        model.getPlate().getImage(),
                        model.getPlate().getPlate(),
                        model.getPlate().getCamera());
            }

            mqttService.publish(
                    MqttServiceImpl.getConfigParam().getPublishTopic(),
                    model.getPlate().getPlate(),
                    model.getPlate().getCamera(),
                    path, flag);


        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String saveImage(String image, String GRZ, String CameraNumber) throws IOException {
        byte[] decodedBytes = Base64.decodeBase64(image);

        LocalDateTime myObj = LocalDateTime.now();

        var m = String.valueOf(myObj.getMonthValue());
        if(m.length() == 1) m = "0" + m;

        var fileName =
                myObj.getYear() + "-" +
                m + "-" +
                myObj.getDayOfMonth() + "_" +
                myObj.getHour() + "-" +
                myObj.getMinute() + "-" +
                myObj.getSecond() + " " +
                "Cam " + CameraNumber + " " + GRZ;
        try (OutputStream stream = new FileOutputStream("images\\" + fileName + ".jpeg")) {
            stream.write(decodedBytes);
        }
        return fileName;
    }

    @ResponseBody
    @GetMapping("/test")
    public void test(Model model) {
        test();
    }


    @ResponseBody
    @PostMapping("/send")
    public String sendMessage(Model model, HttpEntity<String> httpEntity) {
        try {
            String json = httpEntity.getBody();
            ObjectMapper mapper = new ObjectMapper();
            CvsModel cvsModel = mapper.readValue(json, CvsModel.class); // Модель пришедшего JSON
            log.info("Получен JSON = " + mapper.writeValueAsString(cvsModel));

            var path = "";
            var topic = MqttServiceImpl.getConfigParam().getPublishTopic();
            if(MqttServiceImpl.getConfigParam().getDoSaveFile()){
                path = saveImage(
                        cvsModel.getPlate().getImage(),
                        cvsModel.getPlate().getPlate(),
                        cvsModel.getPlate().getCamera());
            }

            boolean flag = true;
            log.info("Получен POST запрос. TOPIC: " + topic + "PAYLOAD: " + cvsModel.getPlate().getPlate() + " CAM_NUMBER: " + cvsModel.getPlate().getCamera());
            model.addAttribute("topic", topic);
            model.addAttribute("payload", cvsModel.getPlate().getPlate());
            model.addAttribute("camNumber", cvsModel.getPlate().getCamera());

            mqttService.publish(
                    topic,
                    cvsModel.getPlate().getPlate(),
                    cvsModel.getPlate().getCamera(),
                    path, flag);

            return json;
        } catch (Exception e) {
            log.error("Ошибка: " + e);
        }
        return "Fail :(";
    }
}
