package OperacionesOcultas;
import java.util.ArrayList;
import java.util.List;
import Usuarios.MiembroConsejo;

public class ConsejoSombrio {
    private int idConsejo;
    private  String nombreClave;
    private List<MiembroConsejo> miembros;

    public ConsejoSombrio(int idConsejo, String nombreClave) {
        this.idConsejo = idConsejo;
        this.nombreClave = nombreClave;
        // Inicializar la lista
        this.miembros = new ArrayList<>();
    }

    public ConsejoSombrio() {

    }

    public void agregarMiembro(MiembroConsejo nuevoMiembro) {
        this.miembros.add(nuevoMiembro);
        System.out.println("Nuevo miembro añadido al Consejo Sombrio.");
    }

    public List<MiembroConsejo> getMiembros() {
        return miembros;
    }
}
