package Ventas;

import java.util.ArrayList;
import java.util.List;

public class Categoria {
    private int idCategoria;
    private String nombreCategoria;
    private String descripcionCategoria;
    private List<Producto> productos;

    public Categoria(String descripcionCategoria, int idCategoria, String nombreCategoria) {
        this.descripcionCategoria = descripcionCategoria;
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.productos = new ArrayList<>();
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescripcionCategoria() {
        return descripcionCategoria;
    }

    public void setDescripcionCategoria(String descripcionCategoria) {
        this.descripcionCategoria = descripcionCategoria;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }
}
