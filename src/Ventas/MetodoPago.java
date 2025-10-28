package Ventas;

public class MetodoPago {
 private int idMetodoPago;
 private String tipoMetodoPago;
 private String titular;
 protected int NumeroMetodoPago;
    private String tipo;


    public MetodoPago(int idMetodoPago, int numeroMetodoPago, String tipoMetodoPago, String titular) {
        this.idMetodoPago = idMetodoPago;
        NumeroMetodoPago = numeroMetodoPago;
        this.tipoMetodoPago = tipoMetodoPago;
        this.titular = titular;
    }

    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public String getTipoMetodoPago() {
        return tipoMetodoPago;
    }

    public void setTipoMetodoPago(String tipoMetodoPago) {
        this.tipoMetodoPago = tipoMetodoPago;
    }
        public MetodoPago(String t) { this.tipo = t; }
        public String getTipo() { return tipo; }
}

