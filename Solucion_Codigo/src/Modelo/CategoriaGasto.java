package Modelo;

import java.io.Serializable;

public enum CategoriaGasto implements Serializable {
    VIVIENDA,
    EDUCACION,
    ALIMENTACION,
    VESTIMENTA,
    SALUD,
    TURISMO;

    public double getLimiteMaximoDeducible(double ingresoAnual) {
        switch (this) {
            case VIVIENDA:
                return Math.min(ingresoAnual * 0.20, 3800);
            case EDUCACION:
                return Math.min(ingresoAnual * 0.20, 3800);
            case ALIMENTACION:
                return Math.min(ingresoAnual * 0.20, 3800);
            case VESTIMENTA:
                return Math.min(ingresoAnual * 0.20, 3800);
            case SALUD:
                return Math.min(ingresoAnual * 0.20, 3800);
            case TURISMO:
                return Math.min(ingresoAnual * 0.10, 1900);
            default:
                return 0.0;
        }
    }
}