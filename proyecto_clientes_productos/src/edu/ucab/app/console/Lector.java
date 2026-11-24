package edu.ucab.app.console;

import java.io.PrintStream;
import java.util.Scanner;

// Se encarga solo de leer datos del usuario (una responsabilidad).
public class Lector {

    public static final int FIN_ENTRADA = -1;
    public static final int OPCION_INVALIDA = 0;

    private final Scanner entrada;
    private final PrintStream salida;

    public Lector(Scanner entrada, PrintStream salida) {
        this.entrada = entrada;
        this.salida = salida;
    }

    public String pedirTexto(String mensaje) {
        salida.print(mensaje);
        if (entrada.hasNextLine()) {
            return entrada.nextLine();
        }
        return "";
    }

    // Devuelve la opción, OPCION_INVALIDA si no es número o FIN_ENTRADA si no hay más entrada.
    public int leerOpcion() {
        salida.print("Opción: ");
        if (!entrada.hasNextLine()) {
            return FIN_ENTRADA;
        }
        try {
            return Integer.parseInt(entrada.nextLine().trim());
        } catch (NumberFormatException e) {
            return OPCION_INVALIDA;
        }
    }
}
