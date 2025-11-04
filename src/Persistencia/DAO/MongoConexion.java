package Persistencia.DAO;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConexion{
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    private static final String URI = "mongodb+srv://Sakura_User:Sakura1234@sakurita.tabunec.mongodb.net/?appName=Sakurita";
    private static final String DB_NAME = "Proyecto0";

    public static MongoDatabase conectar() {
        if (mongoClient == null) {
            try {
                mongoClient = MongoClients.create(URI);
                database = mongoClient.getDatabase(DB_NAME);
                System.out.println(" Conectado correctamente a MongoDB Atlas (Sakurita).");
            } catch (Exception e) {
                System.err.println(" Error al conectar con MongoDB Atlas: " + e.getMessage());
            }
        }
        return database;
    }

    public static void cerrarConexion() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            System.out.println(" Conexión cerrada con MongoDB Atlas.");
        }
    }
}
