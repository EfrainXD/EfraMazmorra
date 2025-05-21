package com.efragame.Controlador;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;

import com.efragame.Modelo.*;

public class JuegoControlador {
    @FXML private GridPane gridMapa;
    @FXML private Text vidaProta;
    @FXML private TextArea infoCombate;

    private Juego juego;

    @FXML
    public void initialize() {
        try {
            juego = new Juego("src/main/resources/com/efragame/files/mapa.csv",
                              "src/main/resources/com/efragame/files/enemigos.csv");
            dibujarMapa();
            actualizarEstadisticas();

            gridMapa.setFocusTraversable(true);
            gridMapa.setOnKeyPressed(this::manejarTecla);
            gridMapa.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dibujarMapa() {
        gridMapa.getChildren().clear();
        char[][] mapa = juego.getMapa();
        Image imgSuelo = new Image(new File("src/main/resources/com/efragame/images/suelo1.png").toURI().toString());
        Image imgPared = new Image(new File("src/main/resources/com/efragame/images/pared1.png").toURI().toString());
        Image imgProta = new Image(new File("src/main/resources/com/efragame/images/protagonista.png").toURI().toString());
        Image imgEsbirro = new Image(new File("src/main/resources/com/efragame/images/esbirro.png").toURI().toString());
        Image imgEsqueleto = new Image(new File("src/main/resources/com/efragame/images/esqueleto.png").toURI().toString());
        Image imgZombie = new Image(new File("src/main/resources/com/efragame/images/zombie.png").toURI().toString());

        for (int y = 0; y < mapa.length; y++) {
            for (int x = 0; x < mapa[y].length; x++) {
                ImageView imgView = new ImageView(mapa[y][x] == '#' ? imgPared : imgSuelo);
                imgView.setFitWidth(32);
                imgView.setFitHeight(32);
                gridMapa.add(imgView, x, y);
            }
        }

        // Protagonista
        ImageView protaView = new ImageView(imgProta);
        protaView.setFitWidth(32);
        protaView.setFitHeight(32);
        gridMapa.add(protaView, juego.getProta().getX(), juego.getProta().getY());

        // Enemigos
        for (Enemigo e : juego.getEnemigos()) {
            if (!e.estaVivo()) continue;
            Image img = null;
            EnemigoTipo tipo = e.getTipo();
            switch (tipo) {
                case ESBIRRO: 
                    img = imgEsbirro;
                    break;
                case ESQUELETO: 
                    img = imgEsqueleto;
                    break;
                case ZOMBIE:
                    img = imgZombie;
                    break;
                default:
                    System.out.println("Enemigo no encontrado");
                    break;
            }
            ImageView enemigoView = new ImageView(img);
            enemigoView.setFitWidth(32);
            enemigoView.setFitHeight(32);
            gridMapa.add(enemigoView, e.getX(), e.getY());
        }
        
        gridMapa.requestFocus();
    }

    private void moverProta(int dx, int dy) {
        Prota p = juego.getProta();
        int nuevoX = p.getX() + dx;
        int nuevoY = p.getY() + dy;

        if (juego.esMuro(nuevoX, nuevoY)) {
            System.out.println("¡Hay un muro!");
            return;
        }

        Enemigo enemigo = juego.obtenerEnemigoEn(nuevoX, nuevoY);
        if (enemigo != null) {
            System.out.println("¡Encontraste un enemigo!");
            infoCombate.clear();
            combatir(p, enemigo);
            actualizarEstadisticas();
        } else {
            System.out.println("Moviendo de (" + p.getX() + "," + p.getY() + ") a (" + nuevoX + "," + nuevoY + ")");
            p.setPosicion(nuevoX, nuevoY);
            infoCombate.clear();
        }

        moverEnemigosConPercepcion();

        dibujarMapa();
        actualizarEstadisticas();
    }

    private void moverEnemigosConPercepcion() {
        Prota p = juego.getProta();

        for (Enemigo e : juego.getEnemigos()) {
            if (!e.estaVivo()) continue;

            if (e.enRangoDe(p)) {
                // Enemigo detecta al prota y se mueve hacia él
                int dx = Integer.compare(p.getX(), e.getX());
                int dy = Integer.compare(p.getY(), e.getY());

                int nuevoX = e.getX() + dx;
                int nuevoY = e.getY() + dy;

                if (!juego.esMuro(nuevoX, nuevoY) && juego.obtenerEnemigoEn(nuevoX, nuevoY) == null
                    && !(nuevoX == p.getX() && nuevoY == p.getY())) {
                    e.setPosicion(nuevoX, nuevoY);
                }
            } else {
                // Movimiento aleatorio si no detecta al prota
                moverEnemigoAleatorio(e);
            }
        }
    }

    private void moverEnemigoAleatorio(Enemigo e) {
        int[] dx = {0, 1, 0, -1};
        int[] dy = {-1, 0, 1, 0};

        int dir = (int)(Math.random() * 4);
        int nuevoX = e.getX() + dx[dir];
        int nuevoY = e.getY() + dy[dir];

        if (!juego.esMuro(nuevoX, nuevoY) && juego.obtenerEnemigoEn(nuevoX, nuevoY) == null) {
            e.setPosicion(nuevoX, nuevoY);
        }
    }

    private void combatir(Prota p, Enemigo e) {
        StringBuilder mensaje = new StringBuilder();

        int danioAlEnemigo = Math.max(0, p.getEstadisticas().getFuerza() - 5);
        e.setSalud(e.getSalud() - danioAlEnemigo);
        mensaje.append("Atacaste al ").append(e.getTipo()).append(" e hiciste ").append(danioAlEnemigo).append(" de daño.\n");

        if (e.estaVivo()) {
            int danioAlProta = Math.max(0, e.getTipo().getAtaque() - p.getEstadisticas().getDefensa());
            p.getEstadisticas().setSalud(p.getEstadisticas().getSalud() - danioAlProta);
            mensaje.append("El ").append(e.getTipo()).append(" te atacó e hizo ").append(danioAlProta).append(" de daño.\n");
        } else {
            mensaje.append("¡Derrotaste al ").append(e.getTipo()).append("!\n");
        }

        infoCombate.appendText(mensaje.toString());
    }

    private void actualizarEstadisticas() {
        vidaProta.setText("Vida: " + juego.getProta().getEstadisticas().getSalud());
    }

    @FXML
    public void manejarTecla(KeyEvent event) {
        System.out.println("Tecla presionada: " + event.getCode());
        switch (event.getCode()) {
            case W: 
                moverProta(0, -1);
                break;
            case A: 
                moverProta(-1, 0);
                break;
            case S: 
                moverProta(0, 1);
                break;
            case D: 
                moverProta(1, 0);
                break;
            default:
                break;  
        }
        event.consume();
    }
}
