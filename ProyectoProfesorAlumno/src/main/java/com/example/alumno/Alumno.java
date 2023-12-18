package com.example.alumno;


import com.example.empresas.Empresas;
import com.example.actividad.Actividad;
import com.example.profesor.Profesor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "alumno")
public class Alumno {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String nombre;
  private String apellidos;
  private String contraseña;
  private long dni;
  private java.sql.Date nacimiento;
  private String email;
  private long telefono;
  private long horasDual;
  private long horasFct;
  private String observaciones;

  @OneToMany(mappedBy = "alumno")
  private List<Actividad> actividad;

  @ManyToOne
  @JoinColumn(name = "empresas")
  private Empresas empresas;

  @ManyToOne
  @JoinColumn(name = "tutor")
  private Profesor tutor;


  public String toString( ) {

    String nombreEmpresa = "Sin empresa";
    String nombreTutor = "Sin tutor";

    Integer ActividadesDiarias = 0;
    if (this.tutor!=null) nombreTutor = this.tutor.getNombre();
    if (this.empresas!=null) nombreEmpresa = this.empresas.getNombre();
    return "Student{" +
            "id=" + id +
            ", nombre='" + nombre + '\'' +
            ", apellidos='" + apellidos + '\'' +
            ", contraseña='" + contraseña + '\'' +
            ", dni='" + dni + '\'' +
            ", nacimiento='" + nacimiento + '\'' +
            ", email='" + email + '\'' +
            ", telefono='" + telefono + '\'' +
            ", empresa='" + empresas + '\'' +
            ", tutor='" + tutor + '\'' +
            ", horasDual=" + horasDual +
            ", horasFct=" + horasFct +
            ", observaciones='" + observaciones + '\'' +
            ", company=" + nombreEmpresa +
            ", tutor=" + nombreTutor +
            '}';
  }
}
