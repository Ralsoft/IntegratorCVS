package ru.artsec.MqttExample.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import ru.artsec.MqttExample.models.IntegratorCVSModel;
import ru.artsec.MqttExample.models.MQTTClientModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class MqttServiceImpl implements MqttService {

    private final static Logger log = LoggerFactory.getLogger(MqttServiceImpl.class);
    MqttClient mqttClient;
    ObjectMapper mapper = new ObjectMapper();
    File mqttConfig = new File("IntegratorConfig.json");
    MqttMessage mqttMessage;

    @Override
    public void publish(String topic, String payload, String camNumber, boolean flag) throws InterruptedException {
        try {
            MQTTClientModel mqttClientModel = mapper.readValue(mqttConfig, MQTTClientModel.class);

            log.info("Создание подключения клиента: HOST_NAME = " + mqttClientModel.getMqttClientIp() + ", PORT = " + mqttClientModel.getMqttClientPort());
            mqttClient = new MqttClient("tcp://" + mqttClientModel.getMqttClientIp() + ":" + mqttClientModel.getMqttClientPort(), MqttClient.generateClientId());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setConnectionTimeout(5000);
            options.setUserName(mqttClientModel.getMqttUsername());
            options.setPassword(mqttClientModel.getMqttPassword().toCharArray());

            mqttClient.connect(options);

            log.info("Успешное подключение клиента по адресу: " + mqttClient.getServerURI());

            if (flag) {
                log.info("Попытка публикации TOPIC: " + topic + "PAYLOAD: " + payload + " CAM_NUMBER: " + camNumber);
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(new IntegratorCVSModel(payload, camNumber));

                mqttMessage = new MqttMessage();
                mqttMessage.setQos(0);
                mqttMessage.setPayload(json.getBytes());
                mqttClient.publish(topic, mqttMessage);
                log.info("ГРЗ \"" + payload + "\" успешно отправлено на топик \"" + topic + "\" Номер камеры: " + camNumber + "\"");
            }
            mqttClient.disconnect();
        } catch (Exception ex) {
            Thread.sleep(5000);
            log.error("Ошибка: " + ex);
            if (!mqttClient.isConnected()) publish(topic, payload, camNumber, false);
        }
    }

    private void isNewFile(File file) {
        try {
            if (file.createNewFile()) {
                FileOutputStream out = new FileOutputStream(file);

                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String json = ow.writeValueAsString(new MQTTClientModel());

                out.write(json.getBytes());
                out.close();
                log.info("Файл конфигурации успешно создан. Запустите программу заново.  ПУТЬ: " + file.getAbsolutePath());
                System.exit(1);
            }
        } catch (IOException e) {
            log.error("Ошибка: " + e.getMessage());
        }
    }

    @Bean
    private void createFileConfig() {
        mqttConfig = new File("IntegratorConfig.json");
        isNewFile(mqttConfig);
    }
}
