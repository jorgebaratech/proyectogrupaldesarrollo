package com.example.controllers;

import com.example.App;
import com.example.Sesion;
import com.example.alumno.Alumno;
import com.example.alumno.AlumnoDAO;
import com.example.profesor.ProfesorDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @javafx.fxml.FXML
    private TextField txtUsuario;
    @javafx.fxml.FXML
    private PasswordField txtContraseña;
    @javafx.fxml.FXML
    private ComboBox<String> comoboxAlumnoProfe;
    @javafx.fxml.FXML
    private Button btnEntrar;
    @javafx.fxml.FXML
    private Button btnRegistrar;
    @javafx.fxml.FXML
    private Label info;
    @javafx.fxml.FXML
    private Label labeliniciarsesion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comoboxAlumnoProfe.setValue(comoboxAlumnoProfe.getItems().getFirst());
        txtUsuario.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnEntrar.fire();
            }
        });
        txtContraseña.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // Púlsa el botón
                btnEntrar.fire();
            }
        });
    }

    public void iniciarsesion(ActionEvent actionEvent) {
        AlumnoDAO daoS = new AlumnoDAO();
        ProfesorDAO daoT = new ProfesorDAO();
        if (comoboxAlumnoProfe.getValue().equals("Alumno")) {
            try {
                if (daoS.esAlumnoCorrecto(txtUsuario.getText(), txtContraseña.getText())) {
                    Sesion.setCurrentAlumno(daoS.loadLogin(txtUsuario.getText(), txtContraseña.getText()));

                    App.loadFXML("alumno-view.fxml", "Tareas de " + Sesion.getCurrentAlumno().getNombre());
                } else {
                    txtUsuario.setText("");
                    txtContraseña.setText("");
                    info.setText("Error");
                }

            } catch (Exception e) {
                txtUsuario.setText("");
                txtContraseña.setText("");
                info.setText("Error");
                System.out.println(e.getMessage());
            }
        } else if (comoboxAlumnoProfe.getValue().equals("Profesor")) {
            try {
                if (daoT.esProfesorCorrecto(txtUsuario.getText(), txtContraseña.getText())) {
                    Sesion.setProfesorLogged(daoT.loadLogin(txtUsuario.getText(), txtContraseña.getText()));

                    App.loadFXML("profesor-view.fxml", "Alumnos de " + Sesion.getProfesorLogged().getNombre());
                } else {
                    txtUsuario.setText("");
                    txtContraseña.setText("");
                    info.setText("Error");
                }

            } catch (Exception e) {
                txtUsuario.setText("");
                txtContraseña.setText("");
                info.setText("Error");
                System.out.println(e.getMessage());
            }

        }
    }
    @javafx.fxml.FXML
    public void registrar(ActionEvent actionEvent) {
        App.loadFXML("registrar-view.fxml", "Registrar");
    }
}
