package Vista;

import Controlador.SistemaController;
import Modelo.CategoriaGasto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Scanner;

public class MenuPrincipal {
    private final SistemaController controller;
    private final Scanner scanner;

    public MenuPrincipal() {
        this.controller = new SistemaController();
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n===== SISTEMA DE DECLARACIÓN DE IMPUESTOS 2025 =====");
            System.out.println("[1] Registrar datos del contribuyente");
            System.out.println("[2] Registrar sueldo mensual");
            System.out.println("[3] Registrar factura");
            System.out.println("[4] Validar datos de la declaración");
            System.out.println("[5] Calcular impuesto a la renta");
            System.out.println("[6] Ver resumen de la declaración");
            System.out.println("[7] Guardar declaración");
            System.out.println("[8] Cargar declaración guardada");
            System.out.println("[0] Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = leerEntero("", 0, 8);

            switch (opcion) {
                case 1:
                    registrarContribuyente();
                    break;
                case 2:
                    registrarSueldo();
                    break;
                case 3:
                    registrarFactura();
                    break;
                case 4:
                    validarDeclaracion();
                    break;
                case 5:
                    calcularImpuesto();
                    break;
                case 6:
                    verResumenDeclaracion();
                    break;
                case 7:
                    guardarDeclaracion();
                    break;
                case 8:
                    cargarDeclaracion();
                    break;
                case 0:
                    salir = true;
                    System.out.println("Gracias por utilizar el sistema.");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }

        // Cerrar el scanner al finalizar
        scanner.close();
    }

    private void registrarContribuyente() {
        System.out.println("\n----- REGISTRO DE CONTRIBUYENTE -----");

        String nombre = leerDatos("Ingrese el nombre completo: ");
        String cedula = leerDatos("Ingrese la cédula (10 dígitos): ");
        String direccion = leerDatos("Ingrese la dirección: ");
        String telefono = leerDatos("Ingrese el teléfono: ");
        String email = leerDatos("Ingrese el correo electrónico: ");
        String ocupacion = leerDatos("Ingrese la ocupación: ");

        boolean resultado = controller.registrarContribuyente(nombre, cedula, direccion, telefono, email, ocupacion);

        if (resultado) {
            System.out.println("Contribuyente registrado exitosamente.");
        } else {
            System.out.println("No se pudo registrar el contribuyente. Verifique los datos e intente nuevamente.");
        }
    }

    private void registrarSueldo() {
        if (controller.getContribuyente() == null) {
            System.out.println("Debe registrar un contribuyente primero.");
            return;
        }

        System.out.println("\n----- REGISTRO DE SUELDO MENSUAL -----");

        int mes = leerEntero("Ingrese el mes (1-12): ", 1, 12);
        double monto = leerNumero("Ingrese el monto del sueldo: $", 0, Double.MAX_VALUE);
        double aporteIESS = leerNumero("Ingrese el aporte al IESS: $", 0, monto);
        double impuestoRetenido = leerNumero("Ingrese el impuesto retenido (si aplica): $", 0, monto);

        boolean resultado = controller.agregarSueldo(mes, monto, aporteIESS, impuestoRetenido);

        if (resultado) {
            System.out.println("Sueldo registrado exitosamente.");
        } else {
            System.out.println("No se pudo registrar el sueldo. Verifique los datos e intente nuevamente.");
        }
    }

    private void registrarFactura() {
        if (controller.getContribuyente() == null) {
            System.out.println("Debe registrar un contribuyente primero.");
            return;
        }

        System.out.println("\n----- REGISTRO DE FACTURA -----");

        String numero = leerDatos("Ingrese el número de factura (formato: 001-001-000000001): ");
        String proveedor = leerDatos("Ingrese el nombre del proveedor: ");
        LocalDate fecha = leerFecha("Ingrese la fecha de la factura (formato: YYYY-MM-DD): ");
        double monto = leerNumero("Ingrese el monto de la factura (sin IVA): $", 0.01, Double.MAX_VALUE);
        double iva = leerNumero("Ingrese el valor del IVA: $", 0, Double.MAX_VALUE);

        System.out.println("Seleccione la categoría del gasto:");
        CategoriaGasto categoria = leerCategoria();

        boolean resultado = controller.agregarFactura(numero, proveedor, fecha, monto, iva, categoria);

        if (resultado) {
            System.out.println("Factura registrada exitosamente.");
        } else {
            System.out.println("No se pudo registrar la factura. Verifique los datos e intente nuevamente.");
        }
    }

    private void validarDeclaracion() {
        if (controller.getContribuyente() == null) {
            System.out.println("Debe registrar un contribuyente primero.");
            return;
        }

        System.out.println("\n----- VALIDACIÓN DE DECLARACIÓN -----");

        boolean valido = controller.validarDatosDeclaracion();

        if (valido) {
            System.out.println("La declaración es válida.");

            // Mostrar tabla de gastos vs límites
            Map<CategoriaGasto, Double> gastos = controller.obtenerGastosPorCategoria();
            Map<CategoriaGasto, Double> limites = controller.obtenerLimitesDeducibles();

            mostrarTablaGastos(gastos, limites);
        } else {
            System.out.println("La declaración NO es válida. Se exceden los límites deducibles.");

            // Mostrar tabla de gastos vs límites para identificar problemas
            Map<CategoriaGasto, Double> gastos = controller.obtenerGastosPorCategoria();
            Map<CategoriaGasto, Double> limites = controller.obtenerLimitesDeducibles();

            mostrarTablaGastos(gastos, limites);
        }
    }

    private void calcularImpuesto() {
        if (controller.getContribuyente() == null) {
            System.out.println("Debe registrar un contribuyente primero.");
            return;
        }

        System.out.println("\n----- CÁLCULO DE IMPUESTO A LA RENTA -----");

        double saldoAPagar = controller.calcularDeclaracion();

        System.out.println("Cálculo realizado exitosamente.");

        if (saldoAPagar > 0) {
            System.out.println("Saldo a pagar: $" + String.format("%.2f", saldoAPagar));
        } else if (saldoAPagar < 0) {
            System.out.println("Saldo a favor: $" + String.format("%.2f", Math.abs(saldoAPagar)));
        } else {
            System.out.println("Declaración en cero.");
        }
    }

    private void verResumenDeclaracion() {
        if (controller.getContribuyente() == null) {
            System.out.println("Debe registrar un contribuyente primero.");
            return;
        }

        System.out.println("\n----- RESUMEN DE DECLARACIÓN -----");
        System.out.println("Contribuyente: " + controller.getContribuyente().getNombre());
        System.out.println("Cédula: " + controller.getContribuyente().getCedula());

        String[] resumen = controller.obtenerResumenDeclaracion();
        mostrarResultados(resumen);
    }

    private void guardarDeclaracion() {
        if (controller.getContribuyente() == null) {
            System.out.println("Debe registrar un contribuyente primero.");
            return;
        }

        System.out.println("\n----- GUARDAR DECLARACIÓN -----");

        String nombreArchivo = leerDatos("Ingrese el nombre del archivo para guardar (o Enter para 'declaracion.dat'): ");
        if (nombreArchivo.isEmpty()) {
            nombreArchivo = "declaracion.dat";
        }

        boolean resultado = controller.guardarDatos(nombreArchivo);

        if (resultado) {
            System.out.println("Declaración guardada exitosamente en '" + nombreArchivo + "'.");
        } else {
            System.out.println("No se pudo guardar la declaración. Intente nuevamente.");
        }
    }

    private void cargarDeclaracion() {
        System.out.println("\n----- CARGAR DECLARACIÓN -----");

        String nombreArchivo = leerDatos("Ingrese el nombre del archivo a cargar (o Enter para 'declaracion.dat'): ");
        if (nombreArchivo.isEmpty()) {
            nombreArchivo = "declaracion.dat";
        }

        boolean resultado = controller.cargarDatos(nombreArchivo);

        if (resultado) {
            System.out.println("Declaración cargada exitosamente desde '" + nombreArchivo + "'.");
            System.out.println("Contribuyente: " + controller.getContribuyente().getNombre());
        } else {
            System.out.println("No se pudo cargar la declaración. Verifique el nombre del archivo e intente nuevamente.");
        }
    }

    public String leerDatos(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    public double leerNumero(String mensaje, double minimo, double maximo) {
        double numero = 0;
        boolean valido = false;

        while (!valido) {
            System.out.print(mensaje);
            try {
                numero = Double.parseDouble(scanner.nextLine());
                if (numero >= minimo && numero <= maximo) {
                    valido = true;
                } else {
                    System.out.println("El número debe estar entre " + minimo + " y " + maximo + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }

        return numero;
    }

    public int leerEntero(String mensaje, int minimo, int maximo) {
        int numero = 0;
        boolean valido = false;

        while (!valido) {
            if (!mensaje.isEmpty()) {
                System.out.print(mensaje);
            }
            try {
                numero = Integer.parseInt(scanner.nextLine());
                if (numero >= minimo && numero <= maximo) {
                    valido = true;
                } else {
                    System.out.println("El número debe estar entre " + minimo + " y " + maximo + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número entero válido.");
            }
        }

        return numero;
    }

    public LocalDate leerFecha(String mensaje) {
        LocalDate fecha = null;
        boolean valido = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (!valido) {
            System.out.print(mensaje);
            try {
                String input = scanner.nextLine();
                fecha = LocalDate.parse(input, formatter);
                valido = true;
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha incorrecto. Use el formato YYYY-MM-DD.");
            }
        }

        return fecha;
    }

    public CategoriaGasto leerCategoria() {
        CategoriaGasto[] categorias = CategoriaGasto.values();

        for (int i = 0; i < categorias.length; i++) {
            System.out.println((i+1) + ". " + categorias[i]);
        }

        int seleccion = leerEntero("Seleccione una categoría (1-" + categorias.length + "): ", 1, categorias.length);
        return categorias[seleccion - 1];
    }

    public void mostrarResultados(String[] resultados) {
        for (String linea : resultados) {
            System.out.println(linea);
        }
    }

    public void mostrarTablaGastos(Map<CategoriaGasto, Double> gastos, Map<CategoriaGasto, Double> limites) {
        System.out.println("\nRESUMEN DE GASTOS DEDUCIBLES POR CATEGORÍA");
        System.out.println("+-----------------+------------------+------------------+-----------------+");
        System.out.println("| CATEGORÍA       | GASTO ACUMULADO  | LÍMITE DEDUCIBLE | ESTADO         |");
        System.out.println("+-----------------+------------------+------------------+-----------------+");

        for (CategoriaGasto categoria : CategoriaGasto.values()) {
            double gasto = gastos.get(categoria);
            double limite = limites.get(categoria);
            String estado = (gasto <= limite) ? "OK" : "EXCEDE LÍMITE";

            System.out.printf("| %-15s | $%14.2f | $%14.2f | %-15s |\n",
                    categoria, gasto, limite, estado);
        }

        System.out.println("+-----------------+------------------+------------------+-----------------+");
    }

    public static void main(String[] args) {
        MenuPrincipal menu = new MenuPrincipal();
        menu.mostrarMenu();
    }
}