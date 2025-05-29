package com.efragame.Controlador;

import com.efragame.SceneID;
import com.efragame.SceneManager;
import com.efragame.Modelo.EstadisticasPersonaje;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Controlador para la escena de personalización de estadísticas del personaje.
 * Permite ajustar valores mediante sliders, valida los puntos usados,
 * y guarda las estadísticas seleccionadas antes de iniciar el juego.
 */
public class PersonalizacionControlador {

    @FXML private Slider saludSlider;
    @FXML private Slider fuerzaSlider;
    @FXML private Slider defensaSlider;
    @FXML private Slider evasionSlider;

    @FXML private Label saludLabel;
    @FXML private Label fuerzaLabel;
    @FXML private Label defensaLabel;
    @FXML private Label evasionLabel;

    @FXML private Text puntosDisponibles;

    @FXML private Button Volver;
    @FXML private Button Iniciar;

    private final int PUNTOS_TOTALES = 200;
    private int puntosUsados = 0;

    /**
     * Inicializa la escena configurando los sliders con los valores actuales,
     * actualiza las etiquetas y añade listeners para controlar los puntos usados.
     * También configura los botones para volver y comenzar el juego.
     */
    @FXML
    private void initialize() {
        EstadisticasPersonaje stats = EstadisticasPersonaje.getInstance();
        saludSlider.setValue(stats.getSalud());
        fuerzaSlider.setValue(stats.getFuerza());
        defensaSlider.setValue(stats.getDefensa());
        evasionSlider.setValue(stats.getEvasion());

        saludLabel.setText(String.valueOf((int) saludSlider.getValue()));
        fuerzaLabel.setText(String.valueOf((int) fuerzaSlider.getValue()));
        defensaLabel.setText(String.valueOf((int) defensaSlider.getValue()));
        evasionLabel.setText(String.valueOf((int) evasionSlider.getValue()));

        saludSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            saludLabel.setText(String.valueOf(newVal.intValue()));
            calcularPuntosUsados();
        });
        fuerzaSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            fuerzaLabel.setText(String.valueOf(newVal.intValue()));
            calcularPuntosUsados();
        });
        defensaSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            defensaLabel.setText(String.valueOf(newVal.intValue()));
            calcularPuntosUsados();
        });
        evasionSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            evasionLabel.setText(String.valueOf(newVal.intValue()));
            calcularPuntosUsados();
        });

        calcularPuntosUsados();

        Volver.setOnAction(e -> SceneManager.getInstance().switchTo(SceneID.PORTADA));
        Iniciar.setOnAction(e -> iniciarJuego());
    }

    /**
     * Calcula los puntos usados en base a los valores de los sliders y actualiza
     * el texto que muestra los puntos disponibles. Deshabilita el botón "Iniciar"
     * si se sobrepasan los puntos disponibles.
     */
    private void calcularPuntosUsados() {
        puntosUsados = (int) (
            saludSlider.getValue() * 1 +
            fuerzaSlider.getValue() * 3 +
            defensaSlider.getValue() * 3 +
            evasionSlider.getValue() * 3
        );

        int puntosRestantes = PUNTOS_TOTALES - puntosUsados;

        String textoRestante = "Puntos disponibles: " + puntosRestantes;
        puntosDisponibles.setText(textoRestante);

        if (puntosRestantes < 0) {
            puntosDisponibles.setFill(Color.RED);
            Iniciar.setDisable(true);
        } else {
            puntosDisponibles.setFill(Color.BLACK);
            Iniciar.setDisable(false);
        }
    }

    /**
     * Guarda las estadísticas seleccionadas en el singleton EstadisticasPersonaje
     * y cambia a la escena del juego.
     */
    private void iniciarJuego() {
        EstadisticasPersonaje.getInstance().setSalud((int) saludSlider.getValue());
        EstadisticasPersonaje.getInstance().setFuerza((int) fuerzaSlider.getValue());
        EstadisticasPersonaje.getInstance().setDefensa((int) defensaSlider.getValue());
        EstadisticasPersonaje.getInstance().setEvasion((int) evasionSlider.getValue());

        SceneManager.getInstance().switchTo(SceneID.JUEGO);
    }
}
