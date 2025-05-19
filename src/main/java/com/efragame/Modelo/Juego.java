package com.efragame.Modelo;

import java.io.*;
import java.util.*;

public class Juego {
    private char[][] mapa;
    private Prota prota;
    private List<Enemigo> enemigos;

    public Juego(String mapaPath, String enemigosPath) throws IOException {
        cargarMapa(mapaPath);
        cargarEnemigos(enemigosPath);
        this.prota = new Prota(1, 1); // posición inicial
    }

    private void cargarMapa(String path) throws IOException {
        List<char[]> filas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                filas.add(linea.toCharArray());
            }
        }
        mapa = filas.toArray(new char[0][]);
    }

    private void cargarEnemigos(String path) throws IOException {
        enemigos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // saltar header
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                String tipoStr = partes[0];
                int x = Integer.parseInt(partes[1]);
                int y = Integer.parseInt(partes[2]);
                EnemigoTipo tipo = EnemigoTipo.valueOf(tipoStr.toUpperCase());
                enemigos.add(new Enemigo(x, y, tipo));
            }
        }
    }

    public char[][] getMapa() { 
        return mapa; 
    }
    public Prota getProta() { 
        return prota; 
    }
    public List<Enemigo> getEnemigos() { 
        return enemigos; 
    }

    public boolean esMuro(int x, int y) {
        return mapa[y][x] == '#';
    }

    public boolean hayEnemigoEn(int x, int y) {
        return enemigos.stream().anyMatch(e -> e.getX() == x && e.getY() == y && e.estaVivo());
    }

    public Enemigo obtenerEnemigoEn(int x, int y) {
        return enemigos.stream()
                .filter(e -> e.getX() == x && e.getY() == y && e.estaVivo())
                .findFirst()
                .orElse(null);
    }

    public void moverProta(int dx, int dy) {
        int nuevoX = prota.getX() + dx;
        int nuevoY = prota.getY() + dy;

        if (esMuro(nuevoX, nuevoY)) {
            return;
        }
        Enemigo enemigo = obtenerEnemigoEn(nuevoX, nuevoY);
        if (enemigo != null) {
            prota.atacar(enemigo);
            if (enemigo.estaVivo()) {
                enemigo.atacar(prota);
            }
        } else {
            prota.setPosicion(nuevoX, nuevoY);
        }
    }

    public void moverEnemigos() {
        for (Enemigo enemigo : enemigos) {
            if (!enemigo.estaVivo()) {
                continue;
            }
            if (enemigo.enRangoDe(prota)) {
                int dx = Integer.compare(prota.getX(), enemigo.getX());
                int dy = Integer.compare(prota.getY(), enemigo.getY());

                int nuevoX = enemigo.getX() + dx;
                int nuevoY = enemigo.getY() + dy;

                if (!esMuro(nuevoX, nuevoY) && !hayEnemigoEn(nuevoX, nuevoY)) {
                    enemigo.setPosicion(nuevoX, nuevoY);
                }
            } else {
                // movimiento aleatorio
                int[] dx = {0, 1, 0, -1};
                int[] dy = {-1, 0, 1, 0};
                int dir = new Random().nextInt(4);

                int nuevoX = enemigo.getX() + dx[dir];
                int nuevoY = enemigo.getY() + dy[dir];

                if (!esMuro(nuevoX, nuevoY) && !hayEnemigoEn(nuevoX, nuevoY)) {
                    enemigo.setPosicion(nuevoX, nuevoY);
                }
            }
        }
    }
}