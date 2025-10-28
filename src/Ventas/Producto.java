package Ventas;

import Excepciones.ProductoInvalidoException;

public class Producto {
    private int idProducto;
    private String id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private String fechaLanzamiento;
    private String categoria;
    private Boolean estadoProducto;

    // --- Constructor validado ---
    public Producto(int idProducto, String nombre, String descripcion, double precio, int stock,
                    String fechaLanzamiento, String categoria) throws ProductoInvalidoException {

        if (nombre == null || nombre.trim().isEmpty())
            throw new ProductoInvalidoException("El nombre del producto no puede estar vacío.");
        if (precio <= 0)
            throw new ProductoInvalidoException("El precio del producto debe ser mayor que 0.");
        if (stock < 0)
            throw new ProductoInvalidoException("El stock no puede ser negativo.");

        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.fechaLanzamiento = fechaLanzamiento;
        this.categoria = categoria;
        this.estadoProducto = false;
    }

    // Otros constructores opcionales (sin validación estricta)
    public Producto(int idProducto, String nombre, double precio, int stock, String categoria) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    public Producto(int idProducto, String nombre, String descripcion, double precio) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.estadoProducto = false;
    }

    public Producto(String id, String nombre, double precio, String descripcion) {}

    // --- Getters y Setters ---
    public int getIdProducto() { return idProducto; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public Boolean getEstadoProducto() { return estadoProducto; }
    public String getId() { return id; }
    public int getStock() { return stock; }
    public String getCategoria() { return categoria; }

    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setEstadoProducto(Boolean estadoProducto) { this.estadoProducto = estadoProducto; }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Producto [Id: " + idProducto + ", Nombre: " + nombre + ", Precio: " + precio +
                ", Publicado: " + estadoProducto + "]";
    }


}
