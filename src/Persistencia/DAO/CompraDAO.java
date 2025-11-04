package Persistencia.DAO;

import Ventas.Compra;
import Ventas.LineaCompra;
import Ventas.MetodoPago;
import Ventas.Cliente;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO {
    private final MongoCollection<Document> coleccion;


    public CompraDAO(MongoDatabase db) {
        this.coleccion = db.getCollection("compras");
    }

    public void insertarCompra(Compra compra) {
        List<Document> lineas = new ArrayList<>();
        for (LineaCompra lc : compra.getLineasCompra()) {
            Document lineaDoc = new Document("productoId", lc.getProducto().getIdProducto())
                    .append("nombreProducto", lc.getProducto().getNombre())
                    .append("cantidad", lc.getCantidad())
                    .append("precioUnitario", lc.getProducto().getPrecio());
            lineas.add(lineaDoc);
        }

        Document clienteDoc = new Document("id", compra.getCliente().getIdUsuario())
                .append("nombre", compra.getCliente().getNombreUsuario())
                .append("email", compra.getCliente().getEmailUsuario());

        Document metodoPagoDoc = new Document("tipo", compra.getMetodoPago().getTipo());

        Document doc = new Document("idCompra", compra.getIdVenta())
                .append("fecha", compra.getFechaVenta().toString())
                .append("cliente", clienteDoc)
                .append("metodoPago", metodoPagoDoc)
                .append("total", compra.getTotalVenta())
                .append("lineas", lineas);

        coleccion.insertOne(doc);
        System.out.println(" Compra registrada en MongoDB (ID=" + compra.getIdVenta() + ")");
    }

    public List<Compra> obtenerTodas() {
        List<Compra> compras = new ArrayList<>();
        for (Document doc : coleccion.find()) {
            Document clienteDoc = (Document) doc.get("cliente");
            Cliente cliente = new Cliente(
                    clienteDoc.getInteger("id"),
                    clienteDoc.getString("email"),
                    "",
                    "CLIENTE",
                    java.time.LocalDate.now(),
                    "ACTIVA",
                    "N/A",
                    0,
                    clienteDoc.getString("nombre")
            );

            Compra compra = new Compra(
                    doc.getInteger("idCompra"),
                    cliente,
                    new MetodoPago(((Document) doc.get("metodoPago")).getString("tipo"))
            );
            compra.setTotalVenta(doc.getDouble("total"));
            compras.add(compra);
        }
        return compras;
    }
}