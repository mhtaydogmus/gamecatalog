module com.example.main_screen {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.main_screen to javafx.fxml;
    exports com.example.main_screen;
}