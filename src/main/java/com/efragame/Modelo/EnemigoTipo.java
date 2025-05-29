package com.efragame.Modelo;

/**
 * Enum que representa los tipos de enemigos con sus estadísticas básicas.
 */
public enum EnemigoTipo {
    /** Enemigo tipo esbirro con salud, ataque y percepción definidos */
    ESBIRRO(40, 16, 3),
    /** Enemigo tipo esqueleto con salud, ataque y percepción definidos */
    ESQUELETO(60, 18, 4),
    /** Enemigo tipo zombie con salud, ataque y percepción definidos */
    ZOMBIE(80, 20, 2);

    /** Salud inicial del enemigo */
    private final int salud;
    /** Ataque base del enemigo */
    private final int ataque;
    /** Percepción o rango de detección del enemigo */
    private final int percepcion;

    /**
     * Constructor para inicializar las estadísticas de cada tipo de enemigo.
     * 
     * @param salud      puntos de salud
     * @param ataque     valor de ataque
     * @param percepcion rango de percepción (detección)
     */
    EnemigoTipo(int salud, int ataque, int percepcion) {
        this.salud = salud;
        this.ataque = ataque;
        this.percepcion = percepcion;
    }

    /**
     * @return la salud inicial del enemigo
     */
    public int getSalud() {
        return salud;
    }

    /**
     * @return el valor de ataque del enemigo
     */
    public int getAtaque() {
        return ataque;
    }

    /**
     * @return la percepción o rango de detección del enemigo
     */
    public int getPercepcion() {
        return percepcion;
    }
}
