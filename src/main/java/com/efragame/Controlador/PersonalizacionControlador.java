package com.efragame.Controlador;

import com.efragame.EstadisticasPersonaje;
import com.efragame.SceneID;
import com.efragame.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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
    
    @FXML
    private void initialize() {
        // Configurar valores iniciales de los sliders
        EstadisticasPersonaje stats = EstadisticasPersonaje.getInstance();
        saludSlider.setValue(stats.getSalud());
        fuerzaSlider.setValue(stats.getFuerza());
        defensaSlider.setValue(stats.getDefensa());
        evasionSlider.setValue(stats.getEvasion());
        
        // Actualizar labels con valores iniciales
        saludLabel.setText(String.valueOf((int)saludSlider.getValue()));
        fuerzaLabel.setText(String.valueOf((int)fuerzaSlider.getValue()));
        defensaLabel.setText(String.valueOf((int)defensaSlider.getValue()));
        evasionLabel.setText(String.valueOf((int)evasionSlider.getValue()));
        
        // Configurar listeners para actualizar etiquetas y calcular puntos
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
        
        // Calcular puntos iniciales
        calcularPuntosUsados();
        
        // Configurar botones
        Volver.setOnAction(e -> SceneManager.getInstance().switchTo(SceneID.PORTADA));
        Iniciar.setOnAction(e -> iniciarJuego());
    }
    
    private void calcularPuntosUsados() {
        puntosUsados = (int) (
            saludSlider.getValue() * 1 +     
            fuerzaSlider.getValue() * 3 +    
            defensaSlider.getValue() * 3 +   
            evasionSlider.getValue() * 3     
        );
        
        int puntosRestantes = PUNTOS_TOTALES - puntosUsados;
        
        // Actualizar texto con puntos disponibles
        String textoRestante = "Puntos disponibles: " + puntosRestantes;
        puntosDisponibles.setText(textoRestante);
        
        // Cambiar color según si hay puntos disponibles o no
        if (puntosRestantes < 0) {
            puntosDisponibles.setFill(Color.RED);
            Iniciar.setDisable(true);
        } else {
            puntosDisponibles.setFill(Color.BLACK);
            Iniciar.setDisable(false);
        }
    }
    
    private void iniciarJuego() {
        // Guardar las estadísticas seleccionadas en la clase singleton
        EstadisticasPersonaje.getInstance().setSalud((int) saludSlider.getValue());
        EstadisticasPersonaje.getInstance().setFuerza((int) fuerzaSlider.getValue());
        EstadisticasPersonaje.getInstance().setDefensa((int) defensaSlider.getValue());
        EstadisticasPersonaje.getInstance().setEvasion((int) evasionSlider.getValue());
        
        // Cambiar a la escena del juego
        SceneManager.getInstance().switchTo(SceneID.JUEGO);
    }
}