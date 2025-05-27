package Modelo;

import java.io.Serializable;

public class RangoImpuesto implements Serializable {
    private static final long serialVersionUID = 1L;

    private double fraccionBasica;
    private double fraccionExcedente;
    private double impuestoFraccionBasica;
    private double porcentajeFraccionExcedente;

    public RangoImpuesto(double fraccionBasica, double fraccionExcedente,
                         double impuestoFraccionBasica, double porcentajeFraccionExcedente) {
        this.fraccionBasica = fraccionBasica;
        this.fraccionExcedente = fraccionExcedente;
        this.impuestoFraccionBasica = impuestoFraccionBasica;
        this.porcentajeFraccionExcedente = porcentajeFraccionExcedente;
    }

    public double getFraccionBasica() {
        return fraccionBasica;
    }

    public double getFraccionExcedente() {
        return fraccionExcedente;
    }

    public double getImpuestoFraccionBasica() {
        return impuestoFraccionBasica;
    }

    public double getPorcentajeFraccionExcedente() {
        return porcentajeFraccionExcedente;
    }

    @Override
    public String toString() {
        return "RangoImpuesto{" +
                "fraccionBasica=" + fraccionBasica +
                ", fraccionExcedente=" + fraccionExcedente +
                ", impuestoFraccionBasica=" + impuestoFraccionBasica +
                ", porcentajeFraccionExcedente=" + porcentajeFraccionExcedente +
                '}';
    }
}