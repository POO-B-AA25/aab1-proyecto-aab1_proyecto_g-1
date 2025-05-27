package Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Declaracion implements Serializable {
    private static final long serialVersionUID = 1L;

    private int anio;
    private String estado;
    private ArrayList<Sueldo> sueldosMensuales;
    private ArrayList<Factura> facturas;
    private double ingresoAnual;
    private double gastosDeducibles;
    private double baseImponible;
    private double impuestoCausado;
    private double impuestoRetenido;
    private double saldoAPagar;
    private TablaImpuestoRenta tablaImpuesto;

    public Declaracion() {
        this.anio = java.time.Year.now().getValue();
        this.estado = "Pendiente";
        this.sueldosMensuales = new ArrayList<>();
        this.facturas = new ArrayList<>();
        this.ingresoAnual = 0.0;
        this.gastosDeducibles = 0.0;
        this.baseImponible = 0.0;
        this.impuestoCausado = 0.0;
        this.impuestoRetenido = 0.0;
        this.saldoAPagar = 0.0;
        this.tablaImpuesto = new TablaImpuestoRenta(anio);
    }

    public Declaracion(int anio) {
        this.anio = anio;
        this.estado = "Pendiente";
        this.sueldosMensuales = new ArrayList<>();
        this.facturas = new ArrayList<>();
        this.ingresoAnual = 0.0;
        this.gastosDeducibles = 0.0;
        this.baseImponible = 0.0;
        this.impuestoCausado = 0.0;
        this.impuestoRetenido = 0.0;
        this.saldoAPagar = 0.0;
        this.tablaImpuesto = new TablaImpuestoRenta(anio);
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void agregarSueldo(Sueldo sueldo) {
        // Verificar si ya existe un sueldo para ese mes
        for (int i = 0; i < sueldosMensuales.size(); i++) {
            if (sueldosMensuales.get(i).getMes() == sueldo.getMes()) {
                // Reemplazar el sueldo existente
                sueldosMensuales.set(i, sueldo);
                return;
            }
        }
        // Agregar nuevo sueldo
        sueldosMensuales.add(sueldo);
    }

    public void agregarFactura(Factura factura) {
        facturas.add(factura);
    }

    public boolean validarLimitesDeducibles() {
        // Calcular ingresos anuales primero
        double ingresoAnual = calcularIngresoAnual();

        // Obtener los gastos por categoría
        Map<CategoriaGasto, Double> gastosPorCategoria = getTotalGastosPorCategoria();
        Map<CategoriaGasto, Double> limites = getLimitesDeduciblesPorCategoria();

        // Verificar si alguna categoría excede su límite
        for (CategoriaGasto categoria : gastosPorCategoria.keySet()) {
            double gastoCategoria = gastosPorCategoria.get(categoria);
            double limiteCategoria = limites.get(categoria);

            if (gastoCategoria > limiteCategoria) {
                return false;
            }
        }

        return true;
    }

    public double calcularIngresoAnual() {
        ingresoAnual = 0.0;
        impuestoRetenido = 0.0;

        for (Sueldo sueldo : sueldosMensuales) {
            ingresoAnual += sueldo.getMonto();
            impuestoRetenido += sueldo.getImpuestoRetenido();
        }

        return ingresoAnual;
    }

    public double calcularGastosDeducibles() {
        gastosDeducibles = 0.0;
        Map<CategoriaGasto, Double> gastosPorCategoria = getTotalGastosPorCategoria();
        Map<CategoriaGasto, Double> limites = getLimitesDeduciblesPorCategoria();

        // Calcular el total de gastos deducibles respetando los límites por categoría
        for (CategoriaGasto categoria : gastosPorCategoria.keySet()) {
            double gastoCategoria = gastosPorCategoria.get(categoria);
            double limiteCategoria = limites.get(categoria);

            // Solo considerar hasta el límite
            gastosDeducibles += Math.min(gastoCategoria, limiteCategoria);
        }

        return gastosDeducibles;
    }

    public double calcularBaseImponible() {
        // Calcular ingresos y gastos
        calcularIngresoAnual();
        calcularGastosDeducibles();

        // Calcular total de aportes IESS
        double totalAportesIESS = 0.0;
        for (Sueldo sueldo : sueldosMensuales) {
            totalAportesIESS += sueldo.getAporteIESS();
        }

        // Base imponible = Ingresos - Gastos deducibles - Aportes IESS
        baseImponible = ingresoAnual - gastosDeducibles - totalAportesIESS;
        if (baseImponible < 0) {
            baseImponible = 0;
        }

        return baseImponible;
    }

    public double calcularImpuestoCausado() {
        // Calcular base imponible primero
        calcularBaseImponible();

        // Calcular impuesto causado usando la tabla de impuesto a la renta
        impuestoCausado = tablaImpuesto.calcularImpuesto(baseImponible);

        return impuestoCausado;
    }

    public double calcularSaldoAPagar() {
        // Calcular impuesto causado primero
        calcularImpuestoCausado();

        // Saldo a pagar = Impuesto causado - Impuesto retenido
        saldoAPagar = impuestoCausado - impuestoRetenido;
        if (saldoAPagar < 0) {
            // Si es negativo, hay saldo a favor del contribuyente
            estado = "Saldo a favor";
        } else if (saldoAPagar > 0) {
            estado = "Por pagar";
        } else {
            estado = "Declaración en cero";
        }

        return saldoAPagar;
    }

    public String[] getDatosResumen() {
        // Asegurar que todos los cálculos estén hechos
        calcularSaldoAPagar();

        String[] resumen = new String[10];
        resumen[0] = "Año fiscal: " + anio;
        resumen[1] = "Estado: " + estado;
        resumen[2] = "Ingresos anuales: $" + String.format("%.2f", ingresoAnual);
        resumen[3] = "Aportes IESS: $" + String.format("%.2f", calcularTotalAportesIESS());
        resumen[4] = "Gastos deducibles: $" + String.format("%.2f", gastosDeducibles);
        resumen[5] = "Base imponible: $" + String.format("%.2f", baseImponible);
        resumen[6] = "Impuesto causado: $" + String.format("%.2f", impuestoCausado);
        resumen[7] = "Impuesto retenido: $" + String.format("%.2f", impuestoRetenido);
        resumen[8] = "Saldo a pagar: $" + String.format("%.2f", saldoAPagar);
        resumen[9] = "Total facturas registradas: " + (facturas.size()+1);

        return resumen;
    }

    private double calcularTotalAportesIESS() {
        double total = 0.0;
        for (Sueldo sueldo : sueldosMensuales) {
            total += sueldo.getAporteIESS();
        }
        return total;
    }

    public ArrayList<Sueldo> getSueldosMensuales() {
        return sueldosMensuales;
    }

    public ArrayList<Factura> getFacturas() {
        return facturas;
    }

    public Map<CategoriaGasto, Double> getTotalGastosPorCategoria() {
        Map<CategoriaGasto, Double> gastosPorCategoria = new HashMap<>();

        // Inicializar todas las categorías en 0
        for (CategoriaGasto categoria : CategoriaGasto.values()) {
            gastosPorCategoria.put(categoria, 0.0);
        }

        // Sumar los gastos por categoría
        for (Factura factura : facturas) {
            if (factura.isDeducible() && factura.getCategoria() != null) {
                CategoriaGasto categoria = factura.getCategoria();
                double valorActual = gastosPorCategoria.get(categoria);
                gastosPorCategoria.put(categoria, valorActual + factura.getMonto());
            }
        }

        return gastosPorCategoria;
    }

    public Map<CategoriaGasto, Double> getLimitesDeduciblesPorCategoria() {
        Map<CategoriaGasto, Double> limites = new HashMap<>();

        // Calcular los límites para cada categoría basado en los ingresos anuales
        double ingresos = calcularIngresoAnual();

        for (CategoriaGasto categoria : CategoriaGasto.values()) {
            limites.put(categoria, categoria.getLimiteMaximoDeducible(ingresos));
        }

        return limites;
    }

    @Override
    public String toString() {
        return "Declaracion{" +
                "anio=" + anio +
                ", estado='" + estado + '\'' +
                ", sueldosMensuales=" + sueldosMensuales.size() +
                ", facturas=" + facturas.size() +
                ", ingresoAnual=" + ingresoAnual +
                ", gastosDeducibles=" + gastosDeducibles +
                ", baseImponible=" + baseImponible +
                ", impuestoCausado=" + impuestoCausado +
                ", impuestoRetenido=" + impuestoRetenido +
                ", saldoAPagar=" + saldoAPagar +
                '}';
    }
}