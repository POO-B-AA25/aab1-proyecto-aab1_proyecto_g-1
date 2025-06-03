# Sistema de Declaración de Impuestos 2025

## Introducción

Este proyecto implementa un sistema para gestionar la declaración de impuestos personales, aplicando el patrón de diseño MVC para separar la lógica de datos, la interacción con el usuario y el control del flujo.

El usuario puede registrar sus ingresos, gastos deducibles y datos personales, y el sistema valida automáticamente la información según las reglas tributarias ecuatorianas (como límites de deducción y formato de cédula). Además, permite calcular el impuesto a la renta, mostrar un resumen detallado y guardar o cargar la declaración.

El objetivo principal es facilitar el cumplimiento tributario, guiando al usuario paso a paso y asegurando que los datos sean correctos y completos. El uso de MVC hace que el código sea más organizado, mantenible y fácil de entender, lo que es fundamental en la programación orientada a objetos.

## Documentación Detallada

--

## ¿Qué es MVC y cómo se aplica aquí?

### ¿Qué es MVC?

**MVC** significa **Modelo-Vista-Controlador**. Es una forma de organizar el código de un programa para que sea más fácil de entender, mantener y mejorar.
- **Modelo:** Son las clases que guardan y procesan los datos (como el contribuyente, los sueldos, las facturas, los cálculos de impuestos).
- **Vista:** Es la parte que muestra la información al usuario y recibe lo que escribe (en este caso, los menús y mensajes en la consola).
- **Controlador:** Es el que conecta la vista con el modelo, recibe las órdenes del usuario y decide qué hacer con los datos.

### ¿Cómo se aplica MVC en este proyecto?

- **Modelo:**  
  Todas las clases dentro del paquete `Modelo` (`Contribuyente`, `Declaracion`, `Sueldo`, `Factura`, `CategoriaGasto`, `TablaImpuestoRenta`, etc.) son el **Modelo**. Aquí se guardan los datos y se hacen los cálculos.

- **Vista:**  
  La clase `MenuPrincipal` en el paquete `Vista` es la **Vista**. Aquí se muestran los menús, se piden los datos al usuario y se muestran los resultados y mensajes.

- **Controlador:**  
  La clase `SistemaController` en el paquete `Controlador` es el **Controlador**. Esta clase recibe las acciones de la vista, valida los datos, llama a los métodos del modelo y devuelve los resultados a la vista.

**Ejemplo de flujo MVC en este sistema:**
1. El usuario elige una opción en el menú (Vista).
2. La Vista llama al Controlador para procesar la acción.
3. El Controlador valida y manipula los datos usando el Modelo.
4. El Controlador devuelve el resultado a la Vista.
5. La Vista muestra el resultado al usuario.

Esto permite que cada parte del programa tenga una responsabilidad clara y que el código sea más ordenado y fácil de modificar.

---

## Estructura del Proyecto

- [`src/Main.java`](src/Main.java): Punto de entrada del programa.
- [`src/Vista/MenuPrincipal.java`](src/Vista/MenuPrincipal.java): Interfaz de usuario por consola.
- [`src/Controlador/SistemaController.java`](src/Controlador/SistemaController.java): Lógica de control y validación.
- [`src/Modelo/`](src/Modelo/): Clases de datos y lógica de negocio (Contribuyente, Declaracion, Sueldo, Factura, etc.).

---

## Flujo General del Usuario

Al ejecutar el programa, el usuario verá el siguiente menú principal en pantalla:

```
--- SISTEMA DE DECLARACIÓN DE IMPUESTOS 2025 ---
[1] Registrar datos del contribuyente
[2] Registrar sueldo mensual
[3] Registrar factura
[4] Validar datos de la declaración
[5] Calcular impuesto a la renta
[6] Ver resumen de la declaración
[7] Guardar declaración
[8] Cargar declaración guardada
[0] Salir
Seleccione una opción:
```

Cada opción guía al usuario paso a paso, validando los datos y mostrando mensajes claros.

---

## Detalle de Cada Opción y Mensajes en Pantalla

### 1. Registrar datos del contribuyente

**Pantalla:**
```
--- REGISTRO DE CONTRIBUYENTE  ---
Ingrese el nombre completo:
Ingrese la cédula (10 dígitos):
Ingrese la dirección:
Ingrese el teléfono:
Ingrese el correo electrónico:
Ingrese la ocupación:
```

- **Validaciones:**  
  - La cédula debe tener 10 dígitos y ser válida según el algoritmo ecuatoriano.
  - El correo debe tener formato correcto.
- **Mensajes de éxito/error:**
  - `Contribuyente registrado exitosamente.`
  - `No se pudo registrar el contribuyente. Verifique los datos e intente nuevamente.`

---

### 2. Registrar sueldo mensual

**Pantalla:**
```
--- REGISTRO DE SUELDO MENSUAL  ---
Ingrese el mes (1-12):
Ingrese el monto del sueldo: $
Ingrese el aporte al IESS: $
Ingrese el impuesto retenido (si aplica): $
```

- **Explicación de campos:**
  - **Mes:** Número del mes (1 para enero, 2 para febrero, etc.).
  - **Monto del sueldo:** Dinero recibido ese mes.
  - **Aporte al IESS:** Dinero descontado para el seguro social (puede ser por afiliación voluntaria o relación de dependencia).
  - **Impuesto retenido:** Si tu empleador ya te descontó impuestos, pon aquí ese valor.
- **Mensajes de éxito/error:**
  - `Sueldo registrado exitosamente.`
  - `No se pudo registrar el sueldo. Verifique los datos e intente nuevamente.`

---

### 3. Registrar factura

**Pantalla:**
```
--- REGISTRO DE FACTURA  ---
Ingrese el número de factura (formato: 001-001-000000001):
Ingrese el nombre del proveedor:
Ingrese la fecha de la factura (formato: YYYY-MM-DD):
Ingrese el monto de la factura (sin IVA): $
Ingrese el valor del IVA: $
Seleccione la categoría del gasto:
1. VIVIENDA
2. EDUCACION
3. ALIMENTACION
4. VESTIMENTA
5. SALUD
6. TURISMO
Seleccione una categoría (1-6):
```

- **Validaciones:**
  - El número de factura debe tener el formato correcto.
  - La fecha debe ser del año de la declaración.
  - El monto debe ser positivo.
- **Mensajes de éxito/error:**
  - `Factura registrada exitosamente.`
  - `No se pudo registrar la factura. Verifique los datos e intente nuevamente.`

---

### 4. Validar datos de la declaración

**Pantalla:**
```
--- VALIDACIÓN DE DECLARACIÓN  ---
La declaración es válida.
```
o
```
La declaración NO es válida. Se exceden los límites deducibles.
```

- **Además, muestra una tabla como:**
```
RESUMEN DE GASTOS DEDUCIBLES POR CATEGORÍA
+-----------------+------------------+------------------+-----------------+
| CATEGORÍA       | GASTO ACUMULADO  | LÍMITE DEDUCIBLE | ESTADO          |
+-----------------+------------------+------------------+-----------------+
| VIVIENDA        | $     1200.00    | $     3800.00    | OK              |
| EDUCACION       | $      800.00    | $     3800.00    | OK              |
| ...             | ...              | ...              | ...             |
+-----------------+------------------+------------------+-----------------+
```
- **Estado:**  
  - "OK" si el gasto está dentro del límite.
  - "EXCEDE LÍMITE" si se pasó del máximo permitido.

---

### 5. Calcular impuesto a la renta

**Pantalla:**
```
--- CÁLCULO DE IMPUESTO A LA RENTA  ---
Cálculo realizado exitosamente.
Saldo a pagar: $123.45
```
o
```
Saldo a favor: $50.00
```
o
```
Declaración en cero.
```

- **Explicación:**  
  - El sistema calcula automáticamente si debes pagar, si te deben devolver dinero o si no hay saldo.

---

### 6. Ver resumen de la declaración

**Pantalla:**
```
--- RESUMEN DE DECLARACIÓN  ---
Contribuyente: Juan Pérez
Cédula: 1234567890
Año fiscal: 2025
Estado: Por pagar
Ingresos anuales: $12000.00
Aportes IESS: $1200.00
Gastos deducibles: $3000.00
Base imponible: $7800.00
Impuesto causado: $200.00
Impuesto retenido: $50.00
Saldo a pagar: $150.00
Total facturas registradas: 10
```

- **Cada línea explica un dato clave de la declaración.**

---

### 7. Guardar declaración

**Pantalla:**
```
--- GUARDAR DECLARACIÓN  ---
Ingrese el nombre del archivo para guardar (o Enter para 'declaracion.dat'):
Declaración guardada exitosamente en 'declaracion.dat'.
```
o
```
No se pudo guardar la declaración. Intente nuevamente.
```

---

### 8. Cargar declaración guardada

**Pantalla:**
```
--- CARGAR DECLARACIÓN  ---
Ingrese el nombre del archivo a cargar (o Enter para 'declaracion.dat'):
Declaración cargada exitosamente desde 'declaracion.dat'.
Contribuyente: Juan Pérez
```
o
```
No se pudo cargar la declaración. Verifique el nombre del archivo e intente nuevamente.
```

---

### 0. Salir

**Pantalla:**
```
Gracias por utilizar el sistema.
```

---

## Validaciones y Seguridad

- **Cédula:** Algoritmo oficial ecuatoriano.
- **Correo:** Formato estándar.
- **Factura:** Formato `001-001-000000001`.
- **Fechas:** Solo del año fiscal correspondiente.
- **Montos:** No se aceptan valores negativos.
- **Límites de gastos:** El sistema valida automáticamente los topes legales por categoría.

---

## Lógica de Cálculo

- **Ingresos anuales:** Suma de todos los sueldos registrados.
- **Aportes IESS:** Suma de todos los aportes mensuales.
- **Gastos deducibles:** Suma de facturas por categoría, respetando los límites legales.
- **Base imponible:** Ingresos - Gastos deducibles - Aportes IESS.
- **Impuesto causado:** Calculado usando la tabla oficial de impuesto a la renta.
- **Saldo a pagar:** Impuesto causado menos impuesto ya retenido.

---

## Ejemplo de Uso

1. Registrar tus datos personales.
2. Registrar cada mes tu sueldo, aporte IESS y si te retuvieron impuesto.
3. Registrar cada factura de tus gastos deducibles.
4. Validar la declaración para ver si algún gasto excede el límite.
5. Calcular el impuesto.
6. Ver el resumen.
7. Guardar tu declaración para no perder los datos.
8. Cargar tu declaración cuando quieras continuar.

---
