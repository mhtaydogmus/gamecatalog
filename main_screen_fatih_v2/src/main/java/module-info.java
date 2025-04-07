module com.example.main_screen {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.databind;

    opens com.example.main_screen to com.fasterxml.jackson.databind;

    exports com.example.main_screen;
}