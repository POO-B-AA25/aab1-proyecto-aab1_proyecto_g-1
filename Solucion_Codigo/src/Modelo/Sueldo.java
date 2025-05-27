package Modelo;

import java.io.Serializable;

public class Sueldo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int mes;
    private double monto;
    private double aporteIESS;
    private double impuestoRetenido;

    public Sueldo() {
        this.mes = 0;
        this.monto = 0.0;
        this.aporteIESS = 0.0;
        this.impuestoRetenido = 0.0;
    }

    public Sueldo(int mes, double monto, double aporteIESS, double impuestoRetenido) {
        this.mes = mes;
        this.monto = monto;
        this.aporteIESS = aporteIESS;
        this.impuestoRetenido = impuestoRetenido;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getAporteIESS() {
        return aporteIESS;
    }

    public void setAporteIESS(double aporteIESS) {
        this.aporteIESS = aporteIESS;
    }

    public double getImpuestoRetenido() {
        return impuestoRetenido;
    }

    public void setImpuestoRetenido(double impuestoRetenido) {
        this.impuestoRetenido = impuestoRetenido;
    }

    public double getMontoNeto() {
        return monto - aporteIESS - impuestoRetenido;
    }

    @Override
    public String toString() {
        return "Sueldo{" +
                "mes=" + mes +
                ", monto=" + monto +
                ", aporteIESS=" + aporteIESS +
                ", impuestoRetenido=" + impuestoRetenido +
                ", montoNeto=" + getMontoNeto() +
                '}';
    }
}