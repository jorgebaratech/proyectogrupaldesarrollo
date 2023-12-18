package com.example.profesor;

import com.example.alumno.Alumno;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "profesor")
public class Profesor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String nombre;
  private String apellidos;
  private String email;
  private String contrasenya;

  @OneToMany(mappedBy = "tutor", fetch = FetchType.EAGER)
  private List<Alumno> alumnos;
  @Override
  public String toString( ) {
    return "Profesor{" +
            "id=" + id +
            ", nombre='" + nombre + '\'' +
            ", apellidos='" + apellidos + '\'' +
            ", contrasenya='" + contrasenya + '\'' +
            ", email='" + email + '\'' +
            ", alumnos=" + alumnos.size() +
            '}';
  }
}
