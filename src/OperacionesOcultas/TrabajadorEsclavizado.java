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

        String nombreFabrica = this.asignadoA != null ? this.asignadoA.getPais() : "ninguna fábrica";

        System.out.println("El trabajador " + this.idTrabajador + " está trabajando en la fábrica de " + nombreFabrica + ".");
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public LocalDate getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(LocalDate fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public String getSalud() {
        return salud;
    }

    public void setSalud(String salud) {
        this.salud = salud;
    }
}
