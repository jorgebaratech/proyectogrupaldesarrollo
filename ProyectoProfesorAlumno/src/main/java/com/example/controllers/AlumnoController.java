package com.example.controllers;

import com.example.App;
import com.example.Sesion;
import com.example.actividad.ActividadDAO;
import com.example.actividad.Actividad;
import com.example.actividad.TipoPractica;
import com.example.alumno.Alumno;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;


import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class AlumnoController implements Initializable{

    @javafx.fxml.FXML
    private DatePicker dataalumno;
    @javafx.fxml.FXML
    private ComboBox<TipoPractica> comboboxdual;
    @javafx.fxml.FXML
    private Spinner<Integer> spinnerhoras;
    @javafx.fxml.FXML
    private TextArea txtActividad;
    @javafx.fxml.FXML
    private TextArea txtObservaciones;
    @javafx.fxml.FXML
    private TableView<Actividad> tablaalumno;
    @javafx.fxml.FXML
    private TableColumn<Actividad,String> cfecha;
    @javafx.fxml.FXML
    private TableColumn<Actividad,String> cpractica;
    @javafx.fxml.FXML
    private TableColumn<Actividad,String> choras;
    @javafx.fxml.FXML
    private TableColumn<Actividad,String> cactividad;
    @javafx.fxml.FXML
    private TableColumn<Actividad,String> cincidencias;
    @javafx.fxml.FXML
    private Button btnAñadirAlumno;
    @javafx.fxml.FXML
    private Button btnEliminarAlumno;
    @javafx.fxml.FXML
    private Button btnCerrarSesion;

    private ObservableList<Actividad>observableListDiaryActivity;

    private ActividadDAO ActividadDAO = new ActividadDAO();

    @Deprecated
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(Sesion.getCurrentAlumno());
        choras.setCellValueFactory((fila) -> {
            String horas = String.valueOf(fila.getValue().getHoras());
            return new SimpleStringProperty(horas);
        });

        cfecha.setCellValueFactory((fila) -> {
            LocalDate fecha = fila.getValue().getFecha().toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String fechaFormateada = fecha.format(formatter);
            return new SimpleStringProperty(fechaFormateada);
        });

        cpractica.setCellValueFactory((fila) -> {
            String practica = String.valueOf(fila.getValue().getTipo());
            return new SimpleStringProperty(practica);
        });

        cactividad.setCellValueFactory((fila) -> {
            String actividad = String.valueOf(fila.getValue().getActividad());
            return new SimpleStringProperty(actividad);
        });

        cincidencias.setCellValueFactory((fila) -> {
            String incidencias = String.valueOf(fila.getValue().getObservacion());
            return new SimpleStringProperty(incidencias);
        });

        Alumno selectedAlumno = Sesion.getAlumnoSelected();
        if (selectedAlumno != null) {
            txtActividad.setText(selectedAlumno.getObservaciones());
            txtObservaciones.setText(selectedAlumno.toString());
        } else {
            System.err.println("Error");
        }
        dataalumno.getValue();

        observableListDiaryActivity = FXCollections.observableArrayList();

        observableListDiaryActivity.setAll(ActividadDAO.getAll());

        ObservableList<TipoPractica> observableListPracticeType = FXCollections.observableArrayList();
        observableListPracticeType.setAll(TipoPractica.DUAL, TipoPractica.FCT);

        comboboxdual.setItems(observableListPracticeType);
        comboboxdual.getSelectionModel().selectFirst();

        spinnerhoras.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, 0, 1));

        tablaalumno.setItems(observableListDiaryActivity);

        tablaalumno.getSelectionModel().selectedItemProperty().addListener((observableValue, tarea, t1) -> {
            if (t1 != null) Sesion.setActividadPulsada(t1);
        });

        tablaalumno.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                Actividad selectedTarea = tablaalumno.getSelectionModel().getSelectedItem();
                if (selectedTarea != null) {
                    Sesion.setActividadPulsada(selectedTarea);
                    App.loadFXML("editar-actividad.fxml", "Editando tarea");
                }
            }
        });
    }

    @Deprecated
    public void añadiralumno(ActionEvent actionEvent) {
        if (dataalumno.getValue() == null || !fechaIsValid(dataalumno.getValue())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Introduce una fecha válida");
            alert.show();
        } else if (spinnerhoras.getValue() < 1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("El tiempo mínimo de la actividad debe ser mínimo 1 hora");
            alert.show();
        } else if (txtActividad.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Introduce la actividad realizada");
            alert.show();
        } else {
            Actividad actividad = new Actividad();
            actividad.setId(Sesion.getCurrentAlumno().getId());
            actividad.setFecha(Date.valueOf(dataalumno.getValue()));
            actividad.setTipo(comboboxdual.getValue());
            actividad.setHoras(spinnerhoras.getValue());
            actividad.setAlumno(Sesion.getCurrentAlumno());
            actividad.setActividad(txtActividad.getText());
            actividad.setObservacion(txtObservaciones.getText());
            ActividadDAO.save(actividad);
            observableListDiaryActivity.setAll(ActividadDAO.getAll());
            tablaalumno.setItems(observableListDiaryActivity);
        }

        try {
            TipoPractica tipoPractica = comboboxdual.getValue();
            Integer horas = spinnerhoras.getValue();
            LocalDate fecha = dataalumno.getValue();
            String actividad = txtActividad.getText();
            String incidencias = txtObservaciones.getText();

            Actividad nuevaActividad = new Actividad();
            nuevaActividad.setTipo(tipoPractica);
            nuevaActividad.setHoras(horas);
            nuevaActividad.setFecha(Date.valueOf(fecha));
            nuevaActividad.setActividad(actividad);
            nuevaActividad.setObservacion(incidencias);
        } catch (Exception e) {
            e.printStackTrace();
        }
        observableListDiaryActivity.setAll(ActividadDAO.getAll());
        tablaalumno.setItems(observableListDiaryActivity);
    }
    private boolean fechaIsValid(LocalDate fecha) {
        String formatoFecha = "\\d{2}/\\d{2}/\\d{4}";
        return fecha != null && fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).matches(formatoFecha);
    }

    @FXML
    public void eliminaralumno(ActionEvent actionEvent) {
        Actividad tareaSeleccionada = tablaalumno.getSelectionModel().getSelectedItem();

        if (tareaSeleccionada != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("¿Quieres borrar la tarea: " + tareaSeleccionada.getId() + "?");
            var result = alert.showAndWait().get();
            if (result.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                ActividadDAO actividadDAO = new ActividadDAO();
                actividadDAO.delete(tareaSeleccionada);
                observableListDiaryActivity.remove(tareaSeleccionada);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Seleccione la tarea a eliminar.");
            alert.showAndWait();
        }
    }
    @javafx.fxml.FXML
    public void cerrarsesion(ActionEvent actionEvent) {
        Sesion.cerrarsesion();
        App.loadFXML("login-view.fxml", "Login");
    }
}
