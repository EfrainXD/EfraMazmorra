package com.unaidarioefra.Lectores;

import com.unaidarioefra.Modelo.Enemigo;
import com.unaidarioefra.Modelo.EnemigoTipo;

import java.io.*;
import java.util.*;

public class GestorEnemigos {
    public static List<Enemigo> cargarEnemigos(String path) throws IOException {
        List<Enemigo> enemigos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // header
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
