package com.aluracursos.literAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public record Autor(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") Integer nacimiento,
        @JsonAlias("death_year") Integer deceso
)

{
}