package Usuarios;

import java.time.LocalDate;

public class UsuarioNormal extends Usuario{

    public UsuarioNormal(int idUsuario, String emailUsuario, String contrasena, String rol, LocalDate fechaRegistro, String estadoCuenta, String nombreUsuario) {
        super(idUsuario, emailUsuario, contrasena, "Normal", fechaRegistro, estadoCuenta,nombreUsuario);
    }

    @Override
    protected String simpleHash(String contrasena) {
        return "HASH_" + contrasena;
    }
}
