package edu.ucab.app.model;

// Validaciones reutilizables por Cliente y Producto (DRY).
// Un dato inválido es una simple validación: lanza IllegalArgumentException.
public class Validaciones {

    public static String textoNoVacio(String campo, String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("Dato inválido en '" + campo + "': no puede ser nulo ni vacío");
        }
        return valor.trim();
    }

    public static double mayorQueCero(String campo, double valor) {
        if (valor <= 0 || Double.isNaN(valor) || Double.isInfinite(valor)) {
            throw new IllegalArgumentException("Dato inválido en '" + campo + "': debe ser un número mayor que cero");
        }
        return valor;
    }
}
