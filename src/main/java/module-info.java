module com.example.sysimc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.sysimc to javafx.fxml;
    exports com.example.sysimc;
    exports com.example.sysimc.controller;
    opens com.example.sysimc.controller to javafx.fxml;
    opens com.example.sysimc.model to javafx.base;
}