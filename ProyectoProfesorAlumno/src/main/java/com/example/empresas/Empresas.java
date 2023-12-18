package com.example.empresas;

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
@Table(name = "empresas")
public class Empresas {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private long telefono;
  private String email;
  private String responsable;
  private String observaciones;
  private String nombre;

  @OneToMany(mappedBy = "empresas", fetch = FetchType.EAGER)
  private List<Alumno> alumno;

  @Override
  public String toString() {
    return "Empresas{" +
            "id=" + id +
            ", nombre='" + nombre + '\'' +
            ", telefono='" + telefono + '\'' +
            ", email='" + email + '\'' +
            ", responsable='" + responsable + '\'' +
            ", observaciones='" + observaciones + '\'' +
            ", alumno=" + alumno +
            '}';
  }
}