package com.unaidarioefra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager.getInstance().init(primaryStage);

        FXMLLoader cargar = new FXMLLoader();
        cargar.setLocation(getClass().getResource("/com/unaidarioefra/vistas/VistaEstadisticas.fxml"));
        Parent root = cargar.load();

        Scene escena = new Scene(root);
        primaryStage.setScene(escena);
        primaryStage.setTitle("Estadisticas vista");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
