package ru.artsec.MqttExample.models;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class CvsModel {

    String messageId;
    CvsModelPlate plate;
}
