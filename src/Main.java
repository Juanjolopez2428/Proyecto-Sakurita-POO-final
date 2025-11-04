import Persistencia.DAO.*;
import Usuarios.*;
import Ventas.*;
import Excepciones.*;
import Persistencia.DAO.MongoConexion;
import Produccion.Fabrica;
import OperacionesOcultas.TrabajadorEsclavizado;

import com.mongodb.client.MongoDatabase;
import java.time.LocalDate;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static Usuario usuarioActual = null;
    private static Duena duena = null;

    private static UsuarioDAO usuarioDAO = null;
    private static ProductoDAO productoDAO = null;
    private static CompraDAO compraDAO = null;
    private static CategoriaDAO categoriaDAO = null;

    // DAOs para Operaciones Ocultas
    private static FabricaDAO fabricaDAO = null;
    private static TrabajadorDAO trabajadorDAO = null;

    private static int nextProductoId = 1001;
    private static int nextUsuarioId = 1;
    private static int nextCompraId = 5001;
    private static int nextClienteId = 200;
    private static int nextCategoriaId = 1;

    // Contadores para Operaciones Ocultas
    private static int nextFabricaId = 1;
    private static int nextTrabajadorId = 1;

    public static void main(String[] args) {
        MongoDatabase db = MongoConexion.conectar();
        if (db == null) {
            System.err.println("❌ No se pudo conectar con MongoDB.");
            return;
        }
        System.out.println("✅ Conectado a MongoDB correctamente.");

        usuarioDAO = new UsuarioDAO(db);
        productoDAO = new ProductoDAO(db);
        compraDAO = new CompraDAO(db);
        categoriaDAO = new CategoriaDAO(db);

        // Inicialización de DAOs
        fabricaDAO = new FabricaDAO(db);
        trabajadorDAO = new TrabajadorDAO(db, fabricaDAO);

        inicializarDatosBase();

        ejecutarMenuPrincipal();

        MongoConexion.cerrarConexion();
        System.out.println("🔒 Conexión cerrada. ¡Hasta luego!");
    }

    // ------------------------------------------------------------------
// Inicialización de datos base
// ------------------------------------------------------------------
    private static void inicializarDatosBase() {
        System.out.println("\n📦 Verificando datos iniciales...");

        List<Usuario> usuarios = usuarioDAO.obtenerTodos();
        List<Producto> productos = productoDAO.obtenerTodos();
        List<Categoria> categorias = categoriaDAO.listarCategorias();

        String hash1234 = Integer.toHexString("1234".hashCode());


        // --- USUARIOS ---
        if (usuarios == null || usuarios.isEmpty()) {
            Duena du = new Duena(0, "duena@sakurita.com", hash1234, "Dueña Principal", hash1234);
            usuarioDAO.insertarUsuario(du);
            duena = du;

            AdministradorContenido ac = new AdministradorContenido(nextUsuarioId++, "admincontenido@sakurita.com", hash1234, "AdministradorContenido", LocalDate.now(), "ACTIVA", "Admin Contenido");
            usuarioDAO.insertarUsuario(ac);

            AdministradorUsuarios au = new AdministradorUsuarios(nextUsuarioId++, "adminusuarios@sakurita.com", hash1234, "AdministradorUsuarios", LocalDate.now(), "ACTIVA", "Admin Usuarios");
            usuarioDAO.insertarUsuario(au);

            UsuarioNormal un = new UsuarioNormal(nextUsuarioId++, "usuario@sakurita.com", hash1234, "Normal", LocalDate.now(), "ACTIVA", "Usuario Normal");
            usuarioDAO.insertarUsuario(un);

            System.out.println("✅ Usuarios iniciales creados.");
        } else {
            Optional<Usuario> optDu = usuarios.stream().filter(u -> u instanceof Duena).findFirst();
            duena = optDu.map(u -> (Duena) u).orElseGet(() -> {
                Duena d = new Duena(0, "duena@sakurita.com", hash1234, "Dueña Principal", hash1234);
                usuarioDAO.insertarUsuario(d);
                return d;
            });
            usuarios.stream().mapToInt(Usuario::getIdUsuario).max().ifPresent(max -> nextUsuarioId = max + 1);
        }

        // --- CATEGORÍAS ---
        if (categorias == null || categorias.isEmpty()) {
            try {
                Categoria cuidadoFacial = new Categoria("Productos para el cuidado del rostro", nextCategoriaId++, "Cuidado facial");
                Categoria maquillaje = new Categoria("Productos de maquillaje", nextCategoriaId++, "Maquillaje");

                categoriaDAO.insertarCategoria(cuidadoFacial);
                categoriaDAO.insertarCategoria(maquillaje);

                System.out.println("✅ Categorías base añadidas a la DB.");

            } catch (Exception e) {
                System.err.println("⚠️ Error inesperado al inicializar categorías: " + e.getMessage());
            }

            categorias = categoriaDAO.listarCategorias();
        }

        categorias.stream().mapToInt(Categoria::getIdCategoria).max().ifPresent(max -> nextCategoriaId = max + 1);

        // --- PRODUCTOS ---
        if (productos == null || productos.isEmpty()) {
            try {

                Producto p1 = new Producto(nextProductoId++, "Mascarilla Sakura",
                        "Mascarilla hidratante japonesa", 45000.0, 20, LocalDate.now().toString(), "Cuidado facial");
                Producto p2 = new Producto(nextProductoId++, "Labial GlowUp",
                        "Color duradero con brillo natural", 38000.0, 15, LocalDate.now().toString(), "Maquillaje");

                p1.setEstadoProducto(true);
                p2.setEstadoProducto(true);

                productoDAO.insertarProducto(p1);
                productoDAO.insertarProducto(p2);

                System.out.println("✅ Productos iniciales añadidos.");
            } catch (Exception e) {
                System.err.println("⚠️ Error al crear productos iniciales: " + e.getMessage());
            }
        } else {
            productos.stream().mapToInt(Producto::getIdProducto).max().ifPresent(max -> nextProductoId = max + 1);
        }

        System.out.println("✔️ Datos base listos.");
    }

    // ------------------------------------------------------------------
// Menú principal
// ------------------------------------------------------------------
    private static void ejecutarMenuPrincipal() {
        int opcion;
        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Log-in");
            System.out.println("2. Registrar Usuario Normal");
            System.out.println("3. Listar Categorías");
            System.out.println("0. Salir");
            System.out.print("Seleccione: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> {
                    logInUsuario();
                    if (usuarioActual != null) ejecutarMenuPorRol();
                }
                case 2 -> registrarUsuarioNormal();
                case 3 -> listarCategorias();
                case 0 -> System.out.println("👋 Saliendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private static void logInUsuario() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        Usuario encontrado = usuarioDAO.buscarPorEmail(email);

        if (encontrado == null) {
            System.out.println("❌ Usuario no encontrado.");
            return;
        }

        String hashGeneradoIngresado = Integer.toHexString(contrasena.hashCode());

        if (encontrado.getContrasena().equals(hashGeneradoIngresado)) {
            usuarioActual = encontrado;
            System.out.println("✅ Log-in exitoso como " + encontrado.getRol());
        } else {
            System.out.println("❌ Contraseña incorrecta.");
        }
    }

    private static void registrarUsuarioNormal() {
        System.out.println("\n--- Registro Usuario Normal ---");
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Contraseña: ");
            String contrasena = scanner.nextLine();

            if (nombre.isEmpty() || email.isEmpty() || contrasena.length() < 4) {
                throw new UsuarioInvalidoException("Nombre/email vacíos o contraseña < 4 caracteres.");
            }
            if (usuarioDAO.buscarPorEmail(email) != null) {
                throw new UsuarioInvalidoException("El email ya está registrado.");
            }

            String hashNuevoUsuario = Integer.toHexString(contrasena.hashCode());

            UsuarioNormal nuevo = new UsuarioNormal(nextUsuarioId++,
                    email,
                    hashNuevoUsuario,
                    "Normal",
                    LocalDate.now(),
                    "ACTIVA",
                    nombre);

            usuarioDAO.insertarUsuario(nuevo);
            System.out.println("✅ Usuario registrado (ID=" + nuevo.getIdUsuario() + ").");
        } catch (UsuarioInvalidoException e) {
            System.out.println("⚠️ Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("⚠️ Error inesperado al registrar: " + e.getMessage());
        }
    }

// ------------------------------------------------------------------
// Métodos de Categoría y Producto
// ------------------------------------------------------------------

    private static void crearCategoria() {
        System.out.println("\n--- Crear Nueva Categoría ---");
        int id = nextCategoriaId;

        System.out.print("Nombre de la categoría (ej: Maquillaje de ojos): ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Descripción de la categoría: ");
        String descripcion = scanner.nextLine().trim();

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            System.out.println("❌ Error: El nombre y la descripción no pueden estar vacíos.");
            return;
        }

        try {
            Categoria nueva = new Categoria(descripcion, id, nombre);
            categoriaDAO.insertarCategoria(nueva);

            nextCategoriaId++;
            System.out.println("✅ Categoría '" + nombre + "' (ID: " + id + ") creada y guardada en la DB.");

        } catch (Exception e) {
            System.out.println("⚠️ Error al guardar la categoría: " + e.getMessage());
        }
    }

    private static void listarCategorias() {
        System.out.println("\n--- Categorías disponibles ---");
        List<Categoria> categorias = categoriaDAO.listarCategorias();
        if (categorias == null || categorias.isEmpty()) {
            System.out.println("⚠️ No hay categorías registradas.");
            return;
        }
        categorias.forEach(c ->
                System.out.println(c.getIdCategoria() + " | " + c.getNombreCategoria() + " | " + c.getDescripcionCategoria()));
    }

    private static void crearProducto() {
        try {
            System.out.println("\n--- Crear Producto ---");
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Descripción: ");
            String descripcion = scanner.nextLine();
            System.out.print("Precio: ");
            double precio = leerDouble();
            System.out.print("Stock: ");
            int stock = leerEntero();
            System.out.print("Categoría: ");
            String categoria = scanner.nextLine();

            if (precio <= 0) throw new ProductoInvalidoException("Precio debe ser mayor a 0");
            if (stock < 0) throw new ProductoInvalidoException("Stock no puede ser negativo");

            Producto nuevo = new Producto(nextProductoId++, nombre, descripcion, precio, stock, LocalDate.now().toString(), categoria);
            nuevo.setEstadoProducto(true);

            productoDAO.insertarProducto(nuevo);

            System.out.println("✅ Producto guardado correctamente.");
        } catch (ProductoInvalidoException e) {
            System.out.println("⚠️ Error: " + e.getMessage());
        }
    }

    private static void listarProductos() {
        System.out.println("\n--- Productos Disponibles ---");
        List<Producto> todos = productoDAO.obtenerTodos();
        if (todos == null || todos.isEmpty()) {
            System.out.println("No hay productos.");
            return;
        }
        todos.forEach(p -> System.out.println(p.getIdProducto() + " | " + p.getNombre() + " | Stock: " + p.getStock() + " | $" + p.getPrecio()));
    }

// ------------------------------------------------------------------
// MÉTODOS DE FÁBRICA Y TRABAJADOR (EXCLUSIVO DUEÑA)
// ------------------------------------------------------------------

    private static void registrarFabrica() {
        System.out.println("\n--- Registro de Fábrica (Producción) ---");

        System.out.print("País: ");
        String pais = scanner.nextLine();
        System.out.print("Ciudad: ");
        String ciudad = scanner.nextLine();
        System.out.print("Capacidad (unidades): ");
        int capacidad = leerEntero();
        System.out.print("Nivel de Automatización: ");
        String nivelAuto = scanner.nextLine();

        Fabrica nueva = new Fabrica(nextFabricaId++, pais, ciudad, capacidad, nivelAuto);
        fabricaDAO.insertarFabrica(nueva);
    }

    private static void listarFabricas() {
        System.out.println("\n--- Fábricas Registradas ---");
        List<Fabrica> fabricas = fabricaDAO.obtenerTodas();
        if (fabricas.isEmpty()) {
            System.out.println("No hay fábricas.");
            return;
        }
        fabricas.forEach(f -> System.out.println(f.getIdFabrica() + " | " + f.getPais() + "/" + f.getCiudad() + " | Capacidad: " + f.getCapacidad()));
    }

    private static void registrarTrabajadorEsclavizado() {
        System.out.println("\n--- Registro de Trabajador ---");

        // 1. Seleccionar Fábrica (Asociación)
        listarFabricas();
        List<Fabrica> fabricas = fabricaDAO.obtenerTodas();
        if (fabricas.isEmpty()) {
            System.out.println("❌ No se puede registrar trabajador sin fábricas. Registre una primero.");
            return;
        }

        System.out.print("Ingrese ID de la Fábrica para asignación: ");
        int idFabrica = leerEntero();
        Fabrica fabricaAsignada = fabricaDAO.buscarPorId(idFabrica);

        if (fabricaAsignada == null) {
            System.out.println("❌ Fábrica no encontrada. Trabajador no registrado.");
            return;
        }

        // 2. Recolección de datos del Trabajador
        System.out.print("País de Origen: ");
        String paisOrigen = scanner.nextLine();
        System.out.print("Edad: ");
        int edad = leerEntero();
        System.out.print("Estado de Salud: ");
        String salud = scanner.nextLine();

        TrabajadorEsclavizado nuevoTrabajador = new TrabajadorEsclavizado(
                nextTrabajadorId++,
                paisOrigen,
                edad,
                LocalDate.now(),
                salud,
                fabricaAsignada
        );

        // 3. Persistencia
        trabajadorDAO.insertarTrabajador(nuevoTrabajador);

        fabricaAsignada.agregarPersonal(nuevoTrabajador); // Actualiza la lista en memoria (opcional)

        System.out.println("✅ Trabajador registrado. Asignado a: " + fabricaAsignada.getPais());
    }

    private static void listarTrabajadores() {
        System.out.println("\n--- Listado de Trabajadores Registrados ---");
        List<TrabajadorEsclavizado> trabajadores = trabajadorDAO.obtenerTodos();
        if (trabajadores.isEmpty()) {
            System.out.println("No hay trabajadores registrados en la base de datos.");
            return;
        }

        trabajadores.forEach(t -> {
            String fabrica = t.getAsignadoA() != null ? t.getAsignadoA().getPais() : "Sin asignar";
            System.out.println("ID: " + t.getIdTrabajador() + " | Origen: " + t.getPaisOrigen() +
                    " | Fábrica: " + fabrica + " | Salud: " + t.getSalud());
        });
    }


    // ------------------------------------------------------------------
// Menús por rol
// ------------------------------------------------------------------
    private static void ejecutarMenuPorRol() {
        if (usuarioActual instanceof Duena) menuDuena();
        else if (usuarioActual instanceof AdministradorContenido) menuAdministradorContenido();
        else if (usuarioActual instanceof AdministradorUsuarios) menuAdministradorUsuarios();
        else menuUsuarioNormal();
    }

    private static void menuDuena() {
        int op;
        do {
            System.out.println("\n--- Menú Dueña ---");
            // Opciones Generales
            System.out.println("1. Registrar Admin Contenido");
            System.out.println("2. Registrar Admin Usuarios");
            System.out.println("3. Crear producto");
            System.out.println("4. Listar productos");
            System.out.println("5. Crear categoría");

            // OPERACIONES OCULTAS (Acceso exclusivo)
            System.out.println("6. Registrar nueva Fábrica");
            System.out.println("7. Listar Fábricas");
            System.out.println("8. Registrar Trabajador");
            System.out.println("9. Listar Trabajadores");

            System.out.println("0. Cerrar sesión");
            System.out.print("Seleccione: ");
            op = leerEntero();

            switch (op) {
                case 1 -> registrarAdminContenido();
                case 2 -> registrarAdminUsuarios();
                case 3 -> crearProducto();
                case 4 -> listarProductos();
                case 5 -> crearCategoria();
                case 6 -> registrarFabrica();
                case 7 -> listarFabricas();
                case 8 -> registrarTrabajadorEsclavizado();
                case 9 -> listarTrabajadores();
                case 0 -> usuarioActual = null;
                default -> System.out.println("Opción inválida.");
            }
        } while (usuarioActual != null);
    }

    private static void registrarAdminContenido() {
        System.out.println("\n--- Registrar Admin Contenido ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        String hashAdmin = Integer.toHexString(contrasena.hashCode());

        AdministradorContenido admin = new AdministradorContenido(nextUsuarioId++,
                email,
                hashAdmin,
                "AdministradorContenido",
                LocalDate.now(),
                "ACTIVA",
                nombre);
        usuarioDAO.insertarUsuario(admin);
        System.out.println("✅ AdministradorContenido registrado.");
    }

    private static void registrarAdminUsuarios() {
        System.out.println("\n--- Registrar Admin Usuarios ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        String hashAdmin = Integer.toHexString(contrasena.hashCode());

        AdministradorUsuarios admin = new AdministradorUsuarios(nextUsuarioId++,
                email,
                hashAdmin,
                "AdministradorUsuarios",
                LocalDate.now(),
                "ACTIVA",
                nombre);
        usuarioDAO.insertarUsuario(admin);
        System.out.println("✅ AdministradorUsuarios registrado.");
    }

    private static void menuAdministradorContenido() {
        int op;
        do {
            System.out.println("\n--- Menú AdministradorContenido ---");
            System.out.println("1. Crear producto");
            System.out.println("2. Listar productos");
            System.out.println("3. Crear categoría");
            System.out.println("0. Cerrar sesión");
            System.out.print("Seleccione: ");
            op = leerEntero();

            switch (op) {
                case 1 -> crearProducto();
                case 2 -> listarProductos();
                case 3 -> crearCategoria();
                case 0 -> usuarioActual = null;
                default -> System.out.println("Opción inválida.");
            }
        } while (usuarioActual != null);
    }

    private static void menuAdministradorUsuarios() {
        int op;
        do {
            System.out.println("\n--- Menú AdministradorUsuarios ---");
            System.out.println("1. Listar usuarios");
            System.out.println("0. Cerrar sesión");
            System.out.print("Seleccione: ");
            op = leerEntero();

            switch (op) {
                case 1 -> {
                    List<Usuario> todos = usuarioDAO.obtenerTodos();
                    todos.forEach(u -> System.out.println(u.getIdUsuario() + " | " + u.getNombreUsuario() + " | " + u.getEmailUsuario() + " | " + u.getRol()));
                }
                case 0 -> usuarioActual = null;
                default -> System.out.println("Opción inválida.");
            }
        } while (usuarioActual != null);
    }

    private static void menuUsuarioNormal() {
        int op;
        do {
            System.out.println("\n--- Menú Usuario Normal ---");
            System.out.println("1. Ver productos");
            System.out.println("2. Comprar producto");
            System.out.println("0. Cerrar sesión");
            System.out.print("Seleccione: ");
            op = leerEntero();

            switch (op) {
                case 1 -> listarProductos();
                case 2 -> registrarCompra();
                case 0 -> usuarioActual = null;
                default -> System.out.println("Opción inválida.");
            }
        } while (usuarioActual != null);
    }

    // ------------------------------------------------------------------
// Métodos de Compra (LÓGICA DE CARRITO)
// ------------------------------------------------------------------
    private static void registrarCompra() {
        if (!(usuarioActual instanceof UsuarioNormal)) {
            System.out.println("❌ Error: Solo usuarios normales pueden realizar compras.");
            return;
        }

        UsuarioNormal u = (UsuarioNormal) usuarioActual;
        Cliente clienteComprador = new Cliente(u.getIdUsuario(),
                u.getEmailUsuario(),
                u.getContrasena(),
                "CLIENTE",
                u.getFechaRegistro(),
                u.getEstadoCuenta(),
                "Dirección no registrada",
                0,
                u.getNombreUsuario());

        MetodoPago mp = new MetodoPago("Tarjeta");
        Compra compraActual = new Compra(nextCompraId, clienteComprador, mp);

        int op;
        boolean compraExitosa = false;

        do {
            System.out.println("\n--- Carrito de Compras ---");
            System.out.println("Productos en carrito: " + compraActual.getLineasCompra().size());
            System.out.println("Total parcial: $" + compraActual.getTotalVenta());
            System.out.println("1. Agregar producto al carrito");
            System.out.println("2. Finalizar compra (Pagar)");
            System.out.println("0. Cancelar y salir (Vaciar carrito)");
            System.out.print("Seleccione una opción: ");

            op = leerEntero();

            if (op == 1) {
                listarProductos();
                System.out.print("Ingrese ID del producto a agregar: ");
                int id = leerEntero();

                Producto producto = productoDAO.buscarPorId(id);
                if (producto == null) {
                    System.out.println("❌ Producto no encontrado.");
                    continue;
                }

                System.out.print("Cantidad: ");
                int cantidad = leerEntero();

                try {
                    if (cantidad <= 0) {
                        System.out.println("⚠️ Cantidad inválida.");
                        continue;
                    }

                    int stockDisponible = producto.getStock();

                    if (cantidad > stockDisponible) {
                        System.out.println("❌ Stock insuficiente. Disponible: " + stockDisponible);
                    } else {
                        LineaCompra linea = new LineaCompra(producto, cantidad);
                        compraActual.agregarLinea(linea);
                        System.out.println("✅ " + cantidad + " unidades de '" + producto.getNombre() + "' añadidas al carrito.");
                    }

                } catch (CompraInvalidaException e) {
                    System.out.println("⚠️ Error al agregar producto: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("⚠️ Error inesperado: " + e.getMessage());
                }

            } else if (op == 2) {
                if (compraActual.getLineasCompra().isEmpty()) {
                    System.out.println("⚠️ El carrito está vacío. Agregue productos primero.");
                    continue;
                }

                try {
                    compraActual.finalizarCompra();

                    compraDAO.insertarCompra(compraActual);

                    for (LineaCompra lc : compraActual.getLineasCompra()) {
                        Producto p = lc.getProducto();
                        int nuevoStock = p.getStock() - lc.getCantidad();
                        productoDAO.actualizarStock(p.getIdProducto(), nuevoStock);
                    }

                    System.out.println("\n--------------------------------------------------");
                    System.out.println("🎉 ¡COMPRA COMPLETADA CON ÉXITO! (ID: " + compraActual.getIdVenta() + ")");
                    System.out.println("Productos totales en líneas: " + compraActual.getLineasCompra().size());
                    System.out.println("TOTAL FINAL: $" + compraActual.getTotalVenta());
                    System.out.println("--------------------------------------------------");

                    nextCompraId++;
                    compraExitosa = true;
                    op = 0;

                } catch (Exception e) {
                    System.out.println("❌ Error al finalizar la compra: " + e.getMessage());
                    compraActual = new Compra(nextCompraId, clienteComprador, mp);
                }
            }
        } while (op != 0);

        if (!compraExitosa) {
            System.out.println("🛒 Operación de compra cancelada.");
        }
    }

    // ------------------------------------------------------------------
// Utilidades lectura
// ------------------------------------------------------------------
    private static int leerEntero() {
        int valor;
        while (!scanner.hasNextInt()) {
            System.out.print("Ingrese un número válido: ");
            scanner.next();
        }
        valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    private static double leerDouble() {
        double valor;
        while (!scanner.hasNextDouble()) {
            System.out.print("Ingrese un número válido: ");
            scanner.next();
        }
        valor = scanner.nextDouble();
        scanner.nextLine();
        return valor;
    }
}