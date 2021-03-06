package com.plateado.pregol.ghuidobro.pregol;

import java.io.Serializable;

public class PGFixture implements Serializable {

    private static final long serialVersionUID = 1L;

    private String equipoLocal;
    private String equipoVisita;
    private String imagenEquipoLocal;
    private String imagenEquipoVisita;
    private String idPartido;


    public PGFixture(String equipoLocal, String equipoVisita, String imagenEquipoLocal, String imagenEquipoVisita, String idPartido) {
        this.equipoLocal = equipoLocal;
        this.equipoVisita = equipoVisita;
        this.imagenEquipoLocal = imagenEquipoLocal;
        this.imagenEquipoVisita = imagenEquipoVisita;
        this.idPartido = idPartido;
    }

    public String getImagenEquipoLocal() {
        return imagenEquipoLocal;
    }

    public void setImagenEquipoLocal(String imagenEquipoLocal) {
        this.imagenEquipoLocal = imagenEquipoLocal;
    }

    public String getImagenEquipoVisita() {
        return imagenEquipoVisita;
    }

    public void setImagenEquipoVisita(String imagenEquipoVisita) {
        this.imagenEquipoVisita = imagenEquipoVisita;
    }

    public String getEquipoLocal() {
        return equipoLocal;
    }

    public void setEquipoLocal(String equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    public String getEquipoVisita() {
        return equipoVisita;
    }

    public void setEquipoVisita(String equipoVisita) {
        this.equipoVisita = equipoVisita;
    }

    public String getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(String idPartido) {
        this.idPartido = idPartido;
    }
}
