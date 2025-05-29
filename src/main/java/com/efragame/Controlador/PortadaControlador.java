package com.efragame.Controlador;

import com.efragame.SceneID;
import com.efragame.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controlador para la escena de portada.
 * Gestiona la interacción con el botón para iniciar el juego.
 */
public class PortadaControlador {

    @FXML
    private Button Jugar;

    /**
     * Método de inicialización que se ejecuta al cargar la vista.
     * Configura la acción del botón "Jugar" para cambiar a la escena de personalización.
     */
    @FXML
    private void initialize() {
        Jugar.setOnAction(e -> SceneManager.getInstance().switchTo(SceneID.PERSONALIZACION));
    }
}
