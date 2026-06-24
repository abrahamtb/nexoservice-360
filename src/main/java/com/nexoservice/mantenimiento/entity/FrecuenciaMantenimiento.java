package com.nexoservice.mantenimiento.entity;

public enum FrecuenciaMantenimiento {
    MENSUAL(1),
    TRIMESTRAL(3),
    SEMESTRAL(6),
    ANUAL(12);

    private final int meses;

    FrecuenciaMantenimiento(int meses) {
        this.meses = meses;
    }

    public int getMeses() {
        return meses;
    }
}
