module com.example.controllers {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;

    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;

    opens com.example.alumno;
    opens com.example.profesor;
    opens com.example.empresas;
    opens com.example.actividad;
    opens com.example.controllers to javafx.fxml;
    exports com.example.controllers;
    exports com.example;
    opens com.example to javafx.fxml;
}