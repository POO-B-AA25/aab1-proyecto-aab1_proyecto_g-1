package Modelo; // Este archivo está en el paquete Modelo

import java.io.Serializable; // Permite guardar/cargar objetos de esta clase

// Enum que representa las categorías de gastos deducibles
public enum CategoriaGasto implements Serializable {
    VIVIENDA,
    EDUCACION,
    ALIMENTACION,
    VESTIMENTA,
    SALUD,
    TURISMO;

    // Devuelve el límite máximo deducible para la categoría según los ingresos anuales
    public double getLimiteMaximoDeducible(double ingresoAnual) {
        return switch (this) {
            case VIVIENDA -> Math.min(ingresoAnual * 0.20, 3800); // 20% del ingreso o $3800, lo menor
            case EDUCACION -> Math.min(ingresoAnual * 0.20, 3800);
            case ALIMENTACION -> Math.min(ingresoAnual * 0.20, 3800);
            case VESTIMENTA -> Math.min(ingresoAnual * 0.20, 3800);
            case SALUD -> Math.min(ingresoAnual * 0.20, 3800);
            case TURISMO -> Math.min(ingresoAnual * 0.10, 1900);
            default -> 0.0;
        };
    }
}