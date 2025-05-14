package com.jaczibeth.alumnos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroControl; // Se recomienda que los atributos comiencen en minúscula
    private String nombre;
    private String apellidos;
    private String telefono;
    private String email;
    private String carrera;
    private String imagenURL;

    public Alumno() {
    }

    public Alumno(Long id, String numeroControl, String nombre, String apellidos, String telefono, String email, String carrera, String imagenURL) {
        this.id = id;
        this.numeroControl = numeroControl;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.email = email;
        this.carrera = carrera;
        this.imagenURL = imagenURL;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroControl() {
        return numeroControl;
    }

    public void setNumeroControl(String numeroControl) {
        this.numeroControl = numeroControl;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    @Override
    public String toString() {
        return "Alumno{" +
                "id=" + id +
                ", numeroControl='" + numeroControl + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", carrera='" + carrera + '\'' +
                ", imagenURL='" + imagenURL + '\'' +
                '}';
    }
}
