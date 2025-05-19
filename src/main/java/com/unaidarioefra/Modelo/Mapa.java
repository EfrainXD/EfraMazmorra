package com.unaidarioefra.Modelo;

public class Mapa {
    private char[][] celdas;

    public Mapa(char[][] celdas) {
        this.celdas = celdas;
    }

    public int getAncho() {
        return celdas[0].length;
    }

    public int getAlto() {
        return celdas.length;
    }

    public char getCelda(int x, int y) {
        return celdas[y][x];
    }

    public boolean esMuro(int x, int y) {
        return getCelda(x, y) == '#';
    }

    public void setCelda(int x, int y, char valor) {
        celdas[y][x] = valor;
    }

    public char[][] getCeldas() {
        return celdas;
    }
}