import Persistencia.Persistencia;
import Persistencia.SistemaGlowUp;
import Usuarios.*;
import Ventas.*;
import Excepciones.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;


public class Main {
    private static final String RUTA_JSON = "sistema_glowup.json";
    private static final Scanner scanner = new Scanner(System.in);

    private static SistemaGlowUp sistema;
    private static Duena duena;                 // referencia rápida
    private static Usuario usuarioActual = null;

    // IDs auto calculados
    private static int nextProductoId = 1001;
    private static int nextUsuarioId = 1;
    private static int nextCompraId = 5001;
    private static int nextClienteId = 200;

    public static void main(String[] args) {
        // Cargar sistema
        sistema = Persistencia.cargar(RUTA_JSON, SistemaGlowUp.class);
        if (sistema == null) {
            sistema = new SistemaGlowUp(); // crea dueña por defecto
            System.out.println("(Inicialización) Sistema nuevo creado.");
        } else {
            System.out.println(" Datos cargados desde persistencia.");
        }

        duena = sistema.getDuena();
        recalcularIdsDesdeDatos();

        ejecutarMenuPrincipal();


        Persistencia.guardar(RUTA_JSON, sistema);
        System.out.println(" Sistema guardado. Adiós.");
    }

    // -
    private static void recalcularIdsDesdeDatos() {
        // productos
        Optional<Integer> maxProd = duena.getProductos().stream()
                .map(Producto::getIdProducto)
                .max(Comparator.naturalOrder());
        if (maxProd.isPresent()) nextProductoId = maxProd.get() + 1;

        // usuarios normales
        Optional<Integer> maxUser = duena.getUsuariosNormales().stream()
                .map(Usuario::getIdUsuario)
                .max(Comparator.naturalOrder());
        if (maxUser.isPresent()) nextUsuarioId = maxUser.get() + 1;

        // compras
        Optional<Integer> maxCompra = duena.getCompras().stream()
                .map(Compra::getIdVenta)
                .max(Comparator.naturalOrder());
        if (maxCompra.isPresent()) nextCompraId = maxCompra.get() + 1;

        // clientes (si hay clientes externos)
        Optional<Integer> maxCliente = duena.getClientes().stream()
                .map(Cliente::getIdUsuario)
                .max(Comparator.naturalOrder());
        if (maxCliente.isPresent()) nextClienteId = maxCliente.get() + 1;
    }

    // ==========================
    // Menú principal (Login / Registro)
    // ==========================
    private static void ejecutarMenuPrincipal() {
        int opcion;
        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Log-in");
            System.out.println("2. Registrar Usuario Normal");
            System.out.println("0. Salir");
            System.out.print("Seleccione: ");
            opcion = leerEntero();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    logInUsuario();
                    if (usuarioActual != null) ejecutarMenuPorRol();
                    break;
                case 2:
                    registrarUsuarioNormal();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    // ==========================
    // Registro de usuario normal
    // ==========================
    private static void registrarUsuarioNormal() {
        try {
            System.out.println("\n--- Registro de Usuario Normal ---");
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Contraseña: ");
            String contrasena = scanner.nextLine();

            // validaciones básicas
            if (nombre.isEmpty() || email.isEmpty() || contrasena.length() < 4) {
                throw new UsuarioInvalidoException("Nombre/email vacíos o contraseña < 4 caracteres.");
            }

            boolean existe = duena.getUsuariosNormales().stream()
                    .anyMatch(u -> u.getEmailUsuario().equalsIgnoreCase(email));
            if (existe) {
                throw new UsuarioInvalidoException("El email ya está registrado.");
            }

            UsuarioNormal nuevo = new UsuarioNormal(
                    nextUsuarioId++,
                    email,
                    contrasena,
                    "Normal",
                    LocalDate.now(),
                    "ACTIVA",
                    nombre
            );

            duena.registrarUsuarioNormal(nuevo);
            Persistencia.guardar(RUTA_JSON, sistema);
            System.out.println(" Usuario registrado (ID=" + nuevo.getIdUsuario() + ").");
        } catch (UsuarioInvalidoException e) {
            System.out.println( e.getMessage());
        }
    }

    // =====
    // Login
    // =====
    private static void logInUsuario() {
        System.out.println("\n--- Log-in ---");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();


        if (duena.getEmailUsuario().equalsIgnoreCase(email)) {
            String hashIngresado = Integer.toHexString(contrasena.hashCode());
            if (hashIngresado.equals(duena.getContrasena())) {
                usuarioActual = duena;
                System.out.println(" Log-in exitoso como DUEÑA: " + duena.getNombreUsuario());
                return;
            } else {
                System.out.println(" Contraseña incorrecta para la Dueña.");
                return;
            }
        }

        // 2) Intentar como AdministradorContenido
        Optional<AdministradorContenido> adminContenidoOpt = duena.getAdministradoresContenido().stream()
                .filter(a -> a.getEmailUsuario().equalsIgnoreCase(email))
                .findFirst();
        if (adminContenidoOpt.isPresent()) {
            // adminContenido usa simpleHash (implementación en tu clase): comparamos
            String hash = Integer.toHexString(contrasena.hashCode());
            if (hash.equals(adminContenidoOpt.get().getContrasena())) {
                usuarioActual = adminContenidoOpt.get();
                System.out.println(" Log-in exitoso como Administrador de Contenido: " + usuarioActual.getNombreUsuario());
                return;
            } else {
                System.out.println(" Contraseña incorrecta para AdministradorContenido.");
                return;
            }
        }

        // 3) Intentar como AdministradorUsuarios
        Optional<AdministradorUsuarios> adminUsuariosOpt = duena.getAdministradoresUsuario().stream()
                .filter(a -> a.getEmailUsuario().equalsIgnoreCase(email))
                .findFirst();
        if (adminUsuariosOpt.isPresent()) {
            String hash = Integer.toHexString(contrasena.hashCode());
            if (hash.equals(adminUsuariosOpt.get().getContrasena())) {
                usuarioActual = adminUsuariosOpt.get();
                System.out.println(" Log-in exitoso como Administrador de Usuarios: " + usuarioActual.getNombreUsuario());
                return;
            } else {
                System.out.println(" Contraseña incorrecta para AdministradorUsuarios.");
                return;
            }
        }

        // 4) Intentar como UsuarioNormal
        String hashNormal = "HASH_" + contrasena;
        usuarioActual = duena.getUsuariosNormales().stream()
                .filter(u -> u.getEmailUsuario().equalsIgnoreCase(email) && u.getContrasena().equals(hashNormal))
                .findFirst().orElse(null);

        if (usuarioActual != null) {
            System.out.println(" Log-in exitoso como Usuario Normal: " + usuarioActual.getNombreUsuario());
        } else {
            System.out.println(" Email o contraseña incorrectos.");
        }
    }


    private static void ejecutarMenuPorRol() {
        if (usuarioActual instanceof Duena) {
            menuDuena();
        } else if (usuarioActual instanceof AdministradorContenido) {
            menuAdministradorContenido();
        } else if (usuarioActual instanceof AdministradorUsuarios) {
            menuAdministradorUsuarios();
        } else {
            menuUsuarioNormal();
        }
    }


    private static void menuDuena() {
        int op;
        do {
            System.out.println("\n--- Menu DUEÑA (" + usuarioActual.getNombreUsuario() + ") ---");
            System.out.println("1. Listar productos");
            System.out.println("2. Alta de producto");
            System.out.println("3. Registrar administrador contenido");
            System.out.println("4. Registrar administrador usuarios");
            System.out.println("5. Listar usuarios normales");
            System.out.println("6. Listar compras");
            System.out.println("0. Cerrar sesión");
            System.out.print("Seleccione: ");
            op = leerEntero();
            scanner.nextLine();

            switch (op) {
                case 1 -> listarProductosAdminView();
                case 2 -> altaProductoPorDuena();
                case 3 -> registrarAdminContenido();
                case 4 -> registrarAdminUsuarios();
                case 5 -> duena.getUsuariosNormales().forEach(u ->
                        System.out.println(u.getIdUsuario() + " | " + u.getNombreUsuario() + " | " + u.getEmailUsuario()));
                case 6 -> duena.getCompras().forEach(System.out::println);
                case 0 -> usuarioActual = null;
                default -> System.out.println("Opción inválida.");
            }
        } while (usuarioActual != null);
    }

    private static void listarProductosAdminView() {
        System.out.println("\n--- Todos los Productos (admin view) ---");
        if (duena.getProductos().isEmpty()) {
            System.out.println("No hay productos.");
            return;
        }
        duena.getProductos().forEach(p -> System.out.println(p.toString() + ", Stock: " + p.getStock()));
    }

    private static void altaProductoPorDuena() {
        System.out.println("\n--- Alta de Producto (Dueña) ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine();
        System.out.print("Precio: ");
        double precio = leerDouble();
        System.out.print("Stock: ");
        int stock = leerEntero();
        scanner.nextLine();
        System.out.print("Categoría: ");
        String categoria = scanner.nextLine();

        try {
            Producto nuevo = new Producto(nextProductoId++, nombre, descripcion, precio, stock, LocalDate.now().toString(), categoria);
            nuevo.setEstadoProducto(true);
            duena.agregarProducto(nuevo);
            Persistencia.guardar(RUTA_JSON, sistema);
            System.out.println(" Producto creado y publicado (ID=" + nuevo.getIdProducto() + ").");
        } catch (ProductoInvalidoException e) {
            System.out.println(" Error al crear producto: " + e.getMessage());
        }
    }

    private static void registrarAdminContenido() {
        System.out.println("\n--- Registrar Administrador de Contenido ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        AdministradorContenido admin = new AdministradorContenido(nextUsuarioId++, email, contrasena,
                "AdministradorContenido", LocalDate.now(), "ACTIVA", nombre);
        duena.registrarAdministradorContenido(admin);
        Persistencia.guardar(RUTA_JSON, sistema);
        System.out.println(" AdministradorContenido registrado (ID=" + admin.getIdUsuario() + ").");
    }

    private static void registrarAdminUsuarios() {
        System.out.println("\n--- Registrar Administrador de Usuarios ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        AdministradorUsuarios admin = new AdministradorUsuarios(nextUsuarioId++, email, contrasena,
                "AdministradorUsuarios", LocalDate.now(), "ACTIVA", nombre);
        duena.registrarAdminUsuario(admin);
        Persistencia.guardar(RUTA_JSON, sistema);
        System.out.println(" AdministradorUsuarios registrado (ID=" + admin.getIdUsuario() + ").");
    }

    // ==========================
    // Menu Administrador de Contenido
    // ==========================
    private static void menuAdministradorContenido() {
        AdministradorContenido admin = (AdministradorContenido) usuarioActual;
        int op;
        do {
            System.out.println("\n--- Menu AdministradorContenido (" + admin.getNombreUsuario() + ") ---");
            System.out.println("1. Crear producto (borrador)");
            System.out.println("2. Publicar producto (usar ID)");
            System.out.println("3. Despublicar producto (usar ID)");
            System.out.println("4. Listar mis productos (borradores)");
            System.out.println("5. Listar todos los productos (global)");
            System.out.println("0. Cerrar sesión");
            System.out.print("Seleccione: ");
            op = leerEntero();
            scanner.nextLine();

            switch (op) {
                case 1 -> {
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Descripción: ");
                    String descripcion = scanner.nextLine();
                    System.out.print("Precio: ");
                    double precio = leerDouble();
                    System.out.print("Stock: ");
                    int stock = leerEntero();
                    scanner.nextLine();
                    System.out.print("Categoría: ");
                    String categoria = scanner.nextLine();

                    try {
                        Producto nuevo = new Producto(nextProductoId++, nombre, descripcion, precio, stock, LocalDate.now().toString(), categoria);
                       
                        nuevo.setEstadoProducto(false);
                        duena.agregarProducto(nuevo);
                        
                        Persistencia.guardar(RUTA_JSON, sistema);
                        System.out.println(" Producto creado como borrador (ID=" + nuevo.getIdProducto() + ").");
                    } catch (ProductoInvalidoException e) {
                        System.out.println(" Error: " + e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.print("ID del producto a publicar: ");
                    int id = leerEntero();
                    scanner.nextLine();
                    Optional<Producto> opt = duena.getProductos().stream().filter(p -> p.getIdProducto() == id).findFirst();
                    if (opt.isPresent()) {
                        opt.get().setEstadoProducto(true);
                        Persistencia.guardar(RUTA_JSON, sistema);
                        System.out.println(" Producto publicado.");
                    } else System.out.println("️ Producto no encontrado.");
                }
                case 3 -> {
                    System.out.print("ID del producto a despublicar: ");
                    int id = leerEntero();
                    scanner.nextLine();
                    Optional<Producto> opt2 = duena.getProductos().stream().filter(p -> p.getIdProducto() == id).findFirst();
                    if (opt2.isPresent()) {
                        opt2.get().setEstadoProducto(false);
                        Persistencia.guardar(RUTA_JSON, sistema);
                        System.out.println(" Producto despublicado.");
                    } else System.out.println("️ Producto no encontrado.");
                }
                case 4 -> {
                    System.out.println("--- Mis borradores / productos (global) ---");
                    admin.verTodosLosProductos().forEach(p -> System.out.println(p.getNombre() + " | ID: " + p.getId()));
                }
                case 5 -> duena.getProductos().forEach(p -> System.out.println(p.toString() + ", Stock: " + p.getStock()));
                case 0 -> usuarioActual = null;
                default -> System.out.println("Opción inválida.");
            }
        } while (usuarioActual != null);
    }

    // ==========================
    // Menu Administrador de Usuarios
    // ==========================
    private static void menuAdministradorUsuarios() {
        AdministradorUsuarios admin = (AdministradorUsuarios) usuarioActual;
        int op;
        do {
            System.out.println("\n--- Menu AdministradorUsuarios (" + admin.getNombreUsuario() + ") ---");
            System.out.println("1. Crear Usuario Normal");
            System.out.println("2. Activar Usuario por ID");
            System.out.println("3. Desactivar Usuario por ID");
            System.out.println("4. Listar Usuarios creados por mi");
            System.out.println("0. Cerrar sesión");
            System.out.print("Seleccione: ");
            op = leerEntero();
            scanner.nextLine();

            switch (op) {
                case 1 -> {
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Contraseña: ");
                    String contrasena = scanner.nextLine();
                    admin.crearUsuario(nextUsuarioId++, nombre, email, contrasena, "Normal", LocalDate.now());
                    Persistencia.guardar(RUTA_JSON, sistema);
                }
                case 2 -> {
                    System.out.print("ID usuario a activar: ");
                    int idAct = leerEntero();
                    scanner.nextLine();
                    admin.activarUsuario(idAct);
                    Persistencia.guardar(RUTA_JSON, sistema);
                }
                case 3 -> {
                    System.out.print("ID usuario a desactivar: ");
                    int idDes = leerEntero();
                    scanner.nextLine();
                    admin.desactivarUsuario(idDes);
                    Persistencia.guardar(RUTA_JSON, sistema);
                }
                case 4 -> admin.listarUsuarios();
                case 0 -> usuarioActual = null;
                default -> System.out.println("Opción inválida.");
            }
        } while (usuarioActual != null);
    }

    // ==========================
    // Menu Usuario Normal (cliente)
    // ==========================
    private static void menuUsuarioNormal() {
        int op;
        do {
            System.out.println("\n--- Menu Usuario (" + usuarioActual.getNombreUsuario() + ") ---");
            System.out.println("1. Listar productos publicados");
            System.out.println("2. Registrar compra");
            System.out.println("0. Cerrar sesión");
            System.out.print("Seleccione: ");
            op = leerEntero();
            scanner.nextLine();

            switch (op) {
                case 1 -> listarProductosPublicados();
                case 2 -> registrarCompra();
                case 0 -> {
                    usuarioActual = null;
                    Persistencia.guardar(RUTA_JSON, sistema);
                }
                default -> System.out.println("Opción inválida.");
            }
        } while (usuarioActual != null);
    }

    // ==========================
    // Funcionalidades comunes
    // ==========================
    private static void listarProductosPublicados() {
        System.out.println("\n--- Productos Disponibles ---");
        duena.getProductos().stream()
                .filter(p -> p.getEstadoProducto() != null && p.getEstadoProducto())
                .forEach(p -> System.out.println(p.getIdProducto() + " | " + p.getNombre() + " | Stock: " + p.getStock() + " | $" + p.getPrecio()));
    }

    // registrarCompra usa usuarioActual como cliente
    private static void registrarCompra() {
        System.out.println("\n--- Registro de Compra ---");

        // validar existencia de productos publicados
        Optional<Producto> anyPublished = duena.getProductos().stream()
                .filter(p -> p.getEstadoProducto() != null && p.getEstadoProducto())
                .findAny();
        if (anyPublished.isEmpty()) {
            System.out.println(" No hay productos publicados.");
            return;
        }

        listarProductosPublicados();
        System.out.print("Ingrese ID del producto a comprar: ");
        int id = leerEntero();
        scanner.nextLine();

        Producto producto = duena.getProductos().stream()
                .filter(p -> p.getIdProducto() == id && Boolean.TRUE.equals(p.getEstadoProducto()))
                .findFirst().orElse(null);
        if (producto == null) {
            System.out.println(" Producto no encontrado o no disponible.");
            return;
        }

        System.out.print("Ingrese cantidad a comprar: ");
        int cantidad = leerEntero();
        if (cantidad <= 0) {
            System.out.println(" Cantidad inválida.");
            return;
        }
        if (cantidad > producto.getStock()) {
            System.out.println(" Stock insuficiente. Disponible: " + producto.getStock());
            return;
        }

        // preparar cliente real a partir del usuarioActual (si es UsuarioNormal)
        Cliente clienteComprador;
        if (usuarioActual instanceof Cliente) {
            clienteComprador = (Cliente) usuarioActual;
        } else if (usuarioActual instanceof UsuarioNormal) {
            UsuarioNormal u = (UsuarioNormal) usuarioActual;
            clienteComprador = new Cliente(
                    u.getIdUsuario(),
                    u.getEmailUsuario(),
                    u.getContrasena(),
                    "CLIENTE",
                    u.getFechaRegistro(),
                    u.getEstadoCuenta(),
                    "Dirección no registrada",
                    5550000,
                    u.getNombreUsuario()
            );
            duena.registrarCliente(clienteComprador); // lo guardamos como cliente también
        } else {
            System.out.println(" Solo usuarios normales pueden comprar.");
            return;
        }

        MetodoPago mp = new MetodoPago("Tarjeta");
        Compra compra = new Compra(nextCompraId++, clienteComprador, mp);
        LineaCompra linea = new LineaCompra(producto, cantidad);

        try {
            compra.agregarLinea(linea);
            compra.finalizarCompra();

            // actualizar stock (asegúrate de que Producto tenga setStock)
            producto.setStock(producto.getStock() - cantidad);

            duena.registrarCompra(compra);
            Persistencia.guardar(RUTA_JSON, sistema);

            System.out.println(" Compra registrada. Resumen:");
            System.out.println(compra);
        } catch (CompraInvalidaException e) {
            System.out.println(" Error en la compra: " + e.getMessage());
        }
    }

    // ==========================
    // Utilidades de entrada
    // ==========================
    private static int leerEntero() {
        while (!scanner.hasNextInt()) {
            System.out.print("Ingrese un número válido: ");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static double leerDouble() {
        while (!scanner.hasNextDouble()) {
            System.out.print("Ingrese un número decimal válido: ");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}
