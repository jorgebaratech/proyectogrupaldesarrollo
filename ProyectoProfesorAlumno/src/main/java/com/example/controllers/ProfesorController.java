package com.example.controllers;

import com.example.App;
import com.example.HibernateUtil;
import com.example.Sesion;
import com.example.empresas.Empresas;
import com.example.empresas.EmpresasDAO;
import com.example.actividad.Actividad;
import com.example.alumno.Alumno;
import com.example.profesor.Profesor;
import com.example.profesor.ProfesorDAO;
import jakarta.persistence.criteria.CriteriaBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.hibernate.Transaction;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfesorController implements Initializable {
    ProfesorDAO profesorDAO;
    EmpresasDAO empresasDAO;
    @javafx.fxml.FXML
    private TextField txtNombre;
    @javafx.fxml.FXML
    private TextField txtApellidos;
    @javafx.fxml.FXML
    private TextField txtPassword;
    @javafx.fxml.FXML
    private TextField txtDNI;
    @javafx.fxml.FXML
    private TextField txtCorreo;
    @javafx.fxml.FXML
    private DatePicker txtFNac;
    @javafx.fxml.FXML
    private TextField txtTelefono;
    @javafx.fxml.FXML
    private ComboBox<String> comoboxempresa;
    @javafx.fxml.FXML
    private TextArea txtobservaciones;
    @javafx.fxml.FXML
    private TableView<Alumno> tablaprofesor;
    @javafx.fxml.FXML
    private TableColumn<Alumno,String> cnombre;
    @javafx.fxml.FXML
    private TableColumn<Alumno,String> capellidos;
    @javafx.fxml.FXML
    private TableColumn<Alumno,String> cdni;
    @javafx.fxml.FXML
    private TableColumn<Alumno,String> cfnac;
    @javafx.fxml.FXML
    private TableColumn<Alumno,String> ccorreo;
    @javafx.fxml.FXML
    private TableColumn<Alumno,String> cempresa;
    @javafx.fxml.FXML
    private TableColumn<Alumno,String> chdual;
    @javafx.fxml.FXML
    private TableColumn<Alumno,String> chfct;
    @javafx.fxml.FXML
    private TableColumn<Alumno,String> cobservaciones;
    @javafx.fxml.FXML
    private Label labelprofesor;
    @javafx.fxml.FXML
    private Button btnnuevoprofesor;
    @javafx.fxml.FXML
    private Button btnveralumno;
    @javafx.fxml.FXML
    private Button btnverempresa;
    @javafx.fxml.FXML
    private Button btncerrarsesion;

    @Override
    public void initialize( URL url , ResourceBundle resourceBundle ) {
        profesorDAO = new ProfesorDAO();
        empresasDAO = new EmpresasDAO();

        fillTable( );

        List<String> nombresEmpresa = new ArrayList<>(  );
        empresasDAO.getAll().forEach( s -> nombresEmpresa.add( s.getNombre() ) );
        comoboxempresa.getItems().addAll( nombresEmpresa );
        comoboxempresa.setValue( comoboxempresa.getItems().getFirst() );


        tablaprofesor.getSelectionModel( ).selectedItemProperty( ).addListener( ( observableValue , producto , t1 ) -> {
            Alumno alumno = profesorDAO.alumnosporDNI(String.valueOf(t1.getDni( )));
            Sesion.setAlumnoSelected( alumno );
        } );
    }

    private void fillTable( ) {
        Profesor profesor = Sesion.getProfesorLogged( );
        ProfesorDAO profesorDAO = new ProfesorDAO( );
        List<Alumno> alumnosdeunprofesor = profesorDAO.alumnosdeunprofesor( profesor );
        cnombre.setCellValueFactory( ( fila ) -> {
            String name = fila.getValue( ).getNombre();
            return new SimpleStringProperty( name );
        } );
        capellidos.setCellValueFactory( ( fila ) -> {
            String lastName = fila.getValue( ).getApellidos();
            return new SimpleStringProperty( lastName );
        } );
        cdni.setCellValueFactory( ( fila ) -> {
            String dni = String.valueOf(fila.getValue( ).getDni());
            return new SimpleStringProperty( dni );
        } );
        cfnac.setCellValueFactory( ( fila ) -> {
            String fechaNac = String.valueOf(fila.getValue( ).getNacimiento());
            return new SimpleStringProperty( fechaNac );
        } );
        ccorreo.setCellValueFactory( ( fila ) -> {
            String email = fila.getValue( ).getEmail();
            return new SimpleStringProperty( email );
        } );
        cempresa.setCellValueFactory( ( fila ) -> {
            Empresas empresas = fila.getValue( ).getEmpresas();
            if (empresas != null) return new SimpleStringProperty( empresas.getNombre( ) );
            else return new SimpleStringProperty("");
        } );
        chdual.setCellValueFactory( ( fila ) -> {
            Integer dual = Math.toIntExact(fila.getValue().getHorasDual());
            if (dual != null) return new SimpleStringProperty( dual.toString() );
            else return new SimpleStringProperty("");
        } );
        chfct.setCellValueFactory( ( fila ) -> {
            Integer fct = Math.toIntExact(fila.getValue().getHorasFct());
            if (fct != null) return new SimpleStringProperty( fct.toString() );
            else return new SimpleStringProperty("");
        } );
        cobservaciones.setCellValueFactory( ( fila ) -> {
            String observations = fila.getValue( ).getObservaciones();
            return new SimpleStringProperty( observations );
        } );
        ObservableList<Alumno> observableList = FXCollections.observableArrayList( );
        observableList.addAll( alumnosdeunprofesor );
        tablaprofesor.setItems( observableList );
    }
    @javafx.fxml.FXML
    public void nuevoprofesor(ActionEvent actionEvent) {
        if(txtNombre.getText().length() < 3){
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "El nombre debe tener mínimo 3 caracteres" );
            alert.show();

        }
        else if(txtApellidos.getText().length() < 3){
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "El apellido debe tener mínimo 3 caracteres" );
            alert.show();
        }
        else if(txtPassword.getText().length() < 6){
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "La contraseña debe tener mínimo 6 caracteres" );
            alert.show();
        }
        else if(!comprobarDNI(txtDNI.getText())){
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "Formato del DNI incorrecto" );
            alert.show();
        }
        else if(!comprobarEmail(txtCorreo.getText())){
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "Formato del Correo electrónico incorrecto" );
            alert.show();
        }
        else if(txtFNac.getValue() == null){
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "Rellena la fecha de nacimiento" );
            alert.show();
        }
        else if(!comprobarTelefono(txtTelefono.getText())){
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "Formato del número de telefono incorrecto" );
            alert.show();
        }
        else {
            Alumno alumno = new Alumno( );
            alumno.setTutor( Sesion.getProfesorLogged( ) );
            alumno.setDni(Long.parseLong(txtDNI.getText( )));
            alumno.setNombre( txtNombre.getText( ) );
            alumno.setApellidos( txtApellidos.getText( ) );
            alumno.setContraseña( txtPassword.getText( ) );
            alumno.setEmail( txtCorreo.getText( ) );
            alumno.setNacimiento(Date.valueOf(txtFNac.getValue( ).toString( )));
            alumno.setTelefono(Long.parseLong(txtTelefono.getText( )));
            alumno.setObservaciones( txtobservaciones.getText( ) );
            alumno.setEmpresas( profesorDAO.empresasPorNombre( comoboxempresa.getValue( ) ) );
            alumno.setActividad( new ArrayList<>( ) );
            alumno.setHorasDual( 0 );
            alumno.setHorasFct( 0 );
            profesorDAO.insertarAlumno( alumno );
            fillTable( );
        }
    }
    private boolean comprobarTelefono( String text ) {
        boolean salida = true;
        try {
            Integer.parseInt(text);
        } catch (NumberFormatException excepcion) {
            salida = false;
        }
        return salida;
    }

    private boolean comprobarEmail( String text ) {
        boolean salida;
        // Patrón para validar el email
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher mather = pattern.matcher( text);

        if (mather.find( )) {
            salida = true;
        } else {
            salida = false;
        }
        return salida;
    }

    private boolean comprobarDNI( String text ) {
        boolean salida = true;
        if(text.length()!=9 || !Character.isLetter( text.charAt( 8 ) ))salida = false;
        else{
            try {
                Integer.parseInt(text.substring( 0,8 ));
            } catch (NumberFormatException excepcion) {
                salida = false;
            }
        }
        return salida;
    }

    @javafx.fxml.FXML
    public void detallesalumno(ActionEvent actionEvent) {
        if (Sesion.getAlumnoSelected() != null) App.loadFXML("editar-alumno.fxml" , "Editar alumno" );
    }

    @javafx.fxml.FXML
    public void detallesempresa(ActionEvent actionEvent) {
        App.loadFXML("empresa-view.fxml" , "Empresas" );
    }

    @javafx.fxml.FXML
    public void cerrarsesion(ActionEvent actionEvent) {
        Sesion.setAlumnoSelected(null);
        App.loadFXML("login-view.fxml", "Login");
    }
}
