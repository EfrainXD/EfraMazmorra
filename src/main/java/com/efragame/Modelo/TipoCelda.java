package com.efragame.Modelo;

/**
 * Enum que representa los diferentes tipos de celdas en el mapa del juego.
 */
public enum TipoCelda {
    /** Celda de suelo normal por la que se puede caminar */
    SUELO('.'),
    /** Celda de pared que bloquea el movimiento */
    PARED('#'),
    MALDICION('M');

    /** Carácter que representa este tipo de celda en el archivo del mapa */
    private final char caracter;

    /**
     * Constructor para asociar un carácter con cada tipo de celda.
     * 
     * @param caracter el carácter que representa este tipo en el mapa
     */
    TipoCelda(char caracter) {
        this.caracter = caracter;
    }

    /**
     * @return el carácter asociado a este tipo de celda
     */
    public char getCaracter() {
        return caracter;
    }

    /**
     * Obtiene el tipo de celda correspondiente a un carácter dado.
     * 
     * @param caracter el carácter a convertir
     * @return el tipo de celda correspondiente, o SUELO si no se reconoce
     */
    public static TipoCelda fromCaracter(char caracter) {
        for (TipoCelda tipo : values()) {
            if (tipo.caracter == caracter) {
                return tipo;
            }
        }
        return SUELO; // Por defecto
    }
}