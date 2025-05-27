package Modelo;

import java.io.Serializable;

public class Contribuyente implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String cedula;
    private String direccion;
    private String telefono;
    private String email;
    private String ocupacion;

    public Contribuyente() {
        this.nombre = "";
        this.cedula = "";
        this.direccion = "";
        this.telefono = "";
        this.email = "";
        this.ocupacion = "";
    }

    public Contribuyente(String nombre, String cedula, String direccion, String telefono, String email, String ocupacion) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.ocupacion = ocupacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String[] getDatos() {
        return new String[]{nombre, cedula, direccion, telefono, email, ocupacion};
    }

    public void setDatos(String nombre, String cedula, String direccion, String telefono, String email, String ocupacion) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.ocupacion = ocupacion;
    }

    @Override
    public String toString() {
        return "Contribuyente{" +
                "nombre='" + nombre + '\'' +
                ", cedula='" + cedula + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", ocupacion='" + ocupacion + '\'' +
                '}';
    }
}