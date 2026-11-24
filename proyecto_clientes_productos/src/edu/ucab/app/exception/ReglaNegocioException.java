package edu.ucab.app.exception;

// Excepción base de las situaciones de negocio (verificada: extends Exception).
// IdDuplicadoException y EntidadNoEncontradaException heredan de ella.
public class ReglaNegocioException extends Exception {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
