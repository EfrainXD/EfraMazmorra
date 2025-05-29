package com.efragame.Modelo;

/**
 * Clase que representa las estadísticas de combate de un personaje (protagonista o enemigo).
 */
public class Estadisticas {
    /** Puntos de salud actuales */
    private int salud;
    /** Puntos de fuerza, usados para calcular daño de ataque */
    private int fuerza;
    /** Nivel de defensa, reduce el daño recibido */
    private int defensa;
    /** Nivel de evasión, determina la probabilidad de esquivar ataques */
    private int evasion;
    /** Velocidad del personaje, usada para determinar orden de turnos */
    private int velocidad;

    /**
     * Constructor para inicializar todas las estadísticas del personaje.
     *
     * @param salud     cantidad de salud inicial
     * @param fuerza    valor de fuerza de ataque
     * @param defensa   valor de defensa
     * @param evasion   probabilidad de esquiva
     * @param velocidad velocidad para turnos
     */
    public Estadisticas(int salud, int fuerza, int defensa, int evasion, int velocidad) {
        this.salud = salud;
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.evasion = evasion;
        this.velocidad = velocidad;
    }

    /**
     * @return salud actual del personaje
     */
    public int getSalud() {
        return salud;
    }

    /**
     * Establece la salud del personaje. Nunca será menor que 0.
     *
     * @param salud nueva salud
     */
    public void setSalud(int salud) {
        this.salud = Math.max(0, salud);
    }

    /**
     * @return fuerza del personaje
     */
    public int getFuerza() {
        return fuerza;
    }

    /**
     * @return defensa del personaje
     */
    public int getDefensa() {
        return defensa;
    }

    /**
     * @return evasión del personaje
     */
    public int getEvasion() {
        return evasion;
    }

    /**
     * @return velocidad del personaje
     */
    public int getVelocidad() {
        return velocidad;
    }

    /**
     * Indica si el personaje sigue vivo.
     *
     * @return true si la salud es mayor que 0
     */
    public boolean estaVivo() {
        return salud > 0;
    }

    /**
     * Aplica daño al personaje, considerando su defensa.
     *
     * @param danio cantidad de daño recibido antes de defensa
     */
    public void recibirDanio(int danio) {
        int recibido = Math.max(0, danio - defensa);
        salud = Math.max(0, salud - recibido);
    }
}
