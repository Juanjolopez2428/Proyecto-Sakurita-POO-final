package Produccion;

import OperacionesOcultas.TrabajadorEsclavizado;
import OperacionesOcultas.RegistroEsclavos;
import Ventas.Producto;

import java.util.List;
import java.util.ArrayList;

import java.lang.ref.PhantomReference;

public class Fabrica {
    private int idFabrica;
    private String pais;
    private  String ciudad;
    private int capacidad;
    private String nivelAutomatizacion;
    private List<TrabajadorEsclavizado> personal;


    public Fabrica(int idFabrica, String pais, String ciudad, int capacidad, String nivelAutomatizacion) {
        this.idFabrica = idFabrica;
        this.pais = pais;
        this.ciudad = ciudad;
        this.capacidad = capacidad;
        this.nivelAutomatizacion = nivelAutomatizacion;
        this.personal = new ArrayList<>();
    }

    public int getIdFabrica() {
        return idFabrica;
    }

    public void setIdFabrica(int idFabrica) {
        this.idFabrica = idFabrica;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getNivelAutomatizacion() {
        return nivelAutomatizacion;
    }

    public void setNivelAutomatizacion(String nivelAutomatizacion) {
        this.nivelAutomatizacion = nivelAutomatizacion;
    }

    public void agregarPersonal(TrabajadorEsclavizado trabajador) {
        this.personal.add(trabajador);
        trabajador.setAsignadoA(this);
    }

    public List<TrabajadorEsclavizado> getPersonal() {
        return personal;
    }


    public void asignarTrabajador(TrabajadorEsclavizado t) {
        personal.add(t);
    }
}
