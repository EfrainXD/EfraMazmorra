package com.efragame.Modelo;

/**
 * Clase Singleton que almacena las estadísticas base del personaje protagonista.
 * Estas estadísticas pueden configurarse antes de iniciar el juego y se usan 
 * para inicializar el objeto {@link Prota}.
 */
public class EstadisticasPersonaje {
    /** Instancia única del singleton */
    private static EstadisticasPersonaje instance;

    /** Puntos de salud del personaje */
    private int salud;

    /** Fuerza de ataque del personaje */
    private int fuerza;

    /** Capacidad de defensa del personaje */
    private int defensa;

    /** Probabilidad de esquivar ataques */
    private int evasion;

    /** Velocidad del personaje, usada para determinar turnos */
    private int velocidad;

    /**
     * Constructor privado para evitar instanciación directa.
     * Inicializa las estadísticas con valores por defecto.
     */
    private EstadisticasPersonaje() {
        reset();
    }

    /**
     * Devuelve la instancia única de {@code EstadisticasPersonaje}.
     *
     * @return la instancia singleton
     */
    public static EstadisticasPersonaje getInstance() {
        if (instance == null) {
            instance = new EstadisticasPersonaje();
        }
        return instance;
    }

    /**
     * Restaura las estadísticas a sus valores por defecto.
     */
    public void reset() {
        this.salud = 100;
        this.fuerza = 10;
        this.defensa = 10;
        this.evasion = 5;
        this.velocidad = 10;
    }

    /** @return la salud del personaje */
    public int getSalud() {
        return salud;
    }

    /**
     * Establece la salud del personaje.
     * @param salud nueva cantidad de salud
     */
    public void setSalud(int salud) {
        this.salud = salud;
    }

    /** @return la fuerza del personaje */
    public int getFuerza() {
        return fuerza;
    }

    /**
     * Establece la fuerza del personaje.
     * @param fuerza nueva cantidad de fuerza
     */
    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    /** @return la defensa del personaje */
    public int getDefensa() {
        return defensa;
    }

    /**
     * Establece la defensa del personaje.
     * @param defensa nueva cantidad de defensa
     */
    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    /** @return la evasión del personaje */
    public int getEvasion() {
        return evasion;
    }

    /**
     * Establece la evasión del personaje.
     * @param evasion nuevo valor de evasión
     */
    public void setEvasion(int evasion) {
        this.evasion = evasion;
    }

    /** @return la velocidad del personaje */
    public int getVelocidad() {
        return velocidad;
    }

    /**
     * Establece la velocidad del personaje.
     * @param velocidad nuevo valor de velocidad
     */
    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }
}
