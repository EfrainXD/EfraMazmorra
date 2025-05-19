package com.unaidarioefra.Lectores;

import java.io.*;
import java.util.*;

public class GestorMapa {
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
