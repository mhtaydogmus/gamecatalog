module com.example.main_screen {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.example.main_screen to com.google.gson;

    exports com.example.main_screen;
}
