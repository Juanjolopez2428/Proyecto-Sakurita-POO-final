package Usuarios;
import Ventas.Producto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdministradorContenido extends Usuario implements MiembroConsejo {

    private boolean permisosEdicion;
    private List<Producto> productos;

    public AdministradorContenido(int idUsuario, String emailUsuario, String contrasena,
                                  String rol, LocalDate fechaRegistro, String estadoCuenta,
                                  String nombreUsuario) {
        super(idUsuario, emailUsuario, contrasena, "AdministradorContenido", fechaRegistro, estadoCuenta, nombreUsuario);
        this.permisosEdicion = true;
        this.productos = new ArrayList<>();
    }

    @Override
    protected String simpleHash(String contrasena) {
        return Integer.toHexString(contrasena.hashCode());
    }

    @Override
    public String getRol() {
        return "Administrador de Contenido";
    }

    @Override
    public void ejecutarAccionSecreta() {
        System.out.println(getNombreUsuario() + " ha cifrado datos en el registro.");
    }

    // --- CRUD de Productos ---
    public void crearNuevoProducto(String id, String nombre, double precio, String descripcion) {
        if (!permisosEdicion) {
            System.out.println(" No tienes permiso para crear productos.");
            return;
        }
        if (obtenerProductoPorId(id).isPresent()) {
            System.out.println("️ Ya existe un producto con ese ID: " + id);
            return;
        }
        Producto nuevo = new Producto(id, nombre, precio, descripcion);
        productos.add(nuevo);
        System.out.println(" Producto creado por " + getNombreUsuario() + ": " + nombre);
    }

    public Optional<Producto> obtenerProductoPorId(String id) {
        return productos.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public void publicarProducto(String idProducto) {
        if (!permisosEdicion) {
            System.out.println(" No tienes permiso para publicar productos.");
            return;
        }
        obtenerProductoPorId(idProducto).ifPresentOrElse(p -> {
            p.setEstadoProducto(true);
            System.out.println(" Producto publicado: " + p.getNombre());
        }, () -> System.out.println(" Producto no encontrado."));
    }

    public void despublicarProducto(String idProducto) {
        obtenerProductoPorId(idProducto).ifPresentOrElse(p -> {
            p.setEstadoProducto(false);
            System.out.println(" Producto despublicado: " + p.getNombre());
        }, () -> System.out.println(" Producto no encontrado."));
    }

    public List<Producto> verTodosLosProductos() {
        return new ArrayList<>(productos);
    }

    public void setPermisosEdicion(boolean permisosEdicion) {
        this.permisosEdicion = permisosEdicion;
    }

    public boolean isPermisosEdicion() {
        return permisosEdicion;
    }
}
