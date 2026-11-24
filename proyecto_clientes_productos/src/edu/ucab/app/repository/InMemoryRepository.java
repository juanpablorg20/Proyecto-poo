package edu.ucab.app.repository;

import edu.ucab.app.model.Identificable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Almacenamiento en memoria con un ArrayList genérico (conserva el orden de registro).
public class InMemoryRepository<T extends Identificable> implements IRepository<T> {

    private List<T> almacen = new ArrayList<>();

    @Override
    public void guardar(T entidad) {
        almacen.add(entidad);
    }

    @Override
    public Optional<T> buscarPorId(String id) {
        T encontrado = null;
        for (T entidad : almacen) {
            if (entidad.getId().equals(id)) {
                encontrado = entidad;
            }
        }
        return Optional.ofNullable(encontrado);
    }

    @Override
    public boolean existePorId(String id) {
        return buscarPorId(id).isPresent();
    }

    @Override
    public List<T> listarTodos() {
        // Se devuelve una copia: quien la reciba no puede alterar el almacén.
        return new ArrayList<>(almacen);
    }
}
