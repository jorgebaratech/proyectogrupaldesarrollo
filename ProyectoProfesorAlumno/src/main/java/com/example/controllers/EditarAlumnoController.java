package com.example.controllers;

import com.example.App;
import com.example.Sesion;
import com.example.empresas.EmpresasDAO;
import com.example.alumno.Alumno;
import com.example.profesor.ProfesorDAO;
import jakarta.persistence.criteria.CriteriaBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class EditarAlumnoController implements Initializable{

    EmpresasDAO empresasDAO;
    ProfesorDAO profesorDAO;

    @javafx.fxml.FXML
    private TextField txtNombre;
    @javafx.fxml.FXML
    private TextField txtApellidos;
    @javafx.fxml.FXML
    private TextField txtDNI;
    @javafx.fxml.FXML
    private TextField txtCorreo;
    @javafx.fxml.FXML
    private ComboBox<String> comboboxEmpresa;
    @javafx.fxml.FXML
    private Spinner<Integer> spinnerdual;
    @javafx.fxml.FXML
    private Spinner<Integer> spinnerFCT;
    @javafx.fxml.FXML
    private TextArea txtObservaciones;
    @javafx.fxml.FXML
    private Button btneliminaralumno;
    @javafx.fxml.FXML
    private Button btnGuardarAlumno;
    @javafx.fxml.FXML
    private Button btnVolver;
    @javafx.fxml.FXML
    private Button btnCerrarSesion;
    @javafx.fxml.FXML
    private TextField txtFecha;

    @Override
    public void initialize( URL url , ResourceBundle resourceBundle ) {
        empresasDAO = new EmpresasDAO();
        profesorDAO = new ProfesorDAO();
        Alumno alumnoSelected = Sesion.getAlumnoSelected();
        txtNombre.setText( alumnoSelected.getNombre() );
        txtApellidos.setText( alumnoSelected.getApellidos() );
        txtDNI.setText(String.valueOf(alumnoSelected.getDni()));
        txtFecha.setText(String.valueOf(alumnoSelected.getNacimiento()));
        txtCorreo.setText( alumnoSelected.getEmail() );

        txtObservaciones.setText( alumnoSelected.getObservaciones() );
        List<String> nombresEmpresa = new ArrayList<>(  );
        empresasDAO.getAll().forEach( s -> nombresEmpresa.add( s.getNombre() ) );
        comboboxEmpresa.getItems().addAll( nombresEmpresa );
        comboboxEmpresa.setValue( comboboxEmpresa.getItems().getFirst() );

        comboboxEmpresa.setValue( alumnoSelected.getEmpresas().getNombre() );

        spinnerdual.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory( 0 , 250 , (int) alumnoSelected.getHorasDual(), 1 ) );
        spinnerFCT.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory( 0 , 250 , (int) alumnoSelected.getHorasFct(), 1 ) );
    }

    @javafx.fxml.FXML
    public void eliminaralumno( ) {
        Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
        alert.setContentText( "¿Quieres borrar al alumno " + Sesion.getAlumnoSelected().getNombre() + " " + Sesion.getAlumnoSelected().getApellidos() + "?" );
        var result = alert.showAndWait( ).get( );
        if (result.getButtonData( ) == ButtonBar.ButtonData.OK_DONE) {
            profesorDAO.borrarAlumno( Sesion.getAlumnoSelected() );
            volver(  );
        }
    }

    @javafx.fxml.FXML
    public void guardar( ) {
        Alumno alumno = Sesion.getAlumnoSelected();
        alumno.setNombre( txtNombre.getText() );
        alumno.setApellidos( txtApellidos.getText() );
        alumno.setDni(Long.parseLong(txtDNI.getText()));
        alumno.setNacimiento(Date.valueOf(txtFecha.getText()));
        alumno.setEmail( txtCorreo.getText() );
        alumno.setEmpresas( profesorDAO.empresasPorNombre((String) comboboxEmpresa.getValue()) );
        alumno.setHorasDual(spinnerdual.getValue());
        alumno.setHorasFct(spinnerFCT.getValue());
        alumno.setObservaciones( txtObservaciones.getText() );
        profesorDAO.updateAlumno( alumno );
    }

    @javafx.fxml.FXML
    public void volver( ) {
        Sesion.setAlumnoSelected( null );
        App.loadFXML( "profesor-view.fxml" , "Profesor " + Sesion.getProfesorLogged().getNombre() );
    }

    @javafx.fxml.FXML
    public void cerrarsesion(ActionEvent actionEvent) {
        Sesion.cerrarsesion();
        App.loadFXML("login-view.fxml", "Login");
    }
}
