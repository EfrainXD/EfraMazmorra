package com.efragame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Clase singleton que gestiona el cambio de escenas (vistas) en la aplicación JavaFX.
 * 
 * {@code SceneManager} permite cambiar entre diferentes vistas de forma centralizada
 * usando identificadores definidos en {@link SceneID}, manteniendo dimensiones y
 * posición de la ventana al cambiar de escena.
 */
public class SceneManager {
    
    /** Instancia única de SceneManager (patrón Singleton). */
    private static SceneManager instance;

    /** Escenario principal de la aplicación. */
    private Stage primaryStage;

    /**
     * Constructor privado para evitar instanciación directa (patrón Singleton).
     */
    private SceneManager() {}

    /**
     * Obtiene la instancia única de {@code SceneManager}.
     *
     * @return instancia única de {@code SceneManager}
     */
    public static SceneManager getInstance() {
        if (instance == null) instance = new SceneManager();
        return instance;
    }

    /**
     * Inicializa el {@code SceneManager} con el escenario principal de la aplicación.
     *
     * @param stage el {@code Stage} principal proporcionado por JavaFX
     */
    public void init(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Cambia la escena mostrada en el escenario principal según el identificador proporcionado.
     * 
     * La nueva escena se carga desde su archivo FXML correspondiente. Se conservan el tamaño
     * y la posición actuales de la ventana.
     *
     * @param sceneID identificador de la escena a mostrar (ver {@link SceneID})
     * @throws IllegalArgumentException si el identificador de escena no es reconocido
     */
    public void switchTo(SceneID sceneID) {
        try {
            FXMLLoader loader;
            Scene scene;

            // Guardar dimensiones y posición actuales
            double currentWidth = primaryStage.getWidth();
            double currentHeight = primaryStage.getHeight();
            double currentX = primaryStage.getX();
            double currentY = primaryStage.getY();

            // Cargar la escena correspondiente
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
                    throw new IllegalArgumentException("Escena desconocida: " + sceneID);
            }

            // Aplicar la nueva escena y restaurar dimensiones
            primaryStage.setScene(scene);
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
