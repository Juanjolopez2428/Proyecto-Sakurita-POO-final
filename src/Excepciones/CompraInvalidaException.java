package Excepciones;

public class CompraInvalidaException extends Exception {
    public CompraInvalidaException(String mensaje) {
        super(mensaje);
    }
}
