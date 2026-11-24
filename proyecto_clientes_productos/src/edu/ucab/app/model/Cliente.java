package edu.ucab.app.model;

// Cliente de la empresa. Atributos privados y datos siempre válidos.
public class Cliente implements Identificable {

    private String id;
    private String nombre;

    public Cliente(String id, String nombre) {
        this.id = Validaciones.textoNoVacio("id", id);
        this.nombre = Validaciones.textoNoVacio("nombre", nombre);
    }

    @Override
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object otro) {
        if (otro instanceof Cliente) {
            Cliente otroCliente = (Cliente) otro;
            return this.id.equals(otroCliente.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Cliente[id=" + id + ", nombre=" + nombre + "]";
    }
}
