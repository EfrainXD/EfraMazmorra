package com.unaidarioefra.Modelo;

public class Prota {
    private int x, y;
    private Estadisticas stats;

    public Prota(int x, int y) {
        this.x = x;
        this.y = y;
        this.stats = new Estadisticas(100, 20, 15, 5, 10);
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

    public Estadisticas getEstadisticas() { 
        return stats; 
    }

    public boolean estaVivo() {
        return stats.estaVivo();
    }

    public void atacar(Enemigo enemigo) {
        enemigo.getEstadisticas().recibirDanio(stats.getFuerza());
    }
}