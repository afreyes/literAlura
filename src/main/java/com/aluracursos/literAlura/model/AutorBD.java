package com.aluracursos.literAlura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="autores")
public class AutorBD {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String nombre;
    private Integer nacimiento;
    private Integer deceso;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<LibroBD> libros;

    public List<LibroBD> getLibros() {
        return libros;
    }

    public void setLibros(List<LibroBD> libros) {
        for (LibroBD books : libros)
        {libros.forEach(l -> l.setAutor(this));
            this.libros=libros;
        }}
    AutorBD(){}
    public AutorBD(Autor autor) {

        this.nombre = autor.nombre();
        this.nacimiento = autor.nacimiento();
        this.deceso = autor.deceso();}

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(Integer nacimiento) {
        this.nacimiento = nacimiento;
    }

    public Integer getDeceso() {
        return deceso;
    }

    public void setDeceso(Integer deceso) {
        this.deceso = deceso;
    }

    @Override
    public String toString() {
        return "AutorBD{" +
                "nombre='" + nombre + '\'' +
                ", nacimiento=" + nacimiento +
                ", deceso=" + deceso +
                '}';
    }
}