package Persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class Persistencia {

    //  Se agrega adaptador para LocalDate
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting()
            .create();

    // --- Guardar datos en archivo JSON ---
    public static <T> void guardar(String ruta, T objeto) {
        try (FileWriter writer = new FileWriter(ruta)) {
            gson.toJson(objeto, writer);
            System.out.println(" Datos guardados en " + ruta);
        } catch (IOException e) {
            System.err.println(" Error al guardar datos en " + ruta + ": " + e.getMessage());
        }
    }

    // --- Cargar datos desde archivo JSON ---
    public static <T> T cargar(String ruta, Class<T> tipo) {
        try (FileReader reader = new FileReader(ruta)) {
            return gson.fromJson(reader, tipo);
        } catch (IOException e) {
            System.out.println("(Inicialización) No se encontró el archivo o no se pudo leer: " + ruta);
            return null;
        }
    }
}
