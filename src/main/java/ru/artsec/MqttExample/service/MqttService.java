package ru.artsec.MqttExample.service;

import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

@Validated
public interface MqttService {
    void publish(
            @Length(min = 2, max = 10, message = "Длина названия топика должна быть от 2 до 10 символов!")
            String topic,
            String payload);
}
