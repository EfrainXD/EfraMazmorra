package com.efragame.Modelo;

/**
 * Representa al protagonista del juego, incluyendo su posición en el mapa y sus estadísticas.
 * 
 * El protagonista se inicializa con estadísticas personalizadas a través del singleton
 * {@link EstadisticasPersonaje}.
 */
public class Prota {
    
    /** Coordenada X del protagonista en el mapa. */
    private int x;
    
    /** Coordenada Y del protagonista en el mapa. */
    private int y;
    
    /** Estadísticas del protagonista (salud, fuerza, defensa, evasión y velocidad). */
    private Estadisticas stats;

    /**
     * Crea un nuevo protagonista en la posición indicada.
     * 
     * Las estadísticas se obtienen del singleton {@code EstadisticasPersonaje}, lo que permite
     * la personalización previa del personaje antes de iniciar la partida.
     *
     * @param x posición X inicial del protagonista
     * @param y posición Y inicial del protagonista
     */
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

    /**
     * Obtiene la coordenada X actual del protagonista.
     *
     * @return la posición X
     */
    public int getX() { 
        return x; 
    }

    /**
     * Obtiene la coordenada Y actual del protagonista.
     *
     * @return la posición Y
     */
    public int getY() { 
        return y; 
    }

    /**
     * Establece una nueva posición (X, Y) para el protagonista.
     *
     * @param x nueva coordenada X
     * @param y nueva coordenada Y
     */
    public void setPosicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Devuelve las estadísticas actuales del protagonista.
     *
     * @return objeto {@link Estadisticas} con los valores actuales
     */
    public Estadisticas getEstadisticas() { 
        return stats; 
    }

    /**
     * Verifica si el protagonista sigue con vida.
     *
     * @return {@code true} si su salud es mayor a cero; {@code false} en caso contrario
     */
    public boolean estaVivo() {
        return stats.estaVivo();
    }

    /**
     * Ataca a un enemigo, aplicando daño basado en la fuerza del protagonista.
     *
     * @param enemigo enemigo al que se desea atacar
     */
    public void atacar(Enemigo enemigo) {
        enemigo.getEstadisticas().recibirDanio(stats.getFuerza());
    }
}
