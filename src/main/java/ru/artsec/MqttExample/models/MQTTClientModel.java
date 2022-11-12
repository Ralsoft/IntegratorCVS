package ru.artsec.MqttExample.models;

import lombok.Data;

@Data
public class MQTTClientModel {
    String mqttClientId = "Integrator";
    String mqttClientIp = "194.87.237.67";
    int mqttClientPort = 1883;
    String mqttUsername = "admin";
    String mqttPassword = "333";

    public MQTTClientModel() {
    }

    public MQTTClientModel(String mqttClientId, String mqttClientIp, int mqttClientPort, String mqttUsername, String mqttPassword) {
        this.mqttClientId = mqttClientId;
        this.mqttClientIp = mqttClientIp;
        this.mqttClientPort = mqttClientPort;
        this.mqttUsername = mqttUsername;
        this.mqttPassword = mqttPassword;
    }
}
