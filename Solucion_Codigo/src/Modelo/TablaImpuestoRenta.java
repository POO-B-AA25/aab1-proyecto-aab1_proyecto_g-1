package Modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class TablaImpuestoRenta implements Serializable {
    private static final long serialVersionUID = 1L;

    private int anio;
    private ArrayList<RangoImpuesto> rangos;

    public TablaImpuestoRenta(int anio) {
        this.anio = anio;
        this.rangos = new ArrayList<>();
        inicializarTabla2023();
    }

    private void inicializarTabla2023() {
        // Tabla IR 2023 Ecuador
        // Fracción Básica, Fracción Excedente, Impuesto Fracción Básica, % Impuesto Fracción Excedente
        rangos.add(new RangoImpuesto(0.00, 11310.00, 0.00, 0.00));
        rangos.add(new RangoImpuesto(11310.01, 14410.00, 0.00, 0.05));
        rangos.add(new RangoImpuesto(14410.01, 18010.00, 155.00, 0.10));
        rangos.add(new RangoImpuesto(18010.01, 21630.00, 515.00, 0.12));
        rangos.add(new RangoImpuesto(21630.01, 31630.00, 949.40, 0.15));
        rangos.add(new RangoImpuesto(31630.01, 41630.00, 2449.40, 0.20));
        rangos.add(new RangoImpuesto(41630.01, 51630.00, 4449.40, 0.25));
        rangos.add(new RangoImpuesto(51630.01, 61630.00, 6949.40, 0.30));
        rangos.add(new RangoImpuesto(61630.01, 100000.00, 9949.40, 0.35));
        rangos.add(new RangoImpuesto(100000.01, Double.MAX_VALUE, 23378.90, 0.37));
    }

    public double calcularImpuesto(double baseImponible) {
        RangoImpuesto rango = buscarRango(baseImponible);

        if (rango == null) {
            return 0.0;
        }

        double excedente = baseImponible - rango.getFraccionBasica();
        double impuestoExcedente = excedente * rango.getPorcentajeFraccionExcedente();

        return rango.getImpuestoFraccionBasica() + impuestoExcedente;
    }

    public RangoImpuesto buscarRango(double baseImponible) {
        for (RangoImpuesto rango : rangos) {
            // Si es el último rango o está dentro del rango actual
            if (baseImponible >= rango.getFraccionBasica() &&
                    (baseImponible < rango.getFraccionExcedente() || rango.getFraccionExcedente() == Double.MAX_VALUE)) {
                return rango;
            }
        }
        return null; // No debería ocurrir si la tabla está completa
    }

    public int getAnio() {
        return anio;
    }

    public ArrayList<RangoImpuesto> getRangos() {
        return rangos;
    }

    @Override
    public String toString() {
        return "TablaImpuestoRenta{" +
                "anio=" + anio +
                ", rangos=" + rangos.size() +
                '}';
    }
}