package edu.ucab.app.exception;

// Se intentó registrar algo con un id que ya existe.
public class IdDuplicadoException extends ReglaNegocioException {

    public IdDuplicadoException(String tipoEntidad, String id) {
        super(tipoEntidad + " con identificador '" + id + "' ya existe.");
    }
}
