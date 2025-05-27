package Modelo;

import java.time.LocalDate;

public class ValidadorDatos {

    public static boolean validarCedula(String cedula) {
        // Validación básica de cédula ecuatoriana
        if (cedula == null || cedula.length() != 10) {
            return false;
        }

        try {
            int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
            int verificador = Character.getNumericValue(cedula.charAt(9));
            int suma = 0;
            int resultado;

            for (int i = 0; i < coeficientes.length; i++) {
                resultado = Character.getNumericValue(cedula.charAt(i)) * coeficientes[i];
                if (resultado >= 10) {
                    resultado -= 9;
                }
                suma += resultado;
            }

            int ultimoDigito = suma % 10;
            return (ultimoDigito == 0 && verificador == 0) || (10 - ultimoDigito == verificador);

        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean validarEmail(String email) {
        // Validación simple de formato de correo electrónico
        if (email == null || email.isEmpty()) {
            return false;
        }

        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(regex);
    }

    public static boolean validarFactura(String numeroFactura) {
        // Validación básica de número de factura (formato: 001-001-000000001)
        if (numeroFactura == null || numeroFactura.isEmpty()) {
            return false;
        }

        String regex = "^\\d{3}-\\d{3}-\\d{9}$";
        return numeroFactura.matches(regex);
    }

    public static boolean validarMontoGasto(CategoriaGasto categoria, double monto, double ingresoAnual) {
        if (categoria == null || monto < 0 || ingresoAnual < 0) {
            return false;
        }

        double limiteCategoria = categoria.getLimiteMaximoDeducible(ingresoAnual);
        return monto <= limiteCategoria;
    }

    public static boolean validarFecha(LocalDate fecha, int anioDeclaracion) {
        if (fecha == null) {
            return false;
        }

        // Verificar que la fecha corresponda al año de declaración
        return fecha.getYear() == anioDeclaracion;
    }
}