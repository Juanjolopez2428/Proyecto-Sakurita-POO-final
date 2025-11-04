package Persistencia.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;

import Usuarios.*;
import Excepciones.UsuarioInvalidoException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private final MongoCollection<Document> coleccion;

    public UsuarioDAO(MongoDatabase db) {
        this.coleccion = db.getCollection("usuarios");
    }

    // Insertar Usuario

    public void insertarUsuario(Usuario usuario) {
        try {
            Document doc = new Document()
                    .append("idUsuario", usuario.getIdUsuario())
                    .append("emailUsuario", usuario.getEmailUsuario())
                    .append("contrasena", usuario.getContrasena())
                    .append("rol", usuario.getRol())
                    .append("fechaRegistro", usuario.getFechaRegistro().toString())
                    .append("estadoCuenta", usuario.getEstadoCuenta())
                    .append("nombreUsuario", usuario.getNombreUsuario());

            coleccion.insertOne(doc);
            System.out.println(" Usuario guardado en MongoDB: " + usuario.getNombreUsuario());
        } catch (Exception e) {
            System.err.println("Error al guardar usuario en MongoDB: " + e.getMessage());
        }
    }


    // Buscar Usuario por Email

    public Usuario buscarPorEmail(String email) {
        try {
            if (email == null || email.isEmpty()) {
                throw new UsuarioInvalidoException("El email es inválido.");
            }

            Document doc = coleccion.find(eq("emailUsuario", email)).first();
            if (doc == null) return null;

            String rol = doc.getString("rol");
            String contrasenaDB = doc.getString("contrasena");
            Usuario usuario;

            switch (rol) {
                case "AdministradorContenido" -> usuario = new AdministradorContenido(
                        doc.getInteger("idUsuario"),
                        doc.getString("emailUsuario"),
                        contrasenaDB,
                        rol,
                        LocalDate.parse(doc.getString("fechaRegistro")),
                        doc.getString("estadoCuenta"),
                        doc.getString("nombreUsuario")
                );

                case "AdministradorUsuarios" -> usuario = new AdministradorUsuarios(
                        doc.getInteger("idUsuario"),
                        doc.getString("emailUsuario"),
                        contrasenaDB,
                        rol,
                        LocalDate.parse(doc.getString("fechaRegistro")),
                        doc.getString("estadoCuenta"),
                        doc.getString("nombreUsuario")
                );

                case "DUENA", "Dueña" -> usuario = new Duena(
                        doc.getInteger("idUsuario"),
                        doc.getString("emailUsuario"),
                        contrasenaDB,
                        doc.getString("nombreUsuario"),
                        contrasenaDB
                );

                default -> usuario = new UsuarioNormal(
                        doc.getInteger("idUsuario"),
                        doc.getString("emailUsuario"),
                        contrasenaDB,
                        "Normal",
                        LocalDate.parse(doc.getString("fechaRegistro")),
                        doc.getString("estadoCuenta"),
                        doc.getString("nombreUsuario")
                );
            }

            return usuario;

        } catch (UsuarioInvalidoException e) {
            System.err.println(" Error al buscar usuario: " + e.getMessage());
        } catch (Exception e) {
            System.err.println(" Error general al buscar usuario: " + e.getMessage());
        }
        return null;
    }


    // Listar Todos los Usuarios

    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        try {
            for (Document doc : coleccion.find()) {
                String rol = doc.getString("rol");
                String contrasenaDB = doc.getString("contrasena");
                Usuario usuario;

                switch (rol) {
                    case "AdministradorContenido" -> usuario = new AdministradorContenido(
                            doc.getInteger("idUsuario"),
                            doc.getString("emailUsuario"),
                            contrasenaDB,
                            rol,
                            LocalDate.parse(doc.getString("fechaRegistro")),
                            doc.getString("estadoCuenta"),
                            doc.getString("nombreUsuario")
                    );

                    case "AdministradorUsuarios" -> usuario = new AdministradorUsuarios(
                            doc.getInteger("idUsuario"),
                            doc.getString("emailUsuario"),
                            contrasenaDB,
                            rol,
                            LocalDate.parse(doc.getString("fechaRegistro")),
                            doc.getString("estadoCuenta"),
                            doc.getString("nombreUsuario")
                    );

                    case "DUENA", "Dueña" -> usuario = new Duena(
                            doc.getInteger("idUsuario"),
                            doc.getString("emailUsuario"),
                            contrasenaDB,
                            doc.getString("nombreUsuario"),
                            contrasenaDB
                    );

                    default -> usuario = new UsuarioNormal(
                            doc.getInteger("idUsuario"),
                            doc.getString("emailUsuario"),
                            contrasenaDB,
                            "Normal",
                            LocalDate.parse(doc.getString("fechaRegistro")),
                            doc.getString("estadoCuenta"),
                            doc.getString("nombreUsuario")
                    );
                }
                lista.add(usuario);
            }
        } catch (Exception e) {
            System.err.println("️ Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }
}