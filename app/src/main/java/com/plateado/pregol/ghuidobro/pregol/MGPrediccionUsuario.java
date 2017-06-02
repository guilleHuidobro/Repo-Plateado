package com.plateado.pregol.ghuidobro.pregol;

import java.io.Serializable;

public class MGPrediccionUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fechaFixture;
    private String idPartido;
    private String ususario;
    private String resultadoPrediccion;


    public MGPrediccionUsuario(String ususario, String resultadoPrediccion, String fechaFixture, String idPartido) {
        this.ususario = ususario;
        this.resultadoPrediccion = resultadoPrediccion;
        this.fechaFixture = fechaFixture;
        this.idPartido = idPartido;
    }

    public String getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(String idPartido) {
        this.idPartido = idPartido;
    }

    public String getUsusario() {
        return ususario;
    }

    public void setUsusario(String ususario) {
        this.ususario = ususario;
    }

    public String getResultadoPrediccion() {
        return resultadoPrediccion;
    }

    public void setResultadoPrediccion(String resultadoPrediccion) {
        this.resultadoPrediccion = resultadoPrediccion;
    }

    public String getFechaFixture() {
        return fechaFixture;
    }

    public void setFechaFixture(String fechaFixture) {
        this.fechaFixture = fechaFixture;
    }


}
