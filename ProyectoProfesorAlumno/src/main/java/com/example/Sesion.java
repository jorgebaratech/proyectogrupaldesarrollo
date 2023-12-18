package com.example;

import com.example.actividad.Actividad;
import com.example.alumno.Alumno;
import com.example.empresas.Empresas;
import com.example.profesor.Profesor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Sesion {
    @Setter
    @Getter
    private static Profesor profesorLogged;

    @Setter
    @Getter
    private static Alumno alumnoSelected;

    @Setter
    @Getter
    private static Alumno currentAlumno;

    @Setter
    @Getter
    private static Actividad ActividadPulsada;

    @Setter
    @Getter
    private static Empresas EmpresaSelected;

    public static void cerrarsesion(){
        profesorLogged = null;
        alumnoSelected = null;
    }
}