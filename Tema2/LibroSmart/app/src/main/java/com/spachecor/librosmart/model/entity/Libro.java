package com.spachecor.librosmart.model.entity;

import java.util.Objects;

public class Libro {
    private Long isbn;
    private String titulo;
    private String autor;
    private Integer nPaginas;
    private String opinion;

    public Libro() {}

    public Libro(Long isbn, String titulo, String autor, Integer nPaginas, String opinion) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.nPaginas = nPaginas;
        this.opinion = opinion;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "isbn=" + isbn +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", nPaginas=" + nPaginas +
                ", opinion='" + opinion + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return Objects.equals(isbn, libro.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isbn);
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getnPaginas() {
        return nPaginas;
    }

    public void setnPaginas(Integer nPaginas) {
        this.nPaginas = nPaginas;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}
