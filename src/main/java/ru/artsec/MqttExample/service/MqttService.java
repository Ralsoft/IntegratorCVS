package ru.artsec.MqttExample.service;

import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Validated
public interface MqttService {
    void publish(

            @NotNull
            String topic,

            @NotNull
            @Length(min = 1, max = 10)
            @Pattern(regexp = "^[ABCDEFHKMOPTXY\\d]+$", message = "ГРЗ содержит недопустимые символы.")
            String payload,

            @NotNull
            @Length(max = 10)
            @Pattern(regexp = "^[A-Za-z\\d]+$", message = "Название канала ГРЗ содержит недопустимые символы.")
            String camNumber,

            boolean flag

    ) throws InterruptedException;
}
