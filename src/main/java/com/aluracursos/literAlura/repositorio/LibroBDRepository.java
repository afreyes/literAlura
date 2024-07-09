package com.aluracursos.literAlura.repositorio;

import com.aluracursos.literAlura.model.LibroBD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroBDrepository extends JpaRepository<LibroBD,Long> {

    Optional<LibroBD> findByTituloContainsIgnoreCase(String titulo);

    List<LibroBD> findTop10ByOrderByDescargasDesc();

    @Query("SELECT DISTINCT l.idiomas FROM LibroBD l")
    List <String> EncontrarlosIdimoas();

}
