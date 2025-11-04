# 🛍️ Proyecto de Gestión de Ventas y Operaciones Ocultas

## 📝 Descripción del Proyecto

Este proyecto es una aplicación de consola desarrollada en **Java** que simula un sistema integral de gestión para una empresa de venta de productos de belleza, denominada "Sakurita". La aplicación utiliza la base de datos **MongoDB** para manejar toda la persistencia de datos.

El sistema se distingue por implementar una robusta arquitectura basada en **cuatro roles** (Dueña, Administrador de Contenido, Administrador de Usuarios y Usuario Normal) y maneja todas las funcionalidades estándar de un e-commerce, incluyendo un flujo transaccional detallado de **Carrito de Compras**. Un aspecto clave y sensible del proyecto son los módulos de **Operaciones Ocultas** para la gestión de la producción (`Fábrica` y `TrabajadorEsclavizado`), cuyo acceso está **restringido de forma exclusiva** al rol de la **Dueña**.

---

## ⚙️ Arquitectura y Tecnologías

El proyecto fue construido utilizando **Java** como lenguaje principal y emplea **MongoDB** como su base de datos NoSQL, conectándose a través del driver síncrono de MongoDB. Sigue el patrón de diseño **DAO (Data Access Object)** para garantizar una clara separación de la lógica de negocio y la capa de acceso a datos, implementada en el paquete `Persistencia.DAO`.

La estructura del código se organiza en paquetes que reflejan las áreas funcionales: `Usuarios` (manejo de roles), `Ventas` (e-commerce), `Produccion` (cadena de suministro) y `OperacionesOcultas` (datos sensibles).

---

## 🚀 Instalación y Ejecución

### Requisitos Previos

Para ejecutar el proyecto necesitas **Java JDK 17** o superior y el **Servidor de MongoDB** debe estar corriendo (generalmente en `mongodb://localhost:27017`). Además, el proyecto debe tener las librerías del driver de MongoDB (`mongodb-driver-sync`, `bson`, `mongodb-driver-core`) añadidas al *classpath* de tu IDE (IntelliJ IDEA o Eclipse).

### Pasos de Ejecución

1.  Asegúrate de que MongoDB esté activo y accesible en la configuración predeterminada.
2.  Ejecuta la clase principal **`Main.java`** desde tu IDE.
3.  El programa se conectará automáticamente a la base de datos y cargará los datos iniciales (usuarios, categorías, productos).

---

## 🔒 Control de Acceso y Roles

El acceso a las funcionalidades se basa en el rol del usuario que inicia sesión. La **contraseña para todos los roles por defecto es `1234`**. A continuación, se detallan las credenciales y el nivel de acceso:

* **Dueña** (`duena@sakurita.com`): Es el rol principal y tiene acceso a **TODAS** las funcionalidades, incluyendo la gestión de Administradores y todas las Operaciones Ocultas.
* **Administrador de Contenido** (`admincontenido@sakurita.com`): Puede crear y listar Productos y Categorías.
* **Administrador de Usuarios** (`adminusuarios@sakurita.com`): Su única función es listar y gestionar usuarios.
* **Usuario Normal** (`usuario@sakurita.com`): Puede ver productos y realizar compras a través del carrito.

---

## ✨ Funcionalidades Clave

### 1. Módulo de Ventas (E-commerce)

Este módulo maneja la lógica de la tienda:
* **Productos y Categorías**: Permite la gestión de inventario y la clasificación de los artículos.
* **Carrito de Compras**: El `UsuarioNormal` interactúa con un flujo transaccional que utiliza las clases `Compra` y `LineaCompra`. Al finalizar la compra, el sistema **persiste la transacción** y, críticamente, **actualiza el `stock`** de los productos vendidos en MongoDB.

### 2. Módulo de Operaciones Ocultas (Exclusivo Dueña)

Este módulo permite la gestión de la producción y es inaccesible para cualquier otro rol, garantizando la confidencialidad de los datos sensibles:
* **Registro de Fábricas**: La Dueña puede registrar la información de producción (`Fabrica`) en la colección `fabricas`.
* **Registro y Listado de Trabajadores**: Permite la inserción de un `TrabajadorEsclavizado`. Al registrarlo, el sistema establece una **asociación** con una fábrica existente, guardando el ID de la `Fabrica` asignada en el documento del trabajador en la colección `trabajadores`. La Dueña puede listar y ver estas asociaciones en cualquier momento.
