module org.example.ics372project3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires java.xml;
    requires  com.fasterxml.jackson.databind;
    requires  json.simple;
    requires java.desktop;


    opens org.example.ics372project3 to javafx.fxml;
    exports org.example.ics372project3;
}