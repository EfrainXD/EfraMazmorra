module com.unaidarioefra {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.unaidarioefra to javafx.fxml;
    exports com.unaidarioefra;
    opens com.unaidarioefra.Controlador to javafx.fxml;
    exports com.unaidarioefra.Controlador to javafx.fxml;
}
