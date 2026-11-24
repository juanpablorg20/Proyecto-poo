package edu.ucab.app;

import edu.ucab.app.console.Consola;
import edu.ucab.app.console.Lector;
import edu.ucab.app.console.MenuClientes;
import edu.ucab.app.console.MenuProductos;
import edu.ucab.app.exception.*;
import edu.ucab.app.model.Cliente;
import edu.ucab.app.model.Producto;
import edu.ucab.app.repository.InMemoryRepository;
import edu.ucab.app.service.ClienteService;
import edu.ucab.app.service.ProductoService;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/**
 * Pruebas funcionales de clientes y productos (sin librerías externas).
 * Ejecutar: ver README.md. Termina con código 1 si alguna prueba falla.
 */
public class PruebasFuncionales {

    interface Accion { void ejecutar() throws Exception; }

    private static int pasadas = 0;
    private static int falladas = 0;

    // ---------- Mini-framework ----------
    static void prueba(String nombre, Accion accion) {
        try {
            accion.ejecutar();
            pasadas++;
            System.out.println("  OK    " + nombre);
        } catch (Throwable t) {
            falladas++;
            System.out.println("  FALLA " + nombre + " -> " + t);
        }
    }

    static void afirma(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }

    static void igual(Object esperado, Object real, String mensaje) {
        if (!esperado.equals(real)) {
            throw new AssertionError(mensaje + " (esperado=" + esperado + ", real=" + real + ")");
        }
    }

    static <T extends Exception> void lanza(Class<T> tipo, Accion accion) throws Exception {
        try {
            accion.ejecutar();
        } catch (Exception e) {
            if (tipo.isInstance(e)) return;
            throw new AssertionError("Se esperaba " + tipo.getSimpleName() + " pero fue " + e);
        }
        throw new AssertionError("Se esperaba " + tipo.getSimpleName() + " pero no hubo excepción");
    }

    static ClienteService nuevoClienteService() {
        return new ClienteService(new InMemoryRepository<Cliente>());
    }

    static ProductoService nuevoProductoService() {
        return new ProductoService(new InMemoryRepository<Producto>());
    }

    static Consola armarConsola(ClienteService cs, ProductoService ps, String guion, PrintStream salida) {
        Lector lector = new Lector(new Scanner(guion), salida);
        return new Consola(new MenuClientes(cs, lector, salida),
                new MenuProductos(ps, lector, salida), lector, salida);
    }

    // ---------- Pruebas ----------
    public static void main(String[] args) {
        System.out.println("== FR-01 Clientes ==");
        pruebasClientes();
        System.out.println("== FR-02 Productos ==");
        pruebasProductos();
        System.out.println("== Consulta sin efectos secundarios ==");
        pruebasConsultas();
        System.out.println("== Consola (robustez NFR-09) ==");
        pruebasConsola();

        System.out.println("\nResultado: " + pasadas + " pasadas, " + falladas + " falladas");
        if (falladas > 0) System.exit(1);
    }

    static void pruebasClientes() {
        prueba("Registrar un cliente válido", () -> {
            ClienteService s = nuevoClienteService();
            s.registrar("C1", "Ana");
            Cliente c = s.buscarPorId("C1");
            igual("C1", c.getId(), "id");
            igual("Ana", c.getNombre(), "nombre");
            igual(1, s.listar().size(), "tamaño de la lista");
        });
        prueba("Id duplicado se rechaza con IdDuplicadoException", () -> {
            ClienteService s = nuevoClienteService();
            s.registrar("C1", "Ana");
            lanza(IdDuplicadoException.class, () -> s.registrar("C1", "Otra persona"));
            igual(1, s.listar().size(), "no debe agregarse el duplicado");
            igual("Ana", s.buscarPorId("C1").getNombre(), "el original no cambia");
        });
        prueba("Id duplicado aunque difiera en espacios (' C1 ')", () -> {
            ClienteService s = nuevoClienteService();
            s.registrar("C1", "Ana");
            lanza(IdDuplicadoException.class, () -> s.registrar(" C1 ", "Ana"));
        });
        prueba("Id null -> IllegalArgumentException", () -> {
            lanza(IllegalArgumentException.class, () -> nuevoClienteService().registrar(null, "Ana"));
        });
        prueba("Id vacío o en blanco -> IllegalArgumentException", () -> {
            ClienteService s = nuevoClienteService();
            lanza(IllegalArgumentException.class, () -> s.registrar("", "Ana"));
            lanza(IllegalArgumentException.class, () -> s.registrar("   ", "Ana"));
        });
        prueba("Nombre null/vacío -> IllegalArgumentException", () -> {
            ClienteService s = nuevoClienteService();
            lanza(IllegalArgumentException.class, () -> s.registrar("C1", null));
            lanza(IllegalArgumentException.class, () -> s.registrar("C1", "  "));
            igual(0, s.listar().size(), "un cliente inválido no entra al sistema");
        });
        prueba("Se recortan espacios de id y nombre", () -> {
            ClienteService s = nuevoClienteService();
            s.registrar("  C1  ", "  Ana  ");
            Cliente c = s.buscarPorId("C1");
            igual("C1", c.getId(), "id recortado");
            igual("Ana", c.getNombre(), "nombre recortado");
            afirma(s.existe("C1"), "debe encontrarse con id limpio");
        });
        prueba("Buscar cliente inexistente -> EntidadNoEncontradaException", () -> {
            lanza(EntidadNoEncontradaException.class, () -> nuevoClienteService().buscarPorId("X"));
        });
        prueba("Buscar con null no revienta con NullPointerException", () -> {
            lanza(EntidadNoEncontradaException.class, () -> nuevoClienteService().buscarPorId(null));
        });
        prueba("Los ids distinguen mayúsculas ('c1' != 'C1')", () -> {
            ClienteService s = nuevoClienteService();
            s.registrar("C1", "Ana");
            s.registrar("c1", "Beto");
            igual(2, s.listar().size(), "dos clientes distintos");
        });
        prueba("Dato inválido se reporta antes que el duplicado", () -> {
            ClienteService s = nuevoClienteService();
            s.registrar("C1", "Ana");
            lanza(IllegalArgumentException.class, () -> s.registrar("C1", ""));
        });
        prueba("Listar sin clientes devuelve lista vacía (no null)", () -> {
            afirma(nuevoClienteService().listar().isEmpty(), "debe estar vacía");
        });
        prueba("Listar conserva el orden de registro", () -> {
            ClienteService s = nuevoClienteService();
            s.registrar("C3", "Cleo");
            s.registrar("C1", "Ana");
            s.registrar("C2", "Beto");
            igual("C3", s.listar().get(0).getId(), "primero");
            igual("C2", s.listar().get(2).getId(), "último");
        });
    }

    static void pruebasProductos() {
        prueba("Registrar un producto válido", () -> {
            ProductoService s = nuevoProductoService();
            s.registrar("P1", "Lápiz", 2.5);
            Producto p = s.buscarPorId("P1");
            igual("P1", p.getId(), "id");
            igual("Lápiz", p.getDescripcion(), "descripción");
            igual(2.5, p.getPrecio(), "precio");
        });
        prueba("Precio 0 -> IllegalArgumentException", () -> {
            ProductoService s = nuevoProductoService();
            lanza(IllegalArgumentException.class, () -> s.registrar("P1", "Lápiz", 0));
            igual(0, s.listar().size(), "no debe registrarse");
        });
        prueba("Precio negativo -> IllegalArgumentException", () -> {
            lanza(IllegalArgumentException.class, () -> nuevoProductoService().registrar("P1", "Lápiz", -1));
        });
        prueba("Precio NaN o infinito -> IllegalArgumentException", () -> {
            ProductoService s = nuevoProductoService();
            lanza(IllegalArgumentException.class, () -> s.registrar("P1", "X", Double.NaN));
            lanza(IllegalArgumentException.class, () -> s.registrar("P2", "X", Double.POSITIVE_INFINITY));
            lanza(IllegalArgumentException.class, () -> s.registrar("P3", "X", Double.NEGATIVE_INFINITY));
        });
        prueba("Caso borde: precio mínimo 0.01 es válido", () -> {
            ProductoService s = nuevoProductoService();
            s.registrar("P1", "Chicle", 0.01);
            igual(0.01, s.buscarPorId("P1").getPrecio(), "precio");
        });
        prueba("Caso borde: -0.0 es inválido", () -> {
            lanza(IllegalArgumentException.class, () -> nuevoProductoService().registrar("P1", "X", -0.0));
        });
        prueba("Id duplicado se rechaza y no sobrescribe el original", () -> {
            ProductoService s = nuevoProductoService();
            s.registrar("P1", "Lápiz", 2.5);
            lanza(IdDuplicadoException.class, () -> s.registrar("P1", "Bolígrafo", 9));
            igual(1, s.listar().size(), "sin duplicados");
            igual("Lápiz", s.buscarPorId("P1").getDescripcion(), "el original se mantiene");
            igual(2.5, s.buscarPorId("P1").getPrecio(), "precio original");
        });
        prueba("Id y descripción null/vacíos -> IllegalArgumentException", () -> {
            ProductoService s = nuevoProductoService();
            lanza(IllegalArgumentException.class, () -> s.registrar(null, "X", 1));
            lanza(IllegalArgumentException.class, () -> s.registrar(" ", "X", 1));
            lanza(IllegalArgumentException.class, () -> s.registrar("P1", null, 1));
            lanza(IllegalArgumentException.class, () -> s.registrar("P1", "", 1));
        });
        prueba("Buscar producto inexistente -> EntidadNoEncontradaException", () -> {
            lanza(EntidadNoEncontradaException.class, () -> nuevoProductoService().buscarPorId("P9"));
        });
        prueba("Cliente y producto pueden compartir el mismo id (repositorios independientes)", () -> {
            ClienteService cs = nuevoClienteService();
            ProductoService ps = nuevoProductoService();
            cs.registrar("001", "Ana");
            ps.registrar("001", "Lápiz", 1);
            afirma(cs.existe("001") && ps.existe("001"), "ambos existen");
        });
        prueba("El mensaje de error nombra el campo inválido", () -> {
            try {
                nuevoProductoService().registrar("P1", "X", -5);
                throw new AssertionError("debió lanzar excepción");
            } catch (IllegalArgumentException e) {
                afirma(e.getMessage().contains("precio"), "el mensaje debe nombrar el precio");
            }
        });
    }

    static void pruebasConsultas() {
        prueba("listar() devuelve una copia: modificarla no altera el almacén", () -> {
            ClienteService s = nuevoClienteService();
            s.registrar("C1", "Ana");
            List<Cliente> lista = s.listar();
            lista.clear();
            igual(1, s.listar().size(), "el almacén no cambia");
        });
        prueba("Consultar (buscar/existe/listar) no altera el estado", () -> {
            ProductoService s = nuevoProductoService();
            s.registrar("P1", "Lápiz", 2.5);
            s.listar();
            s.existe("P1");
            s.existe("NOPE");
            s.buscarPorId("P1");
            igual(1, s.listar().size(), "sigue habiendo un producto");
        });
        prueba("Una lista tomada antes no cambia al registrar después", () -> {
            ClienteService s = nuevoClienteService();
            s.registrar("C1", "Ana");
            List<Cliente> antes = s.listar();
            s.registrar("C2", "Beto");
            igual(1, antes.size(), "la foto anterior queda intacta");
            igual(2, s.listar().size(), "la lista nueva refleja lo vigente");
        });
    }

    static void pruebasConsola() {
        prueba("La consola sobrevive a errores y entradas inválidas", () -> {
            ClienteService cs = nuevoClienteService();
            ProductoService ps = nuevoProductoService();
            String guion = String.join("\n",
                    "1", "1", "C-1", "Ana",        // registrar cliente OK
                    "1", "C-1", "Otra",            // duplicado -> error, sigue vivo
                    "1", "", "Sin id",             // id vacío -> error
                    "3", "NOPE",                   // buscar inexistente
                    "xyz",                         // opción no numérica
                    "4",                           // volver
                    "2", "1", "P-1", "Lápiz", "abc",   // precio no numérico
                    "1", "P-1", "Lápiz", "0",          // precio 0
                    "1", "P-1", "Lápiz", "2,50",       // precio con coma -> OK
                    "4", "3") + "\n";
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            armarConsola(cs, ps, guion, new PrintStream(buffer, true, "UTF-8")).ejecutar();
            String texto = buffer.toString("UTF-8");

            igual(1, cs.listar().size(), "solo el primer cliente quedó registrado");
            igual(2.5, ps.buscarPorId("P-1").getPrecio(), "precio con coma interpretado");
            afirma(texto.contains("ya existe"), "debe informar el duplicado");
            afirma(texto.contains("precio debe ser un número"), "debe informar precio no numérico");
            afirma(texto.contains("mayor que cero"), "debe informar precio inválido");
            afirma(texto.contains("Hasta luego"), "debe terminar limpiamente");
        });
        prueba("La consola termina sin excepción si se acaba la entrada", () -> {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            armarConsola(nuevoClienteService(), nuevoProductoService(), "",
                    new PrintStream(buffer, true, "UTF-8")).ejecutar();
            afirma(buffer.toString("UTF-8").contains("Hasta luego"), "salida limpia");
        });
    }
}
