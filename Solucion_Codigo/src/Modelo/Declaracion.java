package Modelo; // Este archivo está en el paquete Modelo

import java.io.Serializable; // Permite guardar/cargar objetos de esta clase
import java.util.ArrayList;  // Lista dinámica para sueldos y facturas
import java.util.HashMap;    // Tabla para gastos por categoría
import java.util.Map;        // Tabla para límites y gastos

// Clase que representa toda la declaración anual de impuestos de una persona
public class Declaracion implements Serializable {
    private static final long serialVersionUID = 1L;

    private int anio;                       // Año fiscal de la declaración
    private String estado;                  // Estado de la declaración (pendiente, por pagar, saldo a favor, etc.)
    private ArrayList<Sueldo> sueldosMensuales; // Lista de sueldos de cada mes
    private ArrayList<Factura> facturas;        // Lista de facturas registradas
    private double ingresoAnual;            // Suma de todos los sueldos
    private double gastosDeducibles;        // Suma de todos los gastos deducibles (respetando límites)
    private double baseImponible;           // Ingresos - gastos deducibles - aportes IESS
    private double impuestoCausado;         // Impuesto calculado según la tabla
    private double impuestoRetenido;        // Impuesto ya retenido por el empleador
    private double saldoAPagar;             // Diferencia entre impuesto causado y retenido
    private TablaImpuestoRenta tablaImpuesto; // Tabla oficial de impuesto a la renta

    // Constructor por defecto (usa el año actual)
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

    // Constructor con año específico
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

    // Métodos para obtener y modificar los datos principales
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

    // Agrega o reemplaza el sueldo de un mes
    public void agregarSueldo(Sueldo sueldo) {
        // Si ya existe un sueldo para ese mes, lo reemplaza
        for (int i = 0; i < sueldosMensuales.size(); i++) {
            if (sueldosMensuales.get(i).getMes() == sueldo.getMes()) {
                sueldosMensuales.set(i, sueldo);
                return;
            }
        }
        // Si no existe, lo agrega
        sueldosMensuales.add(sueldo);
    }

    // Agrega una factura a la lista
    public void agregarFactura(Factura factura) {
        facturas.add(factura);
    }

    // Valida que ningún gasto por categoría exceda el límite legal
    public boolean validarLimitesDeducibles() {
        double ingresoAnual = calcularIngresoAnual();
        Map<CategoriaGasto, Double> gastosPorCategoria = getTotalGastosPorCategoria();
        Map<CategoriaGasto, Double> limites = getLimitesDeduciblesPorCategoria();

        for (CategoriaGasto categoria : gastosPorCategoria.keySet()) {
            double gastoCategoria = gastosPorCategoria.get(categoria);
            double limiteCategoria = limites.get(categoria);

            if (gastoCategoria > limiteCategoria) {
                return false; // Si algún gasto excede el límite, la declaración no es válida
            }
        }
        return true;
    }

    // Calcula la suma de todos los sueldos y el impuesto retenido
    public double calcularIngresoAnual() {
        ingresoAnual = 0.0;
        impuestoRetenido = 0.0;

        for (Sueldo sueldo : sueldosMensuales) {
            ingresoAnual += sueldo.getMonto();
            impuestoRetenido += sueldo.getImpuestoRetenido();
        }

        return ingresoAnual;
    }

    // Calcula la suma de todos los gastos deducibles (respetando los límites por categoría)
    public double calcularGastosDeducibles() {
        gastosDeducibles = 0.0;
        Map<CategoriaGasto, Double> gastosPorCategoria = getTotalGastosPorCategoria();
        Map<CategoriaGasto, Double> limites = getLimitesDeduciblesPorCategoria();

        for (CategoriaGasto categoria : gastosPorCategoria.keySet()) {
            double gastoCategoria = gastosPorCategoria.get(categoria);
            double limiteCategoria = limites.get(categoria);

            // Solo suma hasta el límite permitido
            gastosDeducibles += Math.min(gastoCategoria, limiteCategoria);
        }

        return gastosDeducibles;
    }

    // Calcula la base imponible (ingresos - gastos deducibles - aportes IESS)
    public double calcularBaseImponible() {
        calcularIngresoAnual();
        calcularGastosDeducibles();

        double totalAportesIESS = 0.0;
        for (Sueldo sueldo : sueldosMensuales) {
            totalAportesIESS += sueldo.getAporteIESS();
        }

        baseImponible = ingresoAnual - gastosDeducibles - totalAportesIESS;
        if (baseImponible < 0) {
            baseImponible = 0;
        }

        return baseImponible;
    }

    // Calcula el impuesto causado usando la tabla oficial
    public double calcularImpuestoCausado() {
        calcularBaseImponible();
        impuestoCausado = tablaImpuesto.calcularImpuesto(baseImponible);
        return impuestoCausado;
    }

    // Calcula el saldo a pagar (impuesto causado - impuesto retenido)
    public double calcularSaldoAPagar() {
        calcularImpuestoCausado();
        saldoAPagar = impuestoCausado - impuestoRetenido;
        if (saldoAPagar < 0) {
            estado = "Saldo a favor";
        } else if (saldoAPagar > 0) {
            estado = "Por pagar";
        } else {
            estado = "Declaración en cero";
        }
        return saldoAPagar;
    }

    // Devuelve un resumen de la declaración en forma de texto
    public String[] getDatosResumen() {
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

    // Calcula el total de aportes al IESS de todos los sueldos
    private double calcularTotalAportesIESS() {
        double total = 0.0;
        for (Sueldo sueldo : sueldosMensuales) {
            total += sueldo.getAporteIESS();
        }
        return total;
    }

    // Métodos para obtener las listas de sueldos y facturas
    public ArrayList<Sueldo> getSueldosMensuales() {
        return sueldosMensuales;
    }

    public ArrayList<Factura> getFacturas() {
        return facturas;
    }

    // Devuelve una tabla con el total de gastos por cada categoría
    public Map<CategoriaGasto, Double> getTotalGastosPorCategoria() {
        Map<CategoriaGasto, Double> gastosPorCategoria = new HashMap<>();

        // Inicializa todas las categorías en 0
        for (CategoriaGasto categoria : CategoriaGasto.values()) {
            gastosPorCategoria.put(categoria, 0.0);
        }

        // Suma los gastos de cada factura por categoría
        for (Factura factura : facturas) {
            if (factura.isDeducible() && factura.getCategoria() != null) {
                CategoriaGasto categoria = factura.getCategoria();
                double valorActual = gastosPorCategoria.get(categoria);
                gastosPorCategoria.put(categoria, valorActual + factura.getMonto());
            }
        }

        return gastosPorCategoria;
    }

    // Devuelve una tabla con los límites deducibles por categoría según los ingresos
    public Map<CategoriaGasto, Double> getLimitesDeduciblesPorCategoria() {
        Map<CategoriaGasto, Double> limites = new HashMap<>();
        double ingresos = calcularIngresoAnual();

        for (CategoriaGasto categoria : CategoriaGasto.values()) {
            limites.put(categoria, categoria.getLimiteMaximoDeducible(ingresos));
        }

        return limites;
    }

    // Representación en texto de la declaración (útil para depuración)
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