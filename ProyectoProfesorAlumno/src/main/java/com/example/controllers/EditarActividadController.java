package com.example.controllers;

import com.example.App;
import com.example.Sesion;
import com.example.actividad.ActividadDAO;
import com.example.actividad.Actividad;
import com.example.actividad.TipoPractica;
import com.example.alumno.Alumno;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class EditarActividadController implements Initializable{
    @javafx.fxml.FXML
    private DatePicker datafecha;
    @javafx.fxml.FXML
    private ComboBox<TipoPractica> comboboxtipo;
    @javafx.fxml.FXML
    private Spinner<Integer> spinnerhoras;
    @javafx.fxml.FXML
    private TextArea txtActividad;
    @javafx.fxml.FXML
    private TextArea txtObservaciones;
    @javafx.fxml.FXML
    private Button btnGuardar;
    @javafx.fxml.FXML
    private Button btnVolver;

    private ObservableList<Actividad> observableListDiaryActivity;
    private ActividadDAO ActividadDAO = new ActividadDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableListDiaryActivity = FXCollections.observableArrayList();

        Actividad actividad = Sesion.getActividadPulsada();

        observableListDiaryActivity.setAll(ActividadDAO.getAll());
        ObservableList<TipoPractica> observableListTipoPractica = FXCollections.observableArrayList();
        observableListTipoPractica.setAll(TipoPractica.DUAL, TipoPractica.FCT);

        comboboxtipo.setItems(observableListTipoPractica);
        comboboxtipo.setValue(actividad.getTipo());
        spinnerhoras.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, 0, 1));
        spinnerhoras.getValueFactory().setValue((int) actividad.getHoras());


        datafecha.setValue(actividad.getFecha().toLocalDate());

        try {
            txtActividad.setText(actividad.getActividad());
            txtObservaciones.setText(actividad.getObservacion());
            Alumno selectedAlumno = Sesion.getAlumnoSelected();
            if (selectedAlumno != null) {
                txtActividad.setText(selectedAlumno.getObservaciones());
                txtObservaciones.setText(selectedAlumno.toString());
            } else {
                System.err.println("Error");
            }
        } catch (NullPointerException e) {
            System.err.println("Error al intentar establecer el texto: " + e.getMessage());
        }
    }

    @javafx.fxml.FXML
    public void guardar(ActionEvent actionEvent) {
        Actividad actividad = Sesion.getActividadPulsada();
        actividad.setActividad(txtActividad.getText());
        actividad.setObservacion(txtObservaciones.getText());
        actividad.setFecha(Date.valueOf(datafecha.getValue()));
        actividad.setTipo(comboboxtipo.getValue());
        actividad.setHoras(spinnerhoras.getValue());
        ActividadDAO.update(actividad);
        volver(actionEvent);
    }

    @javafx.fxml.FXML
    public void volver(ActionEvent actionEvent) {
        Sesion.cerrarsesion();
        App.loadFXML("alumno-view.fxml", "Alumno");
    }
}
