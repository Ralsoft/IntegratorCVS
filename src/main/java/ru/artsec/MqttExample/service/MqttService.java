package ru.artsec.MqttExample.service;

public interface MqttService {
    void publish(String topic, String payload, int camNumber);
}
