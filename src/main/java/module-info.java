/**
 * Módulo principal de la aplicación efragame.
 * 
 * Define las dependencias requeridas para JavaFX y
 * configura la apertura y exportación de los paquetes
 * para que JavaFX pueda acceder a ellos mediante reflexión.
 */
module com.efragame {

    /**
     * Requiere el módulo javafx.controls para controles UI básicos.
     */
    requires javafx.controls;

    /**
     * Requiere el módulo javafx.fxml para cargar archivos FXML.
     */
    requires javafx.fxml;

    /**
     * Requiere el módulo javafx.graphics para funcionalidades gráficas.
     */
    requires javafx.graphics;

    /**
     * Permite que el paquete 'com.efragame' sea accesible a javafx.fxml para
     * carga dinámica de recursos FXML mediante reflexión.
     */
    opens com.efragame to javafx.fxml;

    /**
     * Exporta el paquete 'com.efragame' para que pueda ser usado por otros módulos.
     */
    exports com.efragame;

    /**
     * Permite que el paquete 'com.efragame.Controlador' sea accesible a javafx.fxml
     * para que pueda inyectar controles y manejar eventos definidos en FXML.
     */
    opens com.efragame.Controlador to javafx.fxml;

    /**
     * Exporta el paquete 'com.efragame.Controlador' para que pueda ser usado por otros módulos.
     */
    exports com.efragame.Controlador to javafx.fxml;
}
