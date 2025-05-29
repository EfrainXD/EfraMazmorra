package com.efragame.Modelo;

import java.io.*;
import java.util.*;

/**
 * Clase principal del modelo del juego. Controla el estado del mapa, el protagonista
 * y los enemigos, así como sus interacciones.
 */
public class Juego {
    /** Mapa del juego representado como una matriz de caracteres. */
    private char[][] mapa;

    /** Instancia del protagonista. */
    private Prota prota;

    /** Lista de enemigos activos en el juego. */
    private List<Enemigo> enemigos;

    /**
     * Constructor que carga el mapa y los enemigos desde archivos.
     *
     * @param mapaPath     ruta del archivo que contiene el mapa
     * @param enemigosPath ruta del archivo que contiene los enemigos
     * @throws IOException si hay un error al leer los archivos
     */
    public Juego(String mapaPath, String enemigosPath) throws IOException {
        cargarMapa(mapaPath);
        cargarEnemigos(enemigosPath);
        this.prota = new Prota(1, 1); // posición inicial por defecto
    }

    /**
     * Carga el mapa desde un archivo de texto.
     *
     * @param path ruta del archivo del mapa
     * @throws IOException si ocurre un error al leer el archivo
     */
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

    /**
     * Carga los enemigos desde un archivo CSV.
     *
     * @param path ruta del archivo de enemigos
     * @throws IOException si ocurre un error al leer el archivo
     */
    private void cargarEnemigos(String path) throws IOException {
        enemigos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // saltar cabecera
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

    /** @return matriz de caracteres que representa el mapa */
    public char[][] getMapa() {
        return mapa;
    }

    /** @return instancia del protagonista */
    public Prota getProta() {
        return prota;
    }

    /** @return lista de enemigos en el juego */
    public List<Enemigo> getEnemigos() {
        return enemigos;
    }

    /**
     * Indica si la celda en (x, y) es un muro.
     *
     * @param x coordenada X
     * @param y coordenada Y
     * @return true si es un muro, false en otro caso
     */
    public boolean esMuro(int x, int y) {
        return mapa[y][x] == '#';
    }

    /**
     * Verifica si hay un enemigo vivo en una celda.
     *
     * @param x coordenada X
     * @param y coordenada Y
     * @return true si hay un enemigo vivo, false si no
     */
    public boolean hayEnemigoEn(int x, int y) {
        return enemigos.stream().anyMatch(e -> e.getX() == x && e.getY() == y && e.estaVivo());
    }

    /**
     * Obtiene el enemigo vivo que está en la celda especificada.
     *
     * @param x coordenada X
     * @param y coordenada Y
     * @return enemigo encontrado o null si no hay
     */
    public Enemigo obtenerEnemigoEn(int x, int y) {
        return enemigos.stream()
                .filter(e -> e.getX() == x && e.getY() == y && e.estaVivo())
                .findFirst()
                .orElse(null);
    }

    /**
     * Intenta mover al protagonista en una dirección. Si hay un muro, no se mueve.
     * Si hay un enemigo, se combate. Si no hay obstáculo, se mueve.
     *
     * @param dx desplazamiento en X
     * @param dy desplazamiento en Y
     */
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

    /**
     * Mueve todos los enemigos del juego. Si el protagonista está dentro de su rango
     * de percepción, lo persiguen. Si no, se mueven aleatoriamente.
     */
    public void moverEnemigos() {
        for (Enemigo enemigo : enemigos) {
            if (!enemigo.estaVivo()) continue;

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
