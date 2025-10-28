package Usuarios;
import java.time.LocalDate;
import java.util.Objects;

public abstract class Usuario {
    protected int idUsuario;
    protected  String emailUsuario;
    protected String contrasena;
    protected String rol;
    protected LocalDate fechaRegistro;
    protected String estadoCuenta;
    protected String nombreUsuario;


    public Usuario(int idUsuario, String emailUsuario, String contrasena, String rol, LocalDate fechaRegistro, String estadoCuenta, String nombreUsuario) {
        this.idUsuario = idUsuario;
        this.emailUsuario = emailUsuario;
        this.contrasena = simpleHash(contrasena);
        this.rol = rol;
        this.fechaRegistro = fechaRegistro;
        this.estadoCuenta = estadoCuenta;
        this.nombreUsuario = nombreUsuario;;
    }

    protected abstract String simpleHash(String contrasena);


    @Override
    public boolean equals(Object o){
        if( this == o) return true;
        if(o == null|| getClass() !=o.getClass()) return false; //Comparar usuarios por Id
        Usuario usuario = (Usuario) o;
        return idUsuario == usuario.idUsuario;
    }

    @Override
    public int hashCode(){
        int hash = Objects.hash(idUsuario);
        return hash;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEstadoCuenta() {
        return estadoCuenta;
    }

    public void setEstadoCuenta(String estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }
}
