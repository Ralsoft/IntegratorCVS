package ru.artsec.MqttExample.models;

import lombok.Data;

@Data
public class IntegratorCVSModel {
    String GRZ;
    String camNumber;

    String fileName;

    public IntegratorCVSModel(String GRZ, String camNumber, String fileName) {
        this.GRZ = GRZ;
        this.camNumber = camNumber;
        this.fileName = fileName;
    }

    public IntegratorCVSModel() {
    }

}
