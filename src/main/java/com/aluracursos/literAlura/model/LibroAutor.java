package com.aluracursos.literAlura.model;

public class LibroAutor {

    private LibroBD libro;
    private AutorBD autor;

    public LibroAutor(LibroBD libro, AutorBD autor) {
        this.libro = libro;
        this.autor=autor;
    }


    public LibroBD getLibro() {
        return libro;
    }

    public void setLibro(LibroBD libro) {
        this.libro = libro;
    }

    @Override
    public String toString() {
        return "LibroAutor{" +
                "libro=" + libro +
                ", autor=" + autor +
                '}';
    }

    public AutorBD getAutor() {
        return autor;
    }

    public void setAutor(AutorBD autor) {
        this.autor = autor;
    }


}