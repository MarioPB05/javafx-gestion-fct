module mpb.control_de_personas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires lombok;

    opens apps to javafx.fxml;
    opens controller to javafx.fxml;

    exports apps;
    exports model;
    exports controller;
}