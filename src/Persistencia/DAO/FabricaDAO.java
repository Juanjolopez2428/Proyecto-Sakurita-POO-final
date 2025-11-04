package Persistencia.DAO;

import Produccion.Fabrica;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.List;

public class FabricaDAO {
    private final MongoCollection<Document> coleccion;

    public FabricaDAO(MongoDatabase db) {
        this.coleccion = db.getCollection("fabricas");
    }

    public void insertarFabrica(Fabrica fabrica) {
        Document doc = new Document("idFabrica", fabrica.getIdFabrica())
                .append("pais", fabrica.getPais())
                .append("ciudad", fabrica.getCiudad())
                .append("capacidad", fabrica.getCapacidad())
                .append("nivelAutomatizacion", fabrica.getNivelAutomatizacion());
        coleccion.insertOne(doc);
        System.out.println(" Fábrica registrada en DB: " + fabrica.getPais() + " - " + fabrica.getCiudad());
    }

    public Fabrica buscarPorId(int idFabrica) {
        Document doc = coleccion.find(eq("idFabrica", idFabrica)).first();
        if (doc == null) return null;

        return new Fabrica(
                doc.getInteger("idFabrica"),
                doc.getString("pais"),
                doc.getString("ciudad"),
                doc.getInteger("capacidad"),
                doc.getString("nivelAutomatizacion")
        );
    }

    public List<Fabrica> obtenerTodas() {
        List<Fabrica> fabricas = new ArrayList<>();
        for (Document doc : coleccion.find()) {
            fabricas.add(new Fabrica(
                    doc.getInteger("idFabrica"),
                    doc.getString("pais"),
                    doc.getString("ciudad"),
                    doc.getInteger("capacidad"),
                    doc.getString("nivelAutomatizacion")
            ));
        }
        return fabricas;
    }
}