package edu.ucab.app.console;

import edu.ucab.app.exception.EntidadNoEncontradaException;
import edu.ucab.app.exception.IdDuplicadoException;
import edu.ucab.app.model.Cliente;
import edu.ucab.app.service.ClienteService;
import java.io.PrintStream;
import java.util.List;

// Submenú de clientes. Solo lee, imprime y delega en ClienteService.
public class MenuClientes {

    private static final int REGISTRAR = 1;
    private static final int LISTAR = 2;
    private static final int BUSCAR = 3;
    private static final int VOLVER = 4;

    private final ClienteService servicio;
    private final Lector lector;
    private final PrintStream salida;

    public MenuClientes(ClienteService servicio, Lector lector, PrintStream salida) {
        this.servicio = servicio;
        this.lector = lector;
        this.salida = salida;
    }

    public void ejecutar() {
        boolean volver = false;
        while (!volver) {
            salida.println("\n--- CLIENTES ---");
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
        String nombre = lector.pedirTexto("Nombre: ");
        try {
            servicio.registrar(id, nombre);
            salida.println("Cliente registrado correctamente.");
        } catch (IdDuplicadoException e) {
            salida.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            salida.println("Error: " + e.getMessage());
        }
    }

    private void listar() {
        List<Cliente> clientes = servicio.listar();
        if (clientes.isEmpty()) {
            salida.println("No hay clientes registrados.");
            return;
        }
        for (Cliente c : clientes) {
            salida.println(c);
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
