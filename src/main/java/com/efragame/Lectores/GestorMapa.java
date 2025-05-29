package com.efragame.Lectores;

import java.io.*;
import java.util.*;

/**
 * Clase utilitaria para la lectura de mapas desde archivos de texto.
 */
public class GestorMapa {

    /**
     * Lee un mapa desde un archivo de texto donde cada línea representa una fila
     * del mapa y cada carácter representa una celda.
     * 
     * @param ruta la ruta del archivo del mapa
     * @return un arreglo bidimensional de caracteres que representa el mapa
     * @throws IOException si ocurre un error al leer el archivo
     */
    public static char[][] leerMapa(String ruta) throws IOException {
        List<char[]> filas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                filas.add(linea.toCharArray());
            }
        }
        return filas.toArray(new char[0][]);
    }
}
