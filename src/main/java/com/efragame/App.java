package com.efragame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager.getInstance().init(primaryStage);

        FXMLLoader cargar = new FXMLLoader();
        cargar.setLocation(getClass().getResource("/com/efragame/vistas/VistaPortada.fxml"));
        Parent root = cargar.load();

        // Obtener dimensiones de la pantalla
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        
        // Calcular tamaño de ventana basado en la pantalla (80% del tamaño de pantalla)
        double windowWidth = Math.min(1200, screenBounds.getWidth() * 0.8);
        double windowHeight = Math.min(800, screenBounds.getHeight() * 0.8);
        
        Scene escena = new Scene(root, windowWidth, windowHeight);
        primaryStage.setTitle("Triologia de Mazmorras");
        primaryStage.setScene(escena);
        
        // Establecer tamaño mínimo para que el juego sea jugable
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        
        // Centrar la ventana en la pantalla
        primaryStage.setX((screenBounds.getWidth() - windowWidth) / 2);
        primaryStage.setY((screenBounds.getHeight() - windowHeight) / 2);
        
        // Permitir redimensionar
        primaryStage.setResizable(true);
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}