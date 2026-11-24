package edu.ucab.app.console;

import edu.ucab.app.exception.EntidadNoEncontradaException;
import edu.ucab.app.exception.IdDuplicadoException;
import edu.ucab.app.model.Producto;
import edu.ucab.app.service.ProductoService;
import java.io.PrintStream;
import java.util.List;

// Submenú de productos. Solo lee, imprime y delega en ProductoService.
public class MenuProductos {

    private static final int REGISTRAR = 1;
    private static final int LISTAR = 2;
    private static final int BUSCAR = 3;
    private static final int VOLVER = 4;

    private final ProductoService servicio;
    private final Lector lector;
    private final PrintStream salida;

    public MenuProductos(ProductoService servicio, Lector lector, PrintStream salida) {
        this.servicio = servicio;
        this.lector = lector;
        this.salida = salida;
    }

    public void ejecutar() {
        boolean volver = false;
        while (!volver) {
            salida.println("\n--- PRODUCTOS ---");
            salida.println("1. Registrar  2. Listar  3. Buscar por ID  4. Volver");
            int opcion = lector.leerOpcion();
            switch (opcion) {
                case REGISTRAR:
                    registrar();
                    break;
                case LISTAR:
                    listar();
                    break;
                case BUSCAR:
                    buscar();
                    break;
                case VOLVER:
                case Lector.FIN_ENTRADA:
                    volver = true;
                    break;
                default:
                    salida.println("Opción inválida.");
            }
        }
    }

    private void registrar() {
        String id = lector.pedirTexto("ID: ");
        String descripcion = lector.pedirTexto("Descripción: ");
        String textoPrecio = lector.pedirTexto("Precio: ");
        double precio;
        try {
            // acepta "2,50" y "2.50"
            precio = Double.parseDouble(textoPrecio.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            salida.println("Error: el precio debe ser un número.");
            return;
        }
        try {
            servicio.registrar(id, descripcion, precio);
            salida.println("Producto registrado correctamente.");
        } catch (IdDuplicadoException e) {
            salida.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            salida.println("Error: " + e.getMessage());
        }
    }

    private void listar() {
        List<Producto> productos = servicio.listar();
        if (productos.isEmpty()) {
            salida.println("No hay productos registrados.");
            return;
        }
        for (Producto p : productos) {
            salida.println(p);
        }
    }

    private void buscar() {
        try {
            salida.println(servicio.buscarPorId(lector.pedirTexto("ID: ")));
        } catch (EntidadNoEncontradaException e) {
            salida.println("Error: " + e.getMessage());
        }
    }
}
