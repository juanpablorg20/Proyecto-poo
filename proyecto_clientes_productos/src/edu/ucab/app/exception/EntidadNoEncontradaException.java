package edu.ucab.app.exception;

// Se buscó un cliente o producto que no está registrado.
public class EntidadNoEncontradaException extends ReglaNegocioException {

    public EntidadNoEncontradaException(String tipoEntidad, String id) {
        super(tipoEntidad + " con identificador '" + id + "' no existe.");
    }
}
