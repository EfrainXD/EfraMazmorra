package com.unaidarioefra.Modelo;

public enum EnemigoTipo {
    ESBIRRO(40, 16, 3),
    ESQUELETO(60, 18, 4),
    ZOMBIE(80, 20, 2);

    private final int salud, ataque, percepcion;

    EnemigoTipo(int salud, int ataque, int percepcion) {
        this.salud = salud;
        this.ataque = ataque;
        this.percepcion = percepcion;
    }

    public int getSalud() {
        return salud; 
    }
    public int getAtaque() {
        return ataque; 
    }
    public int getPercepcion() { 
        return percepcion; 
    } 
}