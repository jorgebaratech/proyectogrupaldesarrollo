package com.example.actividad;

import com.example.alumno.Alumno;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "actividad")
public class Actividad {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private java.sql.Date fecha;
  @Enumerated(EnumType.STRING)
  private String observacion;
  private long horas;
  private TipoPractica tipo;
  private String actividad;


  @ManyToOne
  @JoinColumn(name = "alumno")
  private Alumno alumno;

  @Override
  public String toString() {
    return "DiaryActivity{" +
            "id: " + id +
            ", fecha: '" + fecha + '\'' +
            ", tipo: " + tipo +
            ", horas: " + horas +
            ", com.example.actividad: '" + actividad + '\'' +
            ", observacion: '" + observacion + '\'' +
            ", com.example.alumno: " + alumno +
            '}';
  }
}
