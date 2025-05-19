package com.unaidarioefra.Controlador;

import com.unaidarioefra.SceneID;
import com.unaidarioefra.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PortadaControlador {
    @FXML private Button Jugar;

    @FXML
    private void initialize() {
        Jugar.setOnAction(e -> SceneManager.getInstance().switchTo(SceneID.JUEGO));;
    }
}
