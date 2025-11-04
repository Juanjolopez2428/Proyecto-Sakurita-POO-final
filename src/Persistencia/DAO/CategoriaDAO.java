package Persistencia.DAO;

import Ventas.Categoria;
import Ventas.Producto;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class CategoriaDAO {

    private final MongoCollection<Document> coleccionCategoria;

    public CategoriaDAO(MongoDatabase baseDatos) {
        this.coleccionCategoria = baseDatos.getCollection("categorias");
    }


    public void insertarCategoria(Categoria categoria) {
        try {
            Document doc = new Document("idCategoria", categoria.getIdCategoria())
                    .append("nombreCategoria", categoria.getNombreCategoria())
                    .append("descripcionCategoria", categoria.getDescripcionCategoria());

            List<Document> productosDocs = new ArrayList<>();
            if (categoria.getProductos() != null) {
                for (Producto p : categoria.getProductos()) {
                    productosDocs.add(new Document("idProducto", p.getIdProducto())
                            .append("nombreProducto", p.getNombre())
                            .append("precioProducto", p.getPrecio())
                            .append("stockProducto", p.getStock())
                            .append("categoriaProducto", categoria.getNombreCategoria()));
                }
            }

            doc.append("productos", productosDocs);

            coleccionCategoria.insertOne(doc);
            System.out.println(" Categoría guardada en MongoDB: " + categoria.getNombreCategoria());
        } catch (Exception e) {
            System.out.println("️ Error al guardar categoría en MongoDB: " + e.getMessage());
        }
    }


    public List<Categoria> listarCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        try (MongoCursor<Document> cursor = coleccionCategoria.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();

                int id = doc.getInteger("idCategoria");
                String nombre = doc.getString("nombreCategoria");
                String descripcion = doc.getString("descripcionCategoria");

                Categoria categoria = new Categoria(descripcion, id, nombre);

                // Recuperar productos si existen
                List<Document> productosDocs = (List<Document>) doc.get("productos");
                if (productosDocs != null) {
                    for (Document pd : productosDocs) {
                        Producto producto = new Producto(
                                pd.getInteger("idProducto"),
                                pd.getString("nombreProducto"),
                                "", // descripción no se guarda completa aquí
                                pd.getDouble("precioProducto"),
                                pd.getInteger("stockProducto"),
                                "", // fecha vacía
                                doc.getString("nombreCategoria")
                        );
                        categoria.agregarProducto(producto);
                    }
                }

                categorias.add(categoria);
            }
        } catch (Exception e) {
            System.out.println("️ Error al listar categorías: " + e.getMessage());
        }
        return categorias;
    }


    public Categoria buscarCategoriaPorId(int idCategoria) {
        try {
            Document doc = coleccionCategoria.find(eq("idCategoria", idCategoria)).first();
            if (doc != null) {
                Categoria categoria = new Categoria(
                        doc.getString("descripcionCategoria"),
                        doc.getInteger("idCategoria"),
                        doc.getString("nombreCategoria")
                );
                return categoria;
            }
        } catch (Exception e) {
            System.out.println("️ Error al buscar categoría: " + e.getMessage());
        }
        return null;
    }


    public void eliminarCategoria(int idCategoria) {
        try {
            coleccionCategoria.deleteOne(eq("idCategoria", idCategoria));
            System.out.println(" Categoría eliminada con ID: " + idCategoria);
        } catch (Exception e) {
            System.out.println("️ Error al eliminar categoría: " + e.getMessage());
        }
    }


    public void actualizarCategoria(Categoria categoria) {
        try {
            Document actualizacion = new Document("$set", new Document("nombreCategoria", categoria.getNombreCategoria())
                    .append("descripcionCategoria", categoria.getDescripcionCategoria()));
            coleccionCategoria.updateOne(eq("idCategoria", categoria.getIdCategoria()), actualizacion);
            System.out.println(" Categoría actualizada: " + categoria.getNombreCategoria());
        } catch (Exception e) {
            System.out.println("️ Error al actualizar categoría: " + e.getMessage());
        }
    }
}