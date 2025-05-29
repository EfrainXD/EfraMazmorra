package com.efragame.Lectores;

import java.io.*;
import java.util.*;

import com.efragame.Modelo.Enemigo;
import com.efragame.Modelo.EnemigoTipo;

/**
 * Clase utilitaria para cargar enemigos desde un archivo CSV.
 */
public class GestorEnemigos {

    /**
     * Carga una lista de enemigos desde un archivo CSV.
     * 
     * El archivo debe tener un encabezado y cada línea debe contener:
     * tipo,x,y
     * 
     * Ejemplo:
     * ESBIRRO,3,5
     * ESQUELETO,10,7
     * 
     * @param path ruta al archivo CSV de enemigos
     * @return lista de enemigos cargados
     * @throws IOException si ocurre un error de lectura del archivo
     */
    public static List<Enemigo> cargarEnemigos(String path) throws IOException {
        List<Enemigo> enemigos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // saltar encabezado
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
        return enemigos;
    }
}
