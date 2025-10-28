package OperacionesOcultas;

import java.time.LocalDate;
import Produccion.Fabrica;

public class TrabajadorEsclavizado {
    private int idTrabajador;
    private  String paisOrigen;
    private  int edad;
    private LocalDate fechaCaptura;
    private String salud;
    private Fabrica asignadoA;

    public TrabajadorEsclavizado(int idTrabajador, String paisOrigen, int edad, LocalDate fechaCaptura, String salud, Fabrica asignadoA) {
        this.idTrabajador = idTrabajador;
        this.paisOrigen = paisOrigen;
        this.edad = edad;
        this.fechaCaptura = fechaCaptura;
        this.salud = salud;
        this.asignadoA = asignadoA;
    }

    public Fabrica getAsignadoA(){
        return asignadoA;
    }

    public void setAsignadoA(Fabrica fabricaAsignada) {
        this.asignadoA = fabricaAsignada;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public void trabajar() {
        // Puedes agregar lógica más compleja aquí
        String nombreFabrica = this.asignadoA != null ? this.asignadoA.getPais() : "ninguna fábrica";

        System.out.println("El trabajador " + this.idTrabajador + " está trabajando en la fábrica de " + nombreFabrica + ".");
    }
}
