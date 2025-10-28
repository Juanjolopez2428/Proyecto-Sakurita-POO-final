package Usuarios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdministradorUsuarios extends Usuario implements MiembroConsejo {

    private List<Usuario> listaUsuarios;

    public AdministradorUsuarios(int idUsuario, String emailUsuario, String contrasena,
                                 String rol, LocalDate fechaRegistro, String estadoCuenta,
                                 String nombreUsuario) {
        super(idUsuario, emailUsuario, contrasena, "AdministradorUsuarios", fechaRegistro, estadoCuenta, nombreUsuario);
        this.listaUsuarios = new ArrayList<>();
    }

    public void crearUsuario(int id, String nombre, String email, String contrasena,
                             String rol, LocalDate fechaRegistro) {
        Usuario nuevoUsuario = new UsuarioNormal(id, email, contrasena, rol, fechaRegistro, "ACTIVA", nombre);
        listaUsuarios.add(nuevoUsuario);
        System.out.println(" Usuario " + nombre + " creado correctamente.");
    }

    private Usuario buscarUsuarioPorId(int idUsuario) {
        return listaUsuarios.stream()
                .filter(u -> u.getIdUsuario() == idUsuario)
                .findFirst()
                .orElse(null);
    }

    public void activarUsuario(int idUsuario) {
        Usuario u = buscarUsuarioPorId(idUsuario);
        if (u != null) {
            u.setEstadoCuenta("ACTIVA");
            System.out.println(" Usuario " + u.getNombreUsuario() + " activado.");
        } else {
            System.out.println("️ Usuario no encontrado.");
        }
    }

    public void desactivarUsuario(int idUsuario) {
        Usuario u = buscarUsuarioPorId(idUsuario);
        if (u != null) {
            u.setEstadoCuenta("INACTIVA");
            System.out.println(" Usuario " + u.getNombreUsuario() + " desactivado.");
        } else {
            System.out.println(" Usuario no encontrado.");
        }
    }

    public void listarUsuarios() {
        if (listaUsuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        listaUsuarios.forEach(u -> System.out.println(u.getIdUsuario() + " | " + u.getNombreUsuario() + " | " + u.getRol() + " | " + u.getEstadoCuenta()));
    }

    @Override
    protected String simpleHash(String contrasena) {
        return Integer.toHexString(contrasena.hashCode());
    }

    @Override
    public String getRol() {
        return "Administrador de Usuarios";
    }

    @Override
    public void ejecutarAccionSecreta() {
        System.out.println(getNombreUsuario() + " ha auditado la base de datos.");
    }
}
