package ru.artsec.MqttExample.models;

import lombok.Data;

@Data
public class IntegratorCVSModel {
    String GRZ;
    int camNumber;

    public IntegratorCVSModel(String GRZ, int camNumber) {
        this.GRZ = GRZ;
        this.camNumber = camNumber;
    }

    public IntegratorCVSModel() {
    }

}
