package com.efragame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación JavaFX.
 * 
 * Esta clase extiende {@code javafx.application.Application} y sirve como punto de entrada
 * para iniciar la interfaz gráfica del juego "Trilogía de Mazmorras".
 */
public class App extends Application {

    /**
     * Método de inicio de la aplicación JavaFX.
     *
     * Se encarga de cargar la vista inicial desde el archivo FXML, configurar
     * el tamaño de la ventana según la resolución de la pantalla, establecer
     * restricciones de tamaño mínimo, centrar la ventana y mostrarla.
     *
     * @param primaryStage el escenario principal proporcionado por JavaFX
     * @throws Exception si hay algún error al cargar el archivo FXML o al inicializar la escena
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Inicializar el gestor de escenas con el escenario principal
        SceneManager.getInstance().init(primaryStage);

        // Cargar la vista inicial desde el archivo FXML
        FXMLLoader cargar = new FXMLLoader();
        cargar.setLocation(getClass().getResource("/com/efragame/vistas/VistaPortada.fxml"));
        Parent root = cargar.load();

        // Obtener dimensiones de la pantalla principal
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        
        // Calcular tamaño de ventana basado en el 80% del tamaño de pantalla
        double windowWidth = Math.min(1200, screenBounds.getWidth() * 0.8);
        double windowHeight = Math.min(800, screenBounds.getHeight() * 0.8);
        
        // Crear la escena con el tamaño calculado
        Scene escena = new Scene(root, windowWidth, windowHeight);
        primaryStage.setTitle("Trilogía de Mazmorras");
        primaryStage.setScene(escena);
        
        // Establecer tamaño mínimo de la ventana para jugabilidad
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        
        // Centrar la ventana en la pantalla
        primaryStage.setX((screenBounds.getWidth() - windowWidth) / 2);
        primaryStage.setY((screenBounds.getHeight() - windowHeight) / 2);
        
        // Permitir que el usuario redimensione la ventana
        primaryStage.setResizable(true);
        
        // Mostrar la ventana
        primaryStage.show();
    }

    /**
     * Método principal que lanza la aplicación JavaFX.
     * 
     * Añadir que al llegar a la vista del mapa con agrandar o achicar la pantalla
     * el problema sera arreglado ya que no se muestra completa la vista, al igual al dar al esc o r.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}
