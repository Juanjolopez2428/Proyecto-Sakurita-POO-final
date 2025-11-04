package Ventas;

import Usuarios.Usuario;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {

    private String direccionEnvio;
    private int telefono;
    private List<MetodoPago> metodoPagos;
    private Carrito carrito;

    public Cliente(int idUsuario, String emailUsuario, String contrasena, String rol,
                   LocalDate fechaRegistro, String estadoCuenta,
                   String direccionEnvio, int telefono, String nombreUsuario) {
        super(idUsuario, emailUsuario, contrasena, rol, fechaRegistro, estadoCuenta, nombreUsuario);
        this.telefono = telefono;
        this.direccionEnvio = direccionEnvio;
        this.metodoPagos = new ArrayList<>();
        this.carrito = new Carrito(LocalDate.now().toString(), idUsuario); // Un carrito por cliente
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void agregarProductoAlCarrito(Producto p) {
        carrito.agregarProducto(p);
    }

    @Override
    protected String simpleHash(String contrasena) {
        return contrasena;
    }
}
