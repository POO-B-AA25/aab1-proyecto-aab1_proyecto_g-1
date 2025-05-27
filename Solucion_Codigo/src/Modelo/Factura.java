package Modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Factura implements Serializable {
    private static final long serialVersionUID = 1L;

    private String numero;
    private String proveedor;
    private LocalDate fecha;
    private double monto;
    private double iva;
    private CategoriaGasto categoria;
    private boolean deducible;

    public Factura() {
        this.numero = "";
        this.proveedor = "";
        this.fecha = LocalDate.now();
        this.monto = 0.0;
        this.iva = 0.0;
        this.categoria = null;
        this.deducible = false;
    }

    public Factura(String numero, String proveedor, LocalDate fecha, double monto, double iva, CategoriaGasto categoria) {
        this.numero = numero;
        this.proveedor = proveedor;
        this.fecha = fecha;
        this.monto = monto;
        this.iva = iva;
        this.categoria = categoria;
        this.deducible = true; // Por defecto se asume deducible
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public CategoriaGasto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaGasto categoria) {
        this.categoria = categoria;
    }

    public boolean isDeducible() {
        return deducible;
    }

    public void setDeducible(boolean deducible) {
        this.deducible = deducible;
    }

    public double getMontoDeducible() {
        return deducible ? monto : 0.0;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "Factura{" +
                "numero='" + numero + '\'' +
                ", proveedor='" + proveedor + '\'' +
                ", fecha=" + fecha.format(formatter) +
                ", monto=" + monto +
                ", iva=" + iva +
                ", categoria=" + categoria +
                ", deducible=" + deducible +
                '}';
    }
}