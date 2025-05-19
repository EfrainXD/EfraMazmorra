package com.unaidarioefra.Modelo;

public class Enemigo {
    private int x, y;
    private EnemigoTipo tipo;
    private Estadisticas stats;

    public Enemigo(int x, int y, EnemigoTipo tipo) {
        this.x = x;
        this.y = y;
        this.tipo = tipo;
        this.stats = new Estadisticas(tipo.getSalud(), tipo.getAtaque(), 0, 0, 5);
    }

    public int getX() {
        return x; 
    }
    public int getY() { 
        return y; 
    }

    public void setPosicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public EnemigoTipo getTipo() {
        return tipo; 
    }
    public Estadisticas getEstadisticas() { 
        return stats; 
    }

    public boolean estaVivo() {
        return stats.estaVivo();
    }

    public boolean enRangoDe(Prota prota) {
        int dx = Math.abs(prota.getX() - x);
        int dy = Math.abs(prota.getY() - y);
        return dx + dy <= tipo.getPercepcion();
    }

    public void atacar(Prota prota) {
        prota.getEstadisticas().recibirDanio(stats.getFuerza());
    }

    public int getSalud() {
        return stats.getSalud();
    }
    public void setSalud(int salud) {
        stats.setSalud(salud);
    }
}