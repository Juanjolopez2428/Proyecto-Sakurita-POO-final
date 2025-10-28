package Ventas;

import Usuarios.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cliente extends Usuario{

    private String dirreccionEnvio;
    private int telefono;
    private List<MetodoPago> metodoPagos;;



    public Cliente(int idUsuario, String emailUsuario, String contrasena, String rol, LocalDate fechaRegistro, String estadoCuenta, String dirreccionEnvio, int telefono, String nombreUsuario) {
        super(idUsuario, emailUsuario, contrasena, rol, fechaRegistro, estadoCuenta,nombreUsuario);
        this.telefono = telefono;
        this.dirreccionEnvio = dirreccionEnvio;
        this.metodoPagos = new ArrayList<>();
    }

    @Override
    protected String simpleHash(String contrasena) {
        return "";
    }
}
