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
        switch (this) {
            case VIVIENDA:
                return Math.min(ingresoAnual * 0.20, 3800); // 20% del ingreso o $3800, lo menor
            case EDUCACION:
                return Math.min(ingresoAnual * 0.20, 3800);
            case ALIMENTACION:
                return Math.min(ingresoAnual * 0.20, 3800);
            case VESTIMENTA:
                return Math.min(ingresoAnual * 0.20, 3800);
            case SALUD:
                return Math.min(ingresoAnual * 0.20, 3800);
            case TURISMO:
                return Math.min(ingresoAnual * 0.10, 1900); // 10% del ingreso o $1900, lo menor
            default:
                return 0.0;
        }
    }
}