package com.conorward.littlepaydemo.models;

public enum Stop {
    STOP1(0, 3.25, 7.30),
    STOP2(3.25, 0, 5.50),
    STOP3(7.30, 5.50, 0);

    private final double chargeToStop1;
    private final double chargeToStop2;
    private final double chargeToStop3;

    Stop(double chargeToStop1, double chargeToStop2, double chargeToStop3) {
        this.chargeToStop1 = chargeToStop1;
        this.chargeToStop2 = chargeToStop2;
        this.chargeToStop3 = chargeToStop3;
    }

    public double calculateIncompleteTripCharge() {
        return Math.max(Math.max(chargeToStop1, chargeToStop2), chargeToStop3);
    }

    public double calculateChargeToStop(Stop toStop) {
        return switch (toStop) {
            case STOP1 -> chargeToStop1;
            case STOP2 -> chargeToStop2;
            case STOP3 -> chargeToStop3;
        };
    }
}
