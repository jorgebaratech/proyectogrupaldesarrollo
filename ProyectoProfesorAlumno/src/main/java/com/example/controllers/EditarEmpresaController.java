package com.example.controllers;

import com.example.App;
import com.example.Sesion;
import com.example.empresas.Empresas;
import com.example.empresas.EmpresasDAO;
import com.example.profesor.Profesor;
import com.example.profesor.ProfesorDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class EditarEmpresaController implements Initializable{

    EmpresasDAO empresasDAO;
    ProfesorDAO profesorDAO;
    EmpresaController empresaController;

    @javafx.fxml.FXML
    private Label labelprofesor;
    @javafx.fxml.FXML
    private Label labelnombreempresa;
    @javafx.fxml.FXML
    private TextField txtNombre;
    @javafx.fxml.FXML
    private TextField txtCorreo;
    @javafx.fxml.FXML
    private TextField txtTutor;
    @javafx.fxml.FXML
    private TextField txtTelefono;
    @javafx.fxml.FXML
    private TextArea txtObservaciones;
    @javafx.fxml.FXML
    private Button botonguardar;
    @javafx.fxml.FXML
    private Button botoneliminar;
    @javafx.fxml.FXML
    private Button botonvolver;
    @javafx.fxml.FXML
    private Button botoncerrarsesion;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        empresasDAO = new EmpresasDAO();
        profesorDAO = new ProfesorDAO();
        empresaController = new EmpresaController();
        Empresas empresaSelected = Sesion.getEmpresaSelected();
        Profesor currentProfesor = Sesion.getProfesorLogged();
        txtNombre.setText(empresaSelected.getNombre());
        txtCorreo.setText(empresaSelected.getEmail());
        txtTutor.setText(empresaSelected.getResponsable());
        txtTelefono.setText(String.valueOf(empresaSelected.getTelefono()));
        txtObservaciones.setText(empresaSelected.getObservaciones());

        labelnombreempresa.setText("Editando la empresa "+empresaSelected.getNombre());
        labelprofesor.setText(currentProfesor.getNombre()+" "+currentProfesor.getApellidos());
    }
    @FXML
    public void volver() {
        App.loadFXML("empresa-view.fxml","Empresa");
    }
    @FXML
    public void cerrarsesion(ActionEvent actionEvent) {
        Sesion.cerrarsesion();
        App.loadFXML("login-view.fxml" , "Login" );
    }

    @FXML
    public void eliminarEmpresa(ActionEvent actionEvent) {
        if (Sesion.getEmpresaSelected() != null) {
            Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
            alert.setContentText( "¿Quieres borrar la empresa " + Sesion.getEmpresaSelected().getNombre() + "?" );
            var result = alert.showAndWait( ).get( );
            if (result.getButtonData( ) == ButtonBar.ButtonData.OK_DONE) {
                empresasDAO.borrarEmpresa( Sesion.getEmpresaSelected() );
                volver( );
            }
            Sesion.setEmpresaSelected(null);
        }

    }
    @FXML
    public void guardarempresa(ActionEvent actionEvent) {
        Empresas empresas = Sesion.getEmpresaSelected();
        if (txtNombre.getText().length()<2){
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "El nombre de la empresa debe tener minimo 2 caracteres" );
            alert.show();
        }
        else if (!empresaController.validateEmail(txtCorreo.getText())) {
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "Email no válido" );
            alert.show();
        }
        else if (txtTutor.getText().length()<3){
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "El nombre del tutor debe tener minimo 3 caracteres" );
            alert.show();
        }
        else if (!empresaController.validatePhone(txtTelefono.getText())) {
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "Numero de teléfono introducido no válido" );
            alert.show();
        }
        else if (txtObservaciones.getText().length()<3){
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "Ingrese minimo 3 caracteres en las observaciones" );
            alert.show();
        }
        else {
            empresas.setNombre(txtNombre.getText());
            empresas.setEmail(txtCorreo.getText());
            empresas.setResponsable(txtTutor.getText());
            empresas.setTelefono(Long.parseLong(txtTelefono.getText()));
            empresas.setObservaciones(txtObservaciones.getText());
            empresasDAO.actualizarEmpresa(empresas);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Edición de la empresa correcta");
            alert.show();
            volver();
        }
    }
}
