package edu.ucab.app.service;

import edu.ucab.app.exception.IdDuplicadoException;
import edu.ucab.app.model.Cliente;
import edu.ucab.app.repository.IRepository;

// Reglas de negocio de clientes (FR-01). No sabe nada de la consola.
public class ClienteService extends ServicioBase<Cliente> {

    public ClienteService(IRepository<Cliente> repositorio) {
        super(repositorio, "Cliente");
    }

    // Comando: registra un cliente nuevo y no devuelve nada (CQS).
    // Para ver el cliente registrado se usa buscarPorId.
    // IllegalArgumentException: id o nombre nulos o vacíos (lo lanza Cliente).
    // IdDuplicadoException: ya existe un cliente con ese id.
    public void registrar(String id, String nombre) throws IdDuplicadoException {
        Cliente cliente = new Cliente(id, nombre); // valida antes de tocar el almacén
        guardarSiNoExiste(cliente);
    }
}
