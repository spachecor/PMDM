package com.spachecor.librosmart.model.entity;

import java.util.List;

public class Lista {
    private String nombre;
    private List<Libro> libros;

    public Lista() {}

    public Lista(String nombre) {
        this.nombre = nombre;
    }

    public Lista(String nombre, List<Libro> libros) {
        this.nombre = nombre;
        this.libros = libros;
    }

    @Override
    public String toString() {
        return "Lista{" +
                "nombre='" + nombre + '\'' +
                ", libros=" + libros +
                '}';
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
}
