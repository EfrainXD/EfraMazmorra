module com.efragame {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.efragame to javafx.fxml;
    exports com.efragame;
    opens com.efragame.Controlador to javafx.fxml;
    exports com.efragame.Controlador to javafx.fxml;
}
