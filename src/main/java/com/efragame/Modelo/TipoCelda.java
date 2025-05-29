package com.efragame.Modelo;

/**
 * Enumeración que representa los distintos tipos de celdas posibles en el mapa del juego.
 *
 * Las celdas pueden ser de tipo {@code MURO}, que son obstáculos infranqueables, o {@code SUELO},
 * que representan espacios transitables por el protagonista y enemigos.
 */
public enum TipoCelda {
    
    /** Celda que representa un muro. No se puede atravesar. */
    MURO,
    
    /** Celda que representa suelo libre. Es transitable. */
    SUELO
}
