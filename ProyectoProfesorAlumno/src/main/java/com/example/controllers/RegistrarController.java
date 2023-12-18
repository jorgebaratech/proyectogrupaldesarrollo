package com.example.controllers;

import com.example.App;
import com.example.HibernateUtil;
import com.example.profesor.Profesor;
import com.example.profesor.ProfesorDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.extern.java.Log;
import org.hibernate.Transaction;
import java.net.URL;
import java.util.ResourceBundle;
@Log
public class RegistrarController implements Initializable{
    EmpresaController empresaController;

    @javafx.fxml.FXML
    private TextField txtNombre;
    @javafx.fxml.FXML
    private TextField txtApellido;
    @javafx.fxml.FXML
    private TextField txtCorreo;
    @javafx.fxml.FXML
    private PasswordField txtContraseña;
    @javafx.fxml.FXML
    private Button btnRegistrar;
    @javafx.fxml.FXML
    private Button btnCancelar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        empresaController = new EmpresaController();
    }
    @FXML
    public void volver(ActionEvent actionEvent) {
        App.loadFXML("login-view.fxml","Login");
    }

    @FXML
    public void registrar(ActionEvent actionEvent) {
        try ( org.hibernate.Session s = HibernateUtil.getSessionFactory( ).openSession( ) ) {
            Transaction t = s.beginTransaction( );
            Profesor profesor = new Profesor();

            if (txtNombre.getText().length()<2){
                Alert alert = new Alert( Alert.AlertType.WARNING );
                alert.setContentText( "El nombre debe tener mínimo 2 caracteres" );
                alert.show();
            } else if (txtApellido.getText().length()<2) {
                Alert alert = new Alert( Alert.AlertType.WARNING );
                alert.setContentText( "El apellido debe tener mínimo 2 caracteres" );
                alert.show();
            } else if (!empresaController.validateEmail(txtCorreo.getText())) {
                Alert alert = new Alert( Alert.AlertType.WARNING );
                alert.setContentText( "Email no válido" );
                alert.show();
            } else if (txtContraseña.getText().length()<6) {
                Alert alert = new Alert( Alert.AlertType.WARNING );
                alert.setContentText( "La contraseña debe ser de al menos 6 caracteres" );
                alert.show();
            } else {
                profesor.setNombre(txtNombre.getText());
                profesor.setApellidos(txtApellido.getText());
                profesor.setEmail(txtCorreo.getText());
                profesor.setContrasenya(txtContraseña.getText());

                s.persist( profesor );
                t.commit( );
                Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
                alert.setContentText( "Profesor registrado correctamente" );
                alert.show();
                App.loadFXML("login-view.fxml","Login");
            }
        }catch ( Exception e ) {
            log.severe( "Error al insertar un nuevo profesor" );
        }

    }
}
