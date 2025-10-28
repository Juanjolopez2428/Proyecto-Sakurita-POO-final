package Produccion;

import Usuarios.Usuario;
import Ventas.Producto;
import Excepciones.ProductoInvalidoException; //
import java.time.LocalDate;

public class DesarrolladorProducto extends Usuario {

    private String especialidad;

    public DesarrolladorProducto(int idUsuario, String emailUsuario, String contrasena,
                                 String rol, LocalDate fechaRegistro, String estadoCuenta,
                                 String nombreUsuario, String especialidad) {
        super(idUsuario, emailUsuario, contrasena, rol, fechaRegistro, estadoCuenta, nombreUsuario);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /**
     * Crea un nuevo producto validando los datos.
     * Si los datos son inválidos, captura la excepción
     */
    public Producto crearProducto(int idProducto, String nombre, String descripcion,
                                  double precio, int stock, String fechaLanzamiento,
                                  String categoria) {
        System.out.println(getNombreUsuario() + " (" + this.especialidad +
                ") está creando el producto: " + nombre);

        try {
            // Constructor de Producto con validación
            Producto nuevoProducto = new Producto(
                    idProducto, nombre, descripcion, precio, stock, fechaLanzamiento, categoria
            );
            System.out.println(" Producto creado exitosamente: " + nuevoProducto.getNombre());
            return nuevoProducto;
        } catch (ProductoInvalidoException e) {
            System.out.println(" Error al crear producto: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println(" Error inesperado al crear producto: " + e.getMessage());
            return null;
        }
    }

    @Override
    protected String simpleHash(String contrasena) {
        return "Hash_" + contrasena;
    }
}
