package com.example.controllers;

import com.example.App;
import com.example.Sesion;
import com.example.actividad.TipoPractica;
import com.example.empresas.Empresas;
import com.example.empresas.EmpresasDAO;
import com.example.actividad.Actividad;
import com.example.profesor.Profesor;
import com.example.profesor.ProfesorDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmpresaController implements Initializable{

    EmpresasDAO empresasDAO;

    @javafx.fxml.FXML
    private TextField txtNombre;
    @javafx.fxml.FXML
    private TextField txtTelefono;
    @javafx.fxml.FXML
    private TextField txtCorreo;
    @javafx.fxml.FXML
    private TextField txtResponsable;
    @javafx.fxml.FXML
    private TextArea txtObservaciones;
    @javafx.fxml.FXML
    private TableView<Empresas> tablaEmpresa;
    @javafx.fxml.FXML
    private TableColumn<Empresas,String> cnombre;
    @javafx.fxml.FXML
    private TableColumn<Empresas,String> ccorreo;
    @javafx.fxml.FXML
    private TableColumn<Empresas,String> cresponsable;
    @javafx.fxml.FXML
    private TableColumn<Empresas,String> cobservaciones;
    @javafx.fxml.FXML
    private TableColumn<Empresas,String> ctelefono;
    @javafx.fxml.FXML
    private ComboBox<String> comboboxempresas;
    @javafx.fxml.FXML
    private Button btnnuevaempresa;
    @javafx.fxml.FXML
    private Button btnatras;
    @javafx.fxml.FXML
    private Button btncerrarsesion;
    @FXML
    private Label labelprofesor;
    @FXML
    private Button btnEditarEmpresa;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        empresasDAO = new EmpresasDAO();
        fillTable();

        List<String> nombresEmpresa = new ArrayList<>(  );
        empresasDAO.getAll().forEach( s -> nombresEmpresa.add( s.getNombre() ) );
        comboboxempresas.getItems().addAll(nombresEmpresa);
        comboboxempresas.setValue( comboboxempresas.getItems().getFirst() );


        tablaEmpresa.getSelectionModel( ).selectedItemProperty( ).addListener( ( observableValue , company , t1 ) -> {
            Sesion.setEmpresaSelected( t1 );
        } );

        tablaEmpresa.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                Empresas selectedEmpresa = tablaEmpresa.getSelectionModel().getSelectedItem();
                if (selectedEmpresa != null) {
                    Sesion.setEmpresaSelected(selectedEmpresa);
                    App.loadFXML("editar-empresa.fxml", "Edición de empresa");
                }
            }
        });

    }

    private void fillTable( ) {
        EmpresasDAO empresasDAO = new EmpresasDAO( );
        List<Empresas> empresas = empresasDAO.getAll( );
        Profesor profesor = Sesion.getProfesorLogged();
        labelprofesor.setText(profesor.getNombre()+" "+profesor.getApellidos());

        cnombre.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getNombre()
                ));
        ctelefono.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                ));
        ccorreo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmail()
                ));
        cresponsable.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getResponsable()
                ));
        cobservaciones.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getObservaciones()
                ));

        ObservableList<Empresas> observableList = FXCollections.observableArrayList( );
        observableList.addAll( empresas );
        tablaEmpresa.setItems( observableList );
    }

    @FXML
    public void click(Event event) {
    }


    @FXML
    public void volver(ActionEvent actionEvent) {
        App.loadFXML("profesor-view.fxml","Profesor");
    }

    @Deprecated
    public void cerrarsesion(ActionEvent actionEvent) {
        Sesion.cerrarsesion();
        App.loadFXML("login-view.fxml" , "Login" );
    }


    @Deprecated
    public void nuevaempresa(ActionEvent actionEvent) {
        if (txtNombre.getText().length()<2){
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "El nombre debe tener minimo 2 caracteres" );
            alert.show();
        }else if (!validatePhone(txtTelefono.getText())) {
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "Numero de teléfono introducido no válido" );
            alert.show();
        } else if (!validateEmail(txtCorreo.getText())) {
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "Email no válido" );
            alert.show();
        }else if (txtResponsable.getText().length()<3) {
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "El nombre del tutor debe tener minimo 3 caracteres" );
            alert.show();
        } else if (txtObservaciones.getText().length() < 3) {
            Alert alert = new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "Ingrese minimo 3 caracteres en las observaciones" );
            alert.show();
        }
        else {
            Empresas nuevaEmpresa = new Empresas();
            nuevaEmpresa.setNombre(txtNombre.getText());
            nuevaEmpresa.setEmail(txtCorreo.getText());
            nuevaEmpresa.setResponsable(txtResponsable.getText());
            nuevaEmpresa.setTelefono(Long.parseLong(txtTelefono.getText()));
            nuevaEmpresa.setObservaciones(txtObservaciones.getText());
            empresasDAO.insertarEmpresa(nuevaEmpresa);
            comboboxempresas.getItems().add(nuevaEmpresa.getNombre());
            fillTable( );
        }
    }

    protected boolean validateEmail( String text ) {
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

    protected boolean validatePhone( String text ) {
        boolean salida = true;
        try {
            Integer.parseInt(text);
            if (text.length()!=9){
                salida=false;
            }
        } catch (NumberFormatException excepcion) {
            salida = false;
        }
        return salida;
    }

    @Deprecated
    public void editarempresa(ActionEvent actionEvent) {
        if (Sesion.getEmpresaSelected() != null) App.loadFXML("editar-empresa.fxml" , "Editar empresa" );
    }
}
