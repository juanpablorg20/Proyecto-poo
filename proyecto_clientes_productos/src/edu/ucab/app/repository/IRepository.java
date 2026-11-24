package edu.ucab.app.repository;

import edu.ucab.app.model.Identificable;
import java.util.List;
import java.util.Optional;

// Contrato genérico de almacenamiento. Los servicios dependen de esta
// abstracción (DIP) y no de una colección concreta.
public interface IRepository<T extends Identificable> {

    void guardar(T entidad);

    // Optional evita devolver null cuando no existe.
    Optional<T> buscarPorId(String id);

    boolean existePorId(String id);

    // Devuelve una copia: consultar no altera el almacén.
    List<T> listarTodos();
}
