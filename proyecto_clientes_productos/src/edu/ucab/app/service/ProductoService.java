package edu.ucab.app.service;

import edu.ucab.app.exception.IdDuplicadoException;
import edu.ucab.app.model.Producto;
import edu.ucab.app.repository.IRepository;

// Reglas de negocio de productos (FR-02). No sabe nada de la consola.
public class ProductoService extends ServicioBase<Producto> {

    public ProductoService(IRepository<Producto> repositorio) {
        super(repositorio, "Producto");
    }

    // Comando: registra un producto nuevo y no devuelve nada (CQS).
    // IllegalArgumentException: id o descripción vacíos, o precio <= 0 (lo lanza Producto).
    // IdDuplicadoException: ya existe un producto con ese id.
    public void registrar(String id, String descripcion, double precio) throws IdDuplicadoException {
        Producto producto = new Producto(id, descripcion, precio); // un producto inválido nunca entra
        guardarSiNoExiste(producto);
    }
}
