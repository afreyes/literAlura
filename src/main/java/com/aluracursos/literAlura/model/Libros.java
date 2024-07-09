package com.aluracursos.literAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Libros(

        @JsonAlias("id") String id,
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") ArrayList<Autor> autores,
        @JsonAlias("languages") ArrayList<String> idiomas,
        @JsonAlias("download_count") Integer descargas,
        @JsonAlias("subjects") ArrayList<String> categoria,
        @JsonAlias("media_type") String medio


) {
}