package ru.artsec.MqttExample.models;

import lombok.Data;

@Data
public class MQTTClientModel {
    String mqttClientId = "Integrator";
    String mqttClientIp = "194.87.237.67";
    int mqttClientPort = 1883;

    public MQTTClientModel() {
    }

    @Override
    public String toString() {
        return "{" + "\n" +
                "\"mqttClientId\": \"" + mqttClientId + "\",\n" +
                "\"mqttClientIp\": \"" + mqttClientIp + "\",\n" +
                "\"mqttClientPort\": " + mqttClientPort + "\n" +
                '}';
    }

    public MQTTClientModel(String mqttClientId, String mqttClientIp, int mqttClientPort) {
        this.mqttClientId = mqttClientId;
        this.mqttClientIp = mqttClientIp;
        this.mqttClientPort = mqttClientPort;
    }
}
