package com.efragame;

public class EstadisticasPersonaje {
    private static EstadisticasPersonaje instance;
    
    private int salud = 100;
    private int fuerza = 10;
    private int defensa = 10;
    private int evasion = 5;
    private int velocidad = 10;
    
    private EstadisticasPersonaje() {}
    
    public static EstadisticasPersonaje getInstance() {
        if (instance == null) {
            instance = new EstadisticasPersonaje();
        }
        return instance;
    }
    
    // Getters y setters
    public int getSalud() {
        return salud;
    }
    
    public void setSalud(int salud) {
        this.salud = salud;
    }
    
    public int getFuerza() {
        return fuerza;
    }
    
    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }
    
    public int getDefensa() {
        return defensa;
    }
    
    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }
    
    public int getEvasion() {
        return evasion;
    }
    
    public void setEvasion(int evasion) {
        this.evasion = evasion;
    }
    
    public int getVelocidad() {
        return velocidad;
    }
}