package Ventas;

import java.util.ArrayList;
import java.util.List;

public class Carrito {
    private int idCarrito;
    private String fechaCreacionCarrito;
    private List<Producto> productos;
    private double total;

    public Carrito(String fechaCreacionCarrito, int idCarrito) {
        this.fechaCreacionCarrito = fechaCreacionCarrito;
        this.idCarrito = idCarrito;
        this.productos = new ArrayList<>();
        this.total = 0.0;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
        total += producto.getPrecio();
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public double getTotal() {
        return total;
    }
}
