
package Ventas;
public class LineaCompra {
    private Producto producto;
    private int cantidad;
    private double subtotal;

    public LineaCompra(Producto p, int c) {
        this.producto = p;
        this.cantidad = c;
        this.subtotal = p.getPrecio() * c;
    }
    public double getSubtotal() { return subtotal; }
    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    @Override public String toString() {
        return "  -> " + producto.getNombre() + " (" + cantidad + " unid.) - $" + subtotal;
    }
}


