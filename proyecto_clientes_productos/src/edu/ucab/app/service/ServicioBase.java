package edu.ucab.app.service;

import edu.ucab.app.exception.EntidadNoEncontradaException;
import edu.ucab.app.exception.IdDuplicadoException;
import edu.ucab.app.model.Identificable;
import edu.ucab.app.repository.IRepository;
import java.util.List;
import java.util.Optional;

// Lógica común a los servicios de clientes y productos (DRY): guardar sin
// duplicar, buscar, preguntar si existe y listar. Es abstracta porque por
// sí sola no representa ningún servicio concreto.
public abstract class ServicioBase<T extends Identificable> {

    private IRepository<T> repositorio;
    private String tipoEntidad;

    protected ServicioBase(IRepository<T> repositorio, String tipoEntidad) {
        this.repositorio = repositorio;
        this.tipoEntidad = tipoEntidad;
    }

    // Guarda la entidad solo si su id no existe todavía.
    protected void guardarSiNoExiste(T entidad) throws IdDuplicadoException {
        if (repositorio.existePorId(entidad.getId())) {
            throw new IdDuplicadoException(tipoEntidad, entidad.getId());
        }
        repositorio.guardar(entidad);
    }

    // Lanza EntidadNoEncontradaException si el id no existe.
    public T buscarPorId(String id) throws EntidadNoEncontradaException {
        String clave = normalizar(id);
        Optional<T> encontrado = repositorio.buscarPorId(clave);
        if (!encontrado.isPresent()) {
            throw new EntidadNoEncontradaException(tipoEntidad, clave);
        }
        return encontrado.get();
    }

    public boolean existe(String id) {
        return repositorio.existePorId(normalizar(id));
    }

    public List<T> listar() {
        return repositorio.listarTodos();
    }

    private String normalizar(String id) {
        if (id == null) {
            return "";
        }
        return id.trim();
    }
}
