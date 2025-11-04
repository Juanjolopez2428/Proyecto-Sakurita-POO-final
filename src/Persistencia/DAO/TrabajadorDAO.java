package Persistencia.DAO;

import OperacionesOcultas.TrabajadorEsclavizado;
import Produccion.Fabrica;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

;

public class TrabajadorDAO {
    private final MongoCollection<Document> coleccion;
    private final FabricaDAO fabricaDAO;

    public TrabajadorDAO(MongoDatabase db, FabricaDAO fabricaDAO) {
        this.coleccion = db.getCollection("trabajadores");
        this.fabricaDAO = fabricaDAO;
    }

    public void insertarTrabajador(TrabajadorEsclavizado trabajador) {
        Document doc = new Document("idTrabajador", trabajador.getIdTrabajador())
                .append("paisOrigen", trabajador.getPaisOrigen())
                .append("edad", trabajador.getEdad())
                .append("fechaCaptura", trabajador.getFechaCaptura().toString())
                .append("salud", trabajador.getSalud())
                .append("idFabricaAsignada", trabajador.getAsignadoA().getIdFabrica());

        coleccion.insertOne(doc);
        System.out.println(" Trabajador ID " + trabajador.getIdTrabajador() + " registrado en la base de datos.");
    }

    public List<TrabajadorEsclavizado> obtenerTodos() {
        List<TrabajadorEsclavizado> trabajadores = new ArrayList<>();
        for (Document doc : coleccion.find()) {
            Fabrica fabricaAsignada = fabricaDAO.buscarPorId(doc.getInteger("idFabricaAsignada"));
            LocalDate fechaCaptura = LocalDate.parse(doc.getString("fechaCaptura"));

            TrabajadorEsclavizado t = new TrabajadorEsclavizado(
                    doc.getInteger("idTrabajador"),
                    doc.getString("paisOrigen"),
                    doc.getInteger("edad"),
                    fechaCaptura,
                    doc.getString("salud"),
                    fabricaAsignada
            );
            trabajadores.add(t);
        }
        return trabajadores;
    }
}