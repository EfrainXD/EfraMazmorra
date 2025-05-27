package com.efragame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneManager {
    private static SceneManager instance;
    private Stage primaryStage;

    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) instance = new SceneManager();
        return instance;
    }

    public void init(Stage stage) {
        this.primaryStage = stage;
    }

    public void switchTo(SceneID sceneID) {
        try {
            FXMLLoader loader;
            Scene scene;

            // Guardar dimensiones actuales
            double currentWidth = primaryStage.getWidth();
            double currentHeight = primaryStage.getHeight();
            double currentX = primaryStage.getX();
            double currentY = primaryStage.getY();

            switch (sceneID) {
                case JUEGO:
                    loader = new FXMLLoader(getClass().getResource("/com/efragame/vistas/VistaJuego.fxml"));
                    scene = new Scene(loader.load());
                    break;
                case PORTADA:
                    loader = new FXMLLoader(getClass().getResource("/com/efragame/vistas/VistaPortada.fxml"));
                    scene = new Scene(loader.load());
                    break;
                case PERSONALIZACION:
                    loader = new FXMLLoader(getClass().getResource("/com/efragame/vistas/VistaPersonalizacion.fxml"));
                    scene = new Scene(loader.load());
                    break;
                default:
                    throw new IllegalArgumentException("Escena desconocida");
            }

            primaryStage.setScene(scene);
            
            // Restaurar dimensiones y posición
            primaryStage.setWidth(currentWidth);
            primaryStage.setHeight(currentHeight);
            primaryStage.setX(currentX);
            primaryStage.setY(currentY);
            
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}