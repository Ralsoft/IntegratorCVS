package ru.artsec.MqttExample.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfiguration {

    private final static Logger log = LoggerFactory.getLogger(MqttConfiguration.class);
    @Value("${mqtt.clientId}")
    String clientId;
    @Value("${mqtt.hostname}")
    String hostname;
    @Value("${mqtt.port}")
    int port;
    @Value("${mqtt.connectionTimeout}")
    int connectionTimeout;
    @Value("${mqtt.automaticReconnect}")
    boolean automaticReconnect;
    @Value("${mqtt.cleanSession}")
    boolean cleanSession;
    IMqttClient mqttClient;
    MqttConnectOptions connectOptions = new MqttConnectOptions();

    @Bean
    public IMqttClient mqttClient() {
        try {
            log.info("Создание подключения клиента: ID = " + clientId + ", HOST_NAME = " + hostname + ", PORT = " + port);

            connectOptions.setCleanSession(cleanSession);
            connectOptions.setConnectionTimeout(connectionTimeout);
            connectOptions.setAutomaticReconnect(automaticReconnect);

            mqttClient = new MqttClient("tcp://" + hostname + ":" + port, clientId);
            mqttClient.connect();

            log.info("Успешное подключение клиента \"" + clientId + "\" по адресу: " + mqttClient.getServerURI());
        } catch (Exception e) {
            log.error("Ошибка: " + e);
        }
        return mqttClient;
    }
}
