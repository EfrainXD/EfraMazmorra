package com.efragame.Modelo;

/**
 * Clase que representa el mapa del juego como una matriz de caracteres.
 * Cada celda del mapa puede representar distintos elementos del escenario,
 * como suelo, muros u otros elementos especiales.
 */
public class Mapa {

    /** Matriz de caracteres que representa las celdas del mapa. */
    private char[][] celdas;

    /**
     * Crea un nuevo mapa con las celdas especificadas.
     *
     * @param celdas matriz bidimensional de caracteres que representa el mapa
     */
    public Mapa(char[][] celdas) {
        this.celdas = celdas;
    }

    /**
     * Obtiene el ancho del mapa (número de columnas).
     *
     * @return ancho del mapa
     */
    public int getAncho() {
        return celdas[0].length;
    }

    /**
     * Obtiene el alto del mapa (número de filas).
     *
     * @return alto del mapa
     */
    public int getAlto() {
        return celdas.length;
    }

    /**
     * Devuelve el carácter correspondiente a la celda en la posición (x, y).
     *
     * @param x coordenada X de la celda
     * @param y coordenada Y de la celda
     * @return carácter que representa el contenido de la celda
     */
    public char getCelda(int x, int y) {
        return celdas[y][x];
    }

    /**
     * Indica si la celda en la posición (x, y) es un muro.
     * Se considera muro si el carácter es '#'.
     *
     * @param x coordenada X de la celda
     * @param y coordenada Y de la celda
     * @return {@code true} si la celda es un muro; {@code false} en caso contrario
     */
    public boolean esMuro(int x, int y) {
        return getCelda(x, y) == '#';
    }

    /**
     * Modifica el valor de la celda en la posición (x, y).
     *
     * @param x coordenada X de la celda
     * @param y coordenada Y de la celda
     * @param valor nuevo carácter que representará la celda
     */
    public void setCelda(int x, int y, char valor) {
        celdas[y][x] = valor;
    }

    /**
     * Devuelve la matriz completa de celdas del mapa.
     *
     * @return matriz bidimensional de caracteres del mapa
     */
    public char[][] getCeldas() {
        return celdas;
    }
}
