package com.spachecor.librosmart.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lista {
    private String nombre;
    private List<Libro> libros;

    public Lista() {}

    public Lista(String nombre) {
        this.nombre = nombre;
        this.libros = new ArrayList<>();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lista lista = (Lista) o;
        return Objects.equals(nombre, lista.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nombre);
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
