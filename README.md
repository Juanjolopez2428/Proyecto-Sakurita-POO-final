🌸 GlowUp — Sistema de Gestión Integral (POO con Persistencia JSON)
🧠 Descripción general

GlowUp es una aplicación desarrollada en Java que simula un sistema de gestión empresarial para una compañía dirigida por la Dueña (Sakura).
Implementa herencia, polimorfismo, manejo de excepciones personalizadas y persistencia de datos en JSON usando la librería Gson.

El sistema maneja:

Gestión de usuarios (Dueña, Administradores, Clientes)

Control de productos y ventas

Producción y fábricas

Guardado automático de datos con persistencia

🧩 Estructura del proyecto
src/
├── Main.java
├── Persistencia/
│   ├── Persistencia.java
│   └── SistemaGlowUp.java
├── Usuarios/
│   ├── Usuario.java (abstracta)
│   ├── Duena.java
│   ├── AdministradorContenido.java
│   ├── AdministradorUsuarios.java
│   ├── UsuarioNormal.java
│   └── MiembroConsejo.java
├── Ventas/
│   ├── Producto.java
│   ├── Categoria.java
│   ├── Compra.java
│   ├── Carrito.java
│   ├── MetodoPago.java
│   └── LineaCompra.java
├── Produccion/
│   ├── DesarrolladorProducto.java
│   └── Fabrica.java
└── OperacionesOcultas/
    ├── ConsejoSombrio.java
    ├── RegistroEsclavos.java
    └── TrabajadorEsclavizado.java

⚙️ Requisitos
🧰 Librerías externas

El proyecto usa Gson para convertir objetos Java a JSON y viceversa.

Descarga gson-x.x.x.jar desde
👉 https://mvnrepository.com/artifact/com.google.code.gson/gson

Agrégalo al classpath de tu IDE (Eclipse o IntelliJ).

🚀 Cómo ejecutar el proyecto
1️⃣ Compilar

Abre la terminal en la carpeta src y ejecuta:

javac -cp .;gson-2.10.1.jar Main.java


(En Mac/Linux reemplaza ; por :)

2️⃣ Ejecutar
java -cp .;gson-2.10.1.jar Main

🧙‍♀️ Flujo del sistema

Al iniciar el programa, se intenta cargar los datos desde data/sistema.json.
Si no existe, se crea una nueva instancia del sistema con la Dueña predeterminada:

Email: duena@darkcorp.com  
Contraseña: MasterKey123

Menú principal:

Iniciar sesión

Puedes ingresar con la Dueña o con cualquier usuario registrado.

Registrar nuevo usuario

Crea UsuarioNormal, AdministradorContenido o AdministradorUsuarios.

Listar productos disponibles

Muestra todos los productos creados.

Registrar compra

Permite seleccionar un producto, indicar cantidad y registrar la compra.

Guardar y salir

Guarda toda la información en JSON.

💾 Persistencia de datos
📂 Archivos generados

data/sistema.json → Guarda toda la información del sistema.

Ejemplo:

{
  "duena": {
    "idUsuario": 999,
    "emailUsuario": "duena@darkcorp.com",
    "nombreUsuario": "Dark Mistress",
    "productos": [
      {
        "idProducto": 1,
        "nombre": "Crema Facial Sakura",
        "descripcion": "Hidratante natural",
        "precio": 199.99,
        "stock": 50
      }
    ]
  }
}

🧱 Persistencia automática

Cada vez que realizas una acción (crear usuario, producto o compra), el sistema ejecuta:

Persistencia.guardar("data/sistema.json", sistema);


Y al iniciar:

sistema = Persistencia.cargar("data/sistema.json", SistemaGlowUp.class);


Así se mantiene todo guardado entre ejecuciones.

🔐 Excepciones y validaciones

El sistema incluye validaciones y excepciones personalizadas para mantener integridad:

Excepción	Descripción
ExcepcionProductoInvalido	Evita registrar productos con valores inválidos (precio < 0, nombre vacío).
ExcepcionUsuarioDuplicado	Impide crear usuarios con el mismo correo.
IllegalArgumentException	Maneja errores de entrada desde el menú.

Además:

Los IDs se generan automáticamente.

Se valida que los campos no estén vacíos.

Al registrar compras se verifica el stock disponible.

👑 Roles del sistema
Rol	Función	Permisos
Dueña (Sakura)	Control total	Puede acceder a todas las clases, registrar usuarios, productos, fábricas, etc.
AdministradorContenido	Manejo de productos	Crear, editar, eliminar y publicar productos.
AdministradorUsuarios	Manejo de usuarios	Crear, activar y desactivar cuentas.
UsuarioNormal (Cliente)	Usuario común	Consultar productos y registrar compras.
🧠 Clases principales
📘 Main.java

Contiene el menú principal.

Carga y guarda los datos del sistema.

Controla el flujo de usuarios y compras.

📗 Persistencia.java

Usa Gson para guardar y leer objetos JSON.

Implementa los métodos guardar() y cargar().

📙 SistemaGlowUp.java

Contiene una instancia principal de la Dueña (Duena).

Centraliza el acceso a todas las clases del sistema.

🏁 Créditos

Autor: Juan Jose López
Materia: Programación Orientada a Objetos
Lenguaje: Java 17
Persistencia: Gson (JSON)
Versión: 2.0 — Octubre 2025
