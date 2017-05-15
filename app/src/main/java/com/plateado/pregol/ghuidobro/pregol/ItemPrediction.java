package com.plateado.pregol.ghuidobro.pregol;

/**
 * Created by ghuidobro on 5/15/17.
 */

public class ItemPrediction {

    public static final int EMPATE = 0;
    public static final int LOCAL = 1;
    public static final int VISITA = 2;
    public static final int SIN_ESTADO = 3;

    public PGFixture getPgFixture() {
        return pgFixture;
    }

    public void setPgFixture(PGFixture pgFixture) {
        this.pgFixture = pgFixture;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    private PGFixture pgFixture;
    private int estado;
}
