package ru.artsec.MqttExample.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import ru.artsec.MqttExample.models.IntegratorCVSModel;
import ru.artsec.MqttExample.models.MQTTClientModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;

@Service
public class MqttServiceImpl implements MqttService {

    private final static Logger log = LoggerFactory.getLogger(MqttServiceImpl.class);
    ObjectMapper mapper = new ObjectMapper();
    File mqttConfig = new File("IntegratorConfig.json");
    MqttMessage mqttMessage;
    MqttClient mqttClient;

    @Override
    public void publish(String topic, String payload, String camNumber, boolean flag) throws InterruptedException {
        MQTTClientModel mqttClientModel = null;
        try {
            mqttClientModel = mapper.readValue(mqttConfig, MQTTClientModel.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            log.info(
                    "Создание подключения клиента... HOST_NAME = " + mqttClientModel.getMqttClientIp() +
                    ", PORT = " + mqttClientModel.getMqttClientPort() +
                    ", USERNAME = " + mqttClientModel.getMqttUsername() +
                    ", PASSWORD = " + mqttClientModel.getMqttPassword()
                    );

            if(mqttClient == null) {
                MqttConnectOptions options = new MqttConnectOptions();
                options.setAutomaticReconnect(true);
                options.setConnectionTimeout(5000);
                options.setUserName(mqttClientModel.getMqttUsername());
                options.setPassword(mqttClientModel.getMqttPassword().toCharArray());

                mqttClient = new MqttClient(
                        "tcp://" + mqttClientModel.getMqttClientIp() + ":" +
                                mqttClientModel.getMqttClientPort(),
                        InetAddress.getLocalHost() + "-Integration"
                );
                log.info(
                        "Выставленные настройки MQTT: " +
                                "Автоматический реконнект = " + options.isAutomaticReconnect() + ", " +
                                "Максимальное время подключения = " + options.getConnectionTimeout()
                );
                mqttClient.connect(options);

                log.info("Успешное подключение клиента по адресу: " + mqttClient.getServerURI());
            }


            mqttClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean reconnect, String serverURI) {
                    log.info("Соединение присутствует!");
                }

                @Override
                public void connectionLost(Throwable cause) {
                    log.warn("Соединение потеряно!");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });

            if (flag) {
                log.info("Начинается публикация. TOPIC: " + topic + ", PAYLOAD: " + payload + ", CAM_NUMBER: " + camNumber);
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(new IntegratorCVSModel(payload, camNumber));

                mqttMessage = new MqttMessage();
                mqttMessage.setPayload(json.getBytes());
                mqttClient.publish(topic, mqttMessage);
                log.info("Публикация прошла успешно. Опубликовано = " + json);
            }
        } catch (Exception ex) {
            log.error("Ошибка: " + ex);
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
            log.error("Ошибка: " + e);
        }
    }

    @Bean
    private void createFileConfig() {
        mqttConfig = new File("IntegratorConfig.json");
        isNewFile(mqttConfig);
    }
}
