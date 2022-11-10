package ru.artsec.MqttExample.models;

import lombok.Data;

@Data
public class IntegratorCVSModel {
    String GRZ;
    String camNumber;

    public IntegratorCVSModel(String GRZ, String camNumber) {
        this.GRZ = GRZ;
        this.camNumber = camNumber;
    }

    public IntegratorCVSModel() {
    }

}
