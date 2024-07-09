package com.aluracursos.literAlura;

import com.aluracursos.literAlura.principal.Principal;

import com.aluracursos.literAlura.repositorio.AutorBDRepository;

import com.aluracursos.literAlura.repositorio.LibroBDrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class literAluraApplication implements CommandLineRunner {
	@Autowired// acceso CRUD ( inyeccion de dependencias)
	private AutorBDRepository repositorioAutor;// acceso CRUD
	@Autowired
	private LibroBDrepository repositorioLibro;// acceso CRUD
	public static void main(String[] args) {
		SpringApplication.run(literAluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal= new Principal(repositorioLibro,repositorioAutor );
		//principal.BusquedaInicialArray();
		principal.mostrarMenu();
//		principal.BusquedaLibros();

	}
}
