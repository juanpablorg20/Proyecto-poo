package edu.ucab.app.model;

// Producto que puede incluirse en un pedido. Atributos privados y datos siempre válidos.
public class Producto implements Identificable {

    private String id;
    private String descripcion;
    private double precio;

    public Producto(String id, String descripcion, double precio) {
        this.id = Validaciones.textoNoVacio("id", id);
        this.descripcion = Validaciones.textoNoVacio("descripcion", descripcion);
        this.precio = Validaciones.mayorQueCero("precio", precio);
    }

    @Override
    public String getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    @Override
    public boolean equals(Object otro) {
        if (otro instanceof Producto) {
            Producto otroProducto = (Producto) otro;
            return this.id.equals(otroProducto.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Producto[id=" + id + ", descripcion=" + descripcion + ", precio=" + precio + "]";
    }
}
