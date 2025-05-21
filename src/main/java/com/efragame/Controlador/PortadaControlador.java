package com.efragame.Controlador;

import com.efragame.SceneID;
import com.efragame.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PortadaControlador {
    @FXML private Button Jugar;

    @FXML
    private void initialize() {
        Jugar.setOnAction(e -> SceneManager.getInstance().switchTo(SceneID.PERSONALIZACION));
    }
}