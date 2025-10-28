package Excepciones;

public class ProductoInvalidoException extends Exception {
    public ProductoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
