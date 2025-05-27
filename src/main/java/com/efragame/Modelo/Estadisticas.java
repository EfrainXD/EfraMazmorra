package com.efragame.Modelo;

public class Estadisticas {
    private int salud, fuerza, defensa, evasion, velocidad;

    public Estadisticas(int salud, int fuerza, int defensa, int evasion, int velocidad) {
        this.salud = salud;
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.evasion = evasion;
        this.velocidad = velocidad;
    }

    public int getSalud() { 
        return salud; 
    }
    public void setSalud(int salud) { 
        this.salud = Math.max(0, salud); 
    }

    public int getFuerza() { 
        return fuerza; 
    }
    public int getDefensa() { 
        return defensa; 
    }
    public int getEvasion() { 
        return evasion; 
    }
    public int getVelocidad() { 
        return velocidad; 
    }

    public boolean estaVivo() {
        return salud > 0;
    }

    public void recibirDanio(int danio) {
        int recibido = Math.max(0, danio - defensa);
        salud = Math.max(0, salud - recibido);
    }
}