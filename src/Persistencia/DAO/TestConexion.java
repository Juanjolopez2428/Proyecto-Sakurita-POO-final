package Persistencia.DAO;


import com.mongodb.client.MongoDatabase;

public class TestConexion {
    public static void main(String[] args) {
        MongoDatabase db = MongoConexion.conectar();
        System.out.println("Base de datos actual: " + db.getName());
        MongoConexion.cerrarConexion();
    }
}
