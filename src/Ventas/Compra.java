package Ventas;

import Excepciones.CompraInvalidaException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Compra {
    private int idVenta;
    private LocalDate fechaVenta;
    private double totalVenta;
    private String estadoVenta;

    private Cliente cliente;
    private MetodoPago metodoPago;
    private List<LineaCompra> lineasCompra;

    public Compra(int idVenta, Cliente cliente, MetodoPago mp) {
        this.idVenta = idVenta;
        this.cliente = cliente;
        this.metodoPago = mp;
        this.fechaVenta = LocalDate.now();
        this.estadoVenta = "PENDIENTE";
        this.totalVenta = 0.0;
        this.lineasCompra = new ArrayList<>();
    }

    // Validación al agregar líneas
    public void agregarLinea(LineaCompra linea) throws CompraInvalidaException {
        if (linea == null)
            throw new CompraInvalidaException("No se puede agregar una línea de compra nula.");
        if (linea.getCantidad() <= 0)
            throw new CompraInvalidaException("La cantidad debe ser mayor que cero.");

        this.lineasCompra.add(linea);
        this.totalVenta += linea.getSubtotal();
    }

    // Validación al finalizar compra
    public void finalizarCompra() throws CompraInvalidaException {
        if (lineasCompra.isEmpty())
            throw new CompraInvalidaException("No se puede finalizar una compra sin productos.");
        this.estadoVenta = "FINALIZADA";
        System.out.println("Compra " + idVenta + " Finalizada. Total: $" + totalVenta);
    }

    // --- GETTERS ---
    public int getIdVenta() { return idVenta; }
    public double getTotalVenta() { return totalVenta; }
    public String getEstadoVenta() { return estadoVenta; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Compra #").append(idVenta).append(" ---\n");
        sb.append("Cliente: ").append(cliente.getNombreUsuario()).append("\n");
        sb.append("Fecha: ").append(fechaVenta).append("\n");
        sb.append("Pago: ").append(metodoPago.getTipo()).append("\n");
        sb.append("Total: $").append(String.format("%.2f", totalVenta)).append("\n");
        sb.append("Productos:\n");
        lineasCompra.forEach(lc -> sb.append(lc.toString()).append("\n"));
        return sb.toString();
    }
}
