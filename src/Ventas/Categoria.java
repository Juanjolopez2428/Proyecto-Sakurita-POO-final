package Ventas;

public class Categoria {
    private int idCategoria;
    private String nombreCategoria;
    private String descripcionCategoria;


    public Categoria(String descripcionCategoria, int idCategoria, String nombreCategoria) {
        this.descripcionCategoria = descripcionCategoria;
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
    }
}

