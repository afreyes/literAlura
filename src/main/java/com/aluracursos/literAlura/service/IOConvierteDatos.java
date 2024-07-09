package com.aluracursos.literAlura.service;

public interface IOConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
