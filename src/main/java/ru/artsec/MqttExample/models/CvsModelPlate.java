package ru.artsec.MqttExample.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CvsModelPlate {

    String camera;
    String channel;
    String dateTime;
    String description;
    String direction;
    String groupId;
    String id;
    String image;
    String inList;
    String passed;
    String plate;
    String quality;
    String stayTimeMinutes;
    String type;
    String weight;
}
