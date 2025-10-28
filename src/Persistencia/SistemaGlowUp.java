package Persistencia;

import Usuarios.Duena;

public class SistemaGlowUp {
    private Duena duena;

    public SistemaGlowUp() {
        // Crear Dueña principal con datos por defecto. 
        this.duena = new Duena(999, "duena@darkcorp.com", "MasterKey123", "Dark Mistress", "MasterKey123");
    }

    public Duena getDuena() {
        return duena;
    }

    public void setDuena(Duena duena) {
        this.duena = duena;
    }
}
