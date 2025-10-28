package Usuarios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// --- Imports Ventas ---
import Ventas.Carrito;
import Ventas.Categoria;
import Ventas.Cliente;
import Ventas.Compra;
import Ventas.LineaCarrito;
import Ventas.LineaCompra;
import Ventas.Producto;
import Ventas.MetodoPago;

// --- Imports Produccion ---
import Produccion.DesarrolladorProducto;
import Produccion.Fabrica;

// --- Imports Operaciones Ocultas ---
import OperacionesOcultas.ConsejoSombrio;
import OperacionesOcultas.RegistroEsclavos;
import OperacionesOcultas.TrabajadorEsclavizado;

public class Duena extends Usuario {
    private String claveMaestra;
    private LocalDate fechaCoronacion;

    // --- Listas de control ---
    private List<Cliente> clientes;
    private List<AdministradorContenido> administradoresContenido;
    private List<AdministradorUsuarios> administradoresUsuario;
    private List<UsuarioNormal> usuariosNormales;
    private List<MiembroConsejo> miembrosConsejo;

    private List<Producto> productos;
    private List<Categoria> categorias;
    private List<Carrito> carritos;
    private List<LineaCarrito> lineasCarrito;
    private List<Compra> compras;
    private List<LineaCompra> lineasCompra;
    private List<MetodoPago> metodosPago;

    private List<DesarrolladorProducto> desarrolladores;
    private List<Fabrica> fabricas;

    private RegistroEsclavos registroEsclavos;
    private ConsejoSombrio consejoSombrio;

    // --- Constructor ---
    public Duena(int idUsuario, String emailUsuario, String contrasena,
                 String nombreUsuario, String claveMaestra) {
        super(idUsuario, emailUsuario, contrasena, "DUENA",
                LocalDate.now(), "ACTIVA", nombreUsuario);
        this.claveMaestra = simpleHash(claveMaestra);
        this.fechaCoronacion = LocalDate.now();

        // Inicialización de colecciones
        this.clientes = new ArrayList<>();
        this.administradoresContenido = new ArrayList<>();
        this.administradoresUsuario = new ArrayList<>();
        this.usuariosNormales = new ArrayList<>();
        this.miembrosConsejo = new ArrayList<>();

        this.productos = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.carritos = new ArrayList<>();
        this.lineasCarrito = new ArrayList<>();
        this.compras = new ArrayList<>();
        this.lineasCompra = new ArrayList<>();
        this.metodosPago = new ArrayList<>();

        this.desarrolladores = new ArrayList<>();
        this.fabricas = new ArrayList<>();

        this.registroEsclavos = new RegistroEsclavos();
        this.consejoSombrio = new ConsejoSombrio();
    }

    // --- Métodos de gestión Usuarios ---
    public void registrarCliente(Cliente c) { clientes.add(c); }
    public void registrarAdministradorContenido(AdministradorContenido a) { administradoresContenido.add(a); }
    public void registrarAdminUsuario(AdministradorUsuarios a) { administradoresUsuario.add(a); }
    public void registrarUsuarioNormal(UsuarioNormal u) { usuariosNormales.add(u); }
    public void registrarMiembroConsejo(MiembroConsejo m) { miembrosConsejo.add(m); }

    // --- Métodos de gestión Ventas ---
    public void agregarProducto(Producto p) { productos.add(p); }
    public void agregarCategoria(Categoria c) { categorias.add(c); }
    public void registrarCarrito(Carrito c) { carritos.add(c); }
    public void registrarLineaCarrito(LineaCarrito l) { lineasCarrito.add(l); }
    public void registrarCompra(Compra compra) { compras.add(compra); }
    public void registrarLineaCompra(LineaCompra l) { lineasCompra.add(l); }
    public void registrarMetodoPago(MetodoPago m) { metodosPago.add(m); }

    // --- Métodos de gestión Producción ---
    public void registrarDesarrollador(DesarrolladorProducto d) { desarrolladores.add(d); }
    public void agregarFabrica(Fabrica f) { fabricas.add(f); }
    public void asignarTrabajadorEsclavo(Fabrica f, TrabajadorEsclavizado t) {
        f.asignarTrabajador(t);
        registroEsclavos.registrarTrabajador(t); //
    }

    // --- Métodos especiales ---
    public void convocarConsejo() {
        System.out.println("Consejo Sombrío convocado por la Dueña.");
    }

    // --- Getters ---
    public List<Cliente> getClientes() { return clientes; }
    public List<AdministradorContenido> getAdministradoresContenido() { return administradoresContenido; }
    public List<AdministradorUsuarios> getAdministradoresUsuario() { return administradoresUsuario; }
    public List<UsuarioNormal> getUsuariosNormales() { return usuariosNormales; }
    public List<MiembroConsejo> getMiembrosConsejo() { return miembrosConsejo; }

    public List<Producto> getProductos() { return productos; }
    public List<Categoria> getCategorias() { return categorias; }
    public List<Carrito> getCarritos() { return carritos; }
    public List<LineaCarrito> getLineasCarrito() { return lineasCarrito; }
    public List<Compra> getCompras() { return compras; }
    public List<LineaCompra> getLineasCompra() { return lineasCompra; }
    public List<MetodoPago> getMetodosPago() { return metodosPago; }

    public List<DesarrolladorProducto> getDesarrolladores() { return desarrolladores; }
    public List<Fabrica> getFabricas() { return fabricas; }

    public RegistroEsclavos getRegistroEsclavos() { return registroEsclavos; }
    public ConsejoSombrio getConsejoSombrio() { return consejoSombrio; }

    @Override
    protected String simpleHash(String contrasena) {
        return Integer.toHexString(contrasena.hashCode());
    }
}
