package com.efragame.Modelo;

/**
 * Representa a un enemigo en el juego con posición, tipo y estadísticas.
 */
public class Enemigo {
    private int x, y;
    private EnemigoTipo tipo;
    private Estadisticas stats;

    /**
     * Constructor para crear un enemigo en una posición dada y de un tipo específico.
     * 
     * @param x    coordenada X en el mapa
     * @param y    coordenada Y en el mapa
     * @param tipo tipo de enemigo (esbirro, esqueleto, zombie, etc.)
     */
    public Enemigo(int x, int y, EnemigoTipo tipo) {
        this.x = x;
        this.y = y;
        this.tipo = tipo;
        this.stats = new Estadisticas(tipo.getSalud(), tipo.getAtaque(), 0, 0, 5);
    }

    /**
     * @return la coordenada X actual del enemigo
     */
    public int getX() {
        return x;
    }

    /**
     * @return la coordenada Y actual del enemigo
     */
    public int getY() {
        return y;
    }

    /**
     * Actualiza la posición del enemigo en el mapa.
     * 
     * @param x nueva coordenada X
     * @param y nueva coordenada Y
     */
    public void setPosicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return el tipo de enemigo
     */
    public EnemigoTipo getTipo() {
        return tipo;
    }

    /**
     * @return las estadísticas del enemigo
     */
    public Estadisticas getEstadisticas() {
        return stats;
    }

    /**
     * Indica si el enemigo sigue vivo (salud > 0).
     * 
     * @return true si el enemigo está vivo, false en caso contrario
     */
    public boolean estaVivo() {
        return stats.estaVivo();
    }

    /**
     * Comprueba si el protagonista está dentro del rango de percepción del enemigo.
     * 
     * @param prota el protagonista a comprobar
     * @return true si el protagonista está dentro del rango, false si no
     */
    public boolean enRangoDe(Prota prota) {
        int dx = Math.abs(prota.getX() - x);
        int dy = Math.abs(prota.getY() - y);
        return dx + dy <= tipo.getPercepcion();
    }

    /**
     * El enemigo ataca al protagonista, infligiendo daño según su fuerza.
     * 
     * @param prota el protagonista atacado
     */
    public void atacar(Prota prota) {
        prota.getEstadisticas().recibirDanio(stats.getFuerza());
    }

    /**
     * @return la salud actual del enemigo
     */
    public int getSalud() {
        return stats.getSalud();
    }

    /**
     * Establece la salud actual del enemigo.
     * 
     * @param salud nuevo valor de salud
     */
    public void setSalud(int salud) {
        stats.setSalud(salud);
    }
}
