package Persistencia.DAO;

import Ventas.Producto;
import Excepciones.ProductoInvalidoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set; // Import necesario para el update

public class ProductoDAO {
    private final MongoCollection<Document> coleccion;


    public ProductoDAO(MongoDatabase db) {
        this.coleccion = db.getCollection("productos");
    }

    public void insertarProducto(Producto producto) {
        try {
            if (producto == null)
                throw new ProductoInvalidoException("El producto no puede ser nulo.");
            if (producto.getPrecio() <= 0)
                throw new ProductoInvalidoException("El precio debe ser mayor que cero.");
            if (producto.getStock() < 0)
                throw new ProductoInvalidoException("El stock no puede ser negativo.");

            Document doc = new Document("idProducto", producto.getIdProducto())
                    .append("nombre", producto.getNombre())
                    .append("descripcion", producto.getDescripcion())
                    .append("precio", producto.getPrecio())
                    .append("stock", producto.getStock())
                    .append("fechaRegistro", producto.getFechaLanzamiento())
                    .append("categoria", producto.getCategoria())
                    .append("estadoProducto", producto.getEstadoProducto());

            coleccion.insertOne(doc);
            System.out.println("📦 Producto guardado en MongoDB: " + producto.getNombre());
        } catch (ProductoInvalidoException e) {
            System.err.println(" Error de validación de producto: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("️ Error al guardar producto en MongoDB: " + e.getMessage());
        }
    }

    public void actualizarStock(int idProducto, int nuevoStock) {
        try {
            if (nuevoStock < 0)
                throw new ProductoInvalidoException("El stock no puede ser negativo.");


            coleccion.updateOne(eq("idProducto", idProducto), set("stock", nuevoStock));

            System.out.println("🔄 Stock actualizado para el producto ID " + idProducto);
        } catch (ProductoInvalidoException e) {
            System.err.println(" Error al actualizar stock: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("️ Error al actualizar producto en MongoDB: " + e.getMessage());
        }
    }

    public List<Producto> obtenerTodos() {
        List<Producto> lista = new ArrayList<>();
        try {
            for (Document doc : coleccion.find()) {

                Producto p = new Producto(
                        doc.getInteger("idProducto"),
                        doc.getString("nombre"),
                        doc.getString("descripcion"),
                        doc.getDouble("precio"),
                        doc.getInteger("stock"),
                        doc.getString("fechaRegistro"),
                        doc.getString("categoria")
                );
                p.setEstadoProducto(doc.getBoolean("estadoProducto", false));
                lista.add(p);
            }
        } catch (Exception e) {
            System.err.println("️ Error al obtener productos: " + e.getMessage());
        }
        return lista;
    }

    public Producto buscarPorId(int idProducto) {
        try {
            Document doc = coleccion.find(eq("idProducto", idProducto)).first();
            if (doc == null) return null;

            Producto p = new Producto(
                    doc.getInteger("idProducto"),
                    doc.getString("nombre"),
                    doc.getString("descripcion"),
                    doc.getDouble("precio"),
                    doc.getInteger("stock"),
                    doc.getString("fechaRegistro"), // Usamos fechaRegistro
                    doc.getString("categoria")
            );
            p.setEstadoProducto(doc.getBoolean("estadoProducto", false));
            return p;
        } catch (Exception e) {
            System.err.println("️ Error al buscar producto por ID: " + e.getMessage());
        }
        return null;
    }
}