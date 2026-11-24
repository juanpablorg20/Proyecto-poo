package edu.ucab.app.console;

import edu.ucab.app.model.Cliente;
import edu.ucab.app.model.Producto;
import edu.ucab.app.repository.InMemoryRepository;
import edu.ucab.app.repository.IRepository;
import edu.ucab.app.service.ClienteService;
import edu.ucab.app.service.ProductoService;
import java.util.Scanner;

// Punto de entrada. Aquí (y solo aquí) se hace new de las piezas concretas
// y se conectan por constructor (inyección de dependencias):
// repositorios -> servicios -> menús -> consola.
public class Main {

    public static void main(String[] args) {
        IRepository<Cliente> repoClientes = new InMemoryRepository<>();
        IRepository<Producto> repoProductos = new InMemoryRepository<>();

        ClienteService clienteService = new ClienteService(repoClientes);
        ProductoService productoService = new ProductoService(repoProductos);

        Lector lector = new Lector(new Scanner(System.in), System.out);
        MenuClientes menuClientes = new MenuClientes(clienteService, lector, System.out);
        MenuProductos menuProductos = new MenuProductos(productoService, lector, System.out);

        Consola consola = new Consola(menuClientes, menuProductos, lector, System.out);
        consola.ejecutar();
    }
}
