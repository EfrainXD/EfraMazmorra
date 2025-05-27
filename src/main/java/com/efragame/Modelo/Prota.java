package com.efragame.Modelo;

public class Prota {
    private int x, y;
    private Estadisticas stats;

    public Prota(int x, int y) {
        this.x = x;
        this.y = y;
        
        // Obtener las estadísticas personalizadas del singleton
        EstadisticasPersonaje estatPers = EstadisticasPersonaje.getInstance();
        this.stats = new Estadisticas(
            estatPers.getSalud(),
            estatPers.getFuerza(),
            estatPers.getDefensa(),
            estatPers.getEvasion(),
            estatPers.getVelocidad()
        );
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