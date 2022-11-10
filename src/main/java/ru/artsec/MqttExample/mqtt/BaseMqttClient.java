package ru.artsec.MqttExample.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.artsec.MqttExample.models.IntegratorCVSModel;
import ru.artsec.MqttExample.models.MQTTClientModel;
import ru.artsec.MqttExample.service.MqttService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class BaseMqttClient implements MqttService {

    private final static Logger log = LoggerFactory.getLogger(BaseMqttClient.class);
    MqttClient mqttClient;
    ObjectMapper mapper;
    File mqttConfig;
    MQTTClientModel mqttClientModel;
    MqttMessage mqttMessage;

    @Override
    public void publish(String topic, String payload, String camNumber, boolean flag) throws InterruptedException {
        try {
            mapper = new ObjectMapper();
            mqttClientModel = mapper.readValue(mqttConfig, MQTTClientModel.class);

            log.info("Создание подключения клиента: HOST_NAME = " + mqttClientModel.getMqttClientIp() + ", PORT = " + mqttClientModel.getMqttClientPort());

            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setConnectionTimeout(5000);
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttConnectOptions.setCleanSession(true);
            mqttConnectOptions.setUserName(mqttClientModel.getMqttUsername());
            mqttConnectOptions.setPassword(mqttClientModel.getMqttPassword().toCharArray());

            mqttClient = new MqttClient("tcp://" + mqttClientModel.getMqttClientIp() + ":" + mqttClientModel.getMqttClientPort(), MqttClient.generateClientId());
            mqttClient.connect(mqttConnectOptions);
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

    void isNewFile(File file) {
        try {
            if (file.createNewFile()) {
                FileOutputStream out = new FileOutputStream(file);
                out.write(new MQTTClientModel().toString().getBytes());
                out.close();
                log.info("Файл конфигурации успешно создан. Запустите программу заново.  ПУТЬ: " + file.getAbsolutePath());
                System.exit(1);
            }
        } catch (IOException e) {
            log.error("Ошибка: " + e.getMessage());
        }
    }

    @Bean
    void createFileConfig() {
        mqttConfig = new File("IntegratorConfig.json");
        isNewFile(mqttConfig);
    }
}
