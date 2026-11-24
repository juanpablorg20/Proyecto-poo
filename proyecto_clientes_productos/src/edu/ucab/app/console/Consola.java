package edu.ucab.app.console;

import java.io.PrintStream;

// Menú principal: solo decide a qué submenú ir.
public class Consola {

    private static final int CLIENTES = 1;
    private static final int PRODUCTOS = 2;
    private static final int SALIR = 3;

    private final MenuClientes menuClientes;
    private final MenuProductos menuProductos;
    private final Lector lector;
    private final PrintStream salida;

    public Consola(MenuClientes menuClientes, MenuProductos menuProductos,
                   Lector lector, PrintStream salida) {
        this.menuClientes = menuClientes;
        this.menuProductos = menuProductos;
        this.lector = lector;
        this.salida = salida;
    }

    public void ejecutar() {
        boolean salir = false;
        while (!salir) {
            salida.println("\n=== MENÚ PRINCIPAL ===");
            salida.println("1. Clientes");
            salida.println("2. Productos");
            salida.println("3. Salir");
            int opcion = lector.leerOpcion();
            switch (opcion) {
                case CLIENTES:
                    menuClientes.ejecutar();
                    break;
                case PRODUCTOS:
                    menuProductos.ejecutar();
                    break;
                case SALIR:
                case Lector.FIN_ENTRADA:
                    salir = true;
                    break;
                default:
                    salida.println("Opción inválida.");
            }
        }
        salida.println("Hasta luego.");
    }
}
