package OperacionesOcultas;

import java.util.List;
import java.util.ArrayList;

public class RegistroEsclavos{
    private String ultimoAcceso;
    private String nivelCifrado;
    private List<TrabajadorEsclavizado> listaTrabajadores;

    public RegistroEsclavos(String ultimoAcceso, String nivelCifrado, List<TrabajadorEsclavizado> listaTrabajadores) {
        this.ultimoAcceso = ultimoAcceso;
        this.nivelCifrado = nivelCifrado;
        this.listaTrabajadores = new ArrayList<>();
    }

    public RegistroEsclavos() {

    }

    public void registrarEsclavo(TrabajadorEsclavizado nuevoTrabajador) {
        this.listaTrabajadores.add(nuevoTrabajador);
        System.out.println("Trabajador ID " + nuevoTrabajador.getIdTrabajador() + " registrado exitosamente.");
    }

    public List<TrabajadorEsclavizado> obtenerTodosLosTrabajadores() {
        return this.listaTrabajadores;
    }

    public void registrarTrabajador(TrabajadorEsclavizado t) {
        listaTrabajadores.add(t);
    }
}
