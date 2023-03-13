package ru.artsec.MqttExample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IntegratorCVSApplication{

    private static final Logger log = LoggerFactory.getLogger(IntegratorCVSApplication.class);
    public static void main(String[] args) {
        try {
            log.info("Запуск программы.");
            SpringApplication.run(IntegratorCVSApplication.class, args);
        } catch (Exception ex) {
            log.error("Ошибка: " + ex);
        }
    }
}
