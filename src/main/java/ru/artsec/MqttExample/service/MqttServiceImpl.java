package ru.artsec.MqttExample.service;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MqttServiceImpl implements MqttService {

    private final static Logger log = LoggerFactory.getLogger(MqttServiceImpl.class);
    final IMqttClient iMqttClient;
    MqttMessage mqttMessage;

    public MqttServiceImpl(IMqttClient iMqttClient) {
        this.iMqttClient = iMqttClient;
    }

    @Override
    public void publish(String topic, String payload) {
        try {
            log.info("Валидация прошла успешно.");
            log.info("Совершение GET запроса: /send. ТОПИК: \"" + topic + "\"." + " ГРЗ: \"" + payload + "\"");

            mqttMessage = new MqttMessage();
            mqttMessage.setPayload(payload.getBytes());
            iMqttClient.publish(topic, mqttMessage);

            log.info("ГРЗ \"" + payload + "\" успешно отправлено на топик \"" + topic + "\"");
            // mqttExampleApplication.mqttClient.publish(topic, payload.getBytes(), qos, retained);
            //mqttClient.disconnect();
        } catch (Exception e) {
            log.error("Ошибка: " + e);
        }
    }
}
