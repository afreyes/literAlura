package com.aluracursos.literAlura.principal;

import com.aluracursos.literAlura.model.*;


import com.aluracursos.literAlura.repositorio.AutorBDRepository;
import com.aluracursos.literAlura.repositorio.LibroBDrepositorio;
import com.aluracursos.literAlura.service.ConsumoAPI;
import com.aluracursos.literAlura.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class Principal {



    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private Scanner input = new Scanner(System.in);
    private final String direccion_URL = "https://gutendex.com/books/?page=1&search=";
    private ConvierteDatos conversor = new ConvierteDatos();
    public String textoBusqueda;

    private LibroBDrepositorio repositoriolibro; // acceso CRUD
    private AutorBDRepository repositorioautor;
    public Principal(LibroBDrepositorio repositoriolibro, AutorBDRepository repositorioautor) {// acceso CRUD
        this.repositorioautor=repositorioautor;
        this.repositoriolibro=repositoriolibro;
    }


    public void mostrarMenu() {
        var opcion = -1;
        var menu = """
                           
                             MENU PRINCIPAL 
                --------------------------------------------
                1 - Buscar Libros por TÍtulo
                2 - Buscar Autor por Nombre
                3 - Listar Libros Registrados
                4 - Listar Autores Registrados
                5 - Listar Autores Vivos
                6 - Listar Libros por Idioma
                7 - Buscar Libros por TÍtulo BULK
                8 - Top 10 Libros más Buscados
                9 - Generar Estadísticas
                ----------------------------------------------
                0 -  SALIR DEL PROGRAMA 
                ----------------------------------------------
                Elija una opción:
                """;

        while (opcion != 0) {
            System.out.println(menu);
            try {
                opcion = Integer.valueOf(input.nextLine());
                switch (opcion) {
                    case 1:
                        BusquedaInicial();
                        break;
                    case 2:
                        buscarAutorPorNombre();
                        break;
                    case 3:
                        listarLibrosRegistrados();
                        break;
                    case 4:
                        listarAutoresRegistrados();
                        break;
                    case 5:
                        listarAutoresVivos();
                        break;
                    case 6:
                        listarLibrosPorIdioma();
                        break;

                    case 8:
                        top10Libros();
                        break;
                    case 9:
                        generarEstadisticas();
                        break;
                    case 7:
                        BusquedaInicialArray();
                        break;
                    case 0:

                        System.out.println("""
                        Cerrando la aplicacion Literalura  ...
                        “De los diversos instrumentos inventados por el hombre, el más asombroso es el libro;
                         todos los demás son extensiones de su cuerpo… Sólo el libro es una extensión de la imaginación y la memoria”.
                          Jorge Luis Borges.
                          """);
                        break;
                    default:
                        System.out.println("Opción no válida!");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Opción no válida: " + e.getMessage());

            }
        }
    }


    public void BusquedaInicial() {
        System.out.println("Escribe el titulo del libro que quieres buscar");
        var textoBusqueda = input.nextLine();
        // textoBusqueda = "A Room with a View";
        System.out.println(direccion_URL + textoBusqueda.replace(" ", "+"));
        var json = consumoAPI.obtenerDatos(direccion_URL + textoBusqueda.replace(" ", "+"));

        Resultado resultados = conversor.obtenerDatos(json, Resultado.class);

        if (json.isEmpty() || !json.contains("\"count\":0,\"next\":null,\"previous\":null,\"results\":[]")) {


            Optional<Libros> datosLibro = resultados.resultados().stream()
                    .filter(libros -> libros.titulo().toLowerCase()
                            .contains(textoBusqueda.toLowerCase())).findFirst();

            if (datosLibro.isPresent()) {

                System.out.println("libro Encontrado");
                System.out.println(datosLibro.get().titulo());
                var nacimiento = datosLibro.get().autores().stream()
                        .map(autor -> autor.nacimiento().intValue()).findFirst();


                //  Opcion 1Datos directamente del dato encontrado
                System.out.println("\nAutor:" + datosLibro.get().autores().stream()
                        .map(autor -> autor.nombre()).limit(1).collect(Collectors.joining()) +
                        "\nNacimiento: " + datosLibro.get().autores().stream()
                        .map(autor -> autor.nacimiento().toString().replace("[", "").replace("[", "")).collect(Collectors.joining()) +
                        "\nDeceso: " + datosLibro.get().autores().stream()
                        .map(autor -> autor.deceso().toString().replace("[", "").replace("[", "")).collect(Collectors.joining()) +
                        " \nidiomas: " + datosLibro.get().idiomas().stream().collect(Collectors.joining()) +
                        " \ndescargas: " + datosLibro.get().descargas());

                // Opcion 2: Datos a traves del Array, las variables llegan con su tipo, puedo calcular la edad
                ArrayList<Autor> autor = datosLibro.get().autores();
                for (Autor autor1 : autor) {
//                    System.out.println("nombre:" + autor1.nombre());
//                    System.out.println("Nacimientos:" + autor1.nacimiento());
//                    System.out.println("Fallecimiento:" + autor1.deceso());
                    var edad = autor1.nacimiento().intValue() - autor1.deceso().intValue();
                    System.out.println("Edad del Autor" + edad);
                }

                try {
                    List<LibroBD> librosEncontrados = datosLibro.stream()
                            .map(libros -> new LibroBD(libros)).collect(Collectors.toList());
                    AutorBD autorLibro = datosLibro.stream()
                            .flatMap(libros -> libros.autores().stream()
                                    .map(autor2 -> new AutorBD(autor2))).collect(Collectors.toList())
                            .stream().findFirst().get();
                    Optional<AutorBD> buscarAutorenBD = repositorioautor.findByNombreContainsIgnoreCase(datosLibro.get().autores().stream()
                            .map(autor2 -> autor2.nombre())
                            .collect(Collectors.joining()));
                    Optional<LibroBD> buscarLibroBD = repositorioautor.buscarLibroPorNombre(textoBusqueda);
                    if (buscarLibroBD.isPresent()) {
                        System.out.println(" El libro ya esta en la base de datos");
                    } else {
                        AutorBD autorsalvarBase;
                        if (buscarAutorenBD.isPresent()) {
                            autorsalvarBase = buscarAutorenBD.get();
                            System.out.println(" El autor ya esta en la base de datos");
                        } else {
                            autorsalvarBase = autorLibro;
                            repositorioautor.save(autorsalvarBase);
                        }
                        autorsalvarBase.setLibros(librosEncontrados);
                        repositorioautor.save(autorsalvarBase);


                    }


                } catch (Exception e) {
                    System.out.println("Warning! " + e.getMessage());
                }
            } else {
                System.out.println("Libro no encontrado!");
            }

        }
    }

    public void guardarLibroAutor(infoLibroAutor infoLibroAutor ) {
        if (infoLibroAutor != null) {
            System.out.println("en el metodo"+infoLibroAutor.getAutor());
            repositorioautor.save(infoLibroAutor.getAutor());
            infoLibroAutor.getLibro().setAutor(infoLibroAutor.getAutor());
            repositoriolibro.save(infoLibroAutor.getLibro());
            System.out.println("en el metodo"+infoLibroAutor.getLibro());
        }
    }

    public void BusquedaInicialArray() {
        System.out.println("Escribe el titulo del libro que quieres buscar");
        var textoBusqueda = input.nextLine();
        // textoBusqueda = "A Room with a View";
        System.out.println(direccion_URL + textoBusqueda.replace(" ", "+"));
        var json = consumoAPI.obtenerDatos(direccion_URL + textoBusqueda.replace(" ", "+"));

        Resultado librosEncontrados = conversor.obtenerDatos(json, Resultado.class);

        if (json.isEmpty() || !json.contains("\"count\":0,\"next\":null,\"previous\":null,\"results\":[]")) {


            System.out.println(librosEncontrados);
            List<LibroBD> librosParaBase = new ArrayList<>();
            librosParaBase = librosEncontrados.resultados().stream()
                    .map(LibroBD::new)
                    .collect(Collectors.toList());
            // System.out.println(librosParaBase);
            for (LibroBD librosBD : librosParaBase) {
                System.out.println(librosBD);
            }
            List<AutorBD> autorParaBase = new ArrayList<>();
            autorParaBase = librosEncontrados.resultados().stream()
                    .flatMap(libros -> libros.autores().stream()
                            .map(autor -> new AutorBD(autor)))
                    .collect(Collectors.toList());
            for (AutorBD autorBD : autorParaBase) {
                //  System.out.println(autorBD);
            }

            List<infoLibroAutor> listaLibroAutores = new ArrayList<>();


            for (int i = 0; i < librosParaBase.size(); i++) {
                infoLibroAutor infoLibroAutor = new infoLibroAutor(librosParaBase.get(i), autorParaBase.get(i));
                listaLibroAutores.add(infoLibroAutor);
            }
            //  listaLibroAutores.forEach(System.out::println);

            for (infoLibroAutor libroAutor : listaLibroAutores) {
                System.out.println( libroAutor.getAutor().getNombre());
                System.out.println( libroAutor.getLibro().getTitulo());
                Optional<AutorBD> buscarAutorenBD = repositorioautor.findByNombreContainsIgnoreCase(libroAutor.getAutor().getNombre());
                Optional<LibroBD> buscarLibroBD = repositoriolibro.findByTituloContainsIgnoreCase(libroAutor.getLibro().getTitulo());

                try {
                    if (buscarLibroBD.isPresent()) {
                        System.out.println(" El libro ya esta en la base de datos");
                    } else {

                        if (buscarAutorenBD.isPresent()) {
                            LibroBD libronuevo;
                            libronuevo = libroAutor.getLibro();
                            libronuevo.setAutor(buscarAutorenBD.get());
                            System.out.println(libronuevo);
                            repositoriolibro.save(libronuevo);
                            System.out.println(" El autor ya sera en la base de datos");
                        } else {
                            //  System.out.println("autor no presente");
                            AutorBD autornuevo= libroAutor.getAutor();
                            LibroBD libronuevo = libroAutor.getLibro();
                            //  System.out.println("22222222222222"+libronuevo);
                            // System.out.println("11111111111111"+autornuevo);
                            infoLibroAutor infoLibroAutor = new infoLibroAutor(libronuevo, autornuevo);
                            guardarLibroAutor(infoLibroAutor);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Atencion! " + e.getMessage());
                }
//

            }
        } else {
            System.out.println("Libro no encontrado!");
        }
    }


    // metodo desahabilitado
    public void BusquedaLibros() {
//        textoBusqueda = "quijote";
        System.out.println(direccion_URL + textoBusqueda.replace(" ", "+"));
        var json = consumoAPI.obtenerDatos(direccion_URL + textoBusqueda.replace(" ", "+"));

        Resultado librosEncontrados = conversor.obtenerDatos(json, Resultado.class);
        System.out.println(librosEncontrados);
        List<LibroBD> librosParaBase = new ArrayList<>();
        librosParaBase = librosEncontrados.resultados().stream()
                .map(LibroBD::new)
                .collect(Collectors.toList());
        System.out.println(librosParaBase);
        for (LibroBD librosBD : librosParaBase) {
            System.out.println(librosBD);
        }
        List<AutorBD> autorParaBase = new ArrayList<>();
        autorParaBase = librosEncontrados.resultados().stream()
                .flatMap(libros -> libros.autores().stream()
                        .map(autor -> new AutorBD(autor)))
                .collect(Collectors.toList());
        for (AutorBD autorBD : autorParaBase) {
            System.out.println(autorBD);
        }


    }

    public void buscarAutorPorNombre() {
        System.out.println("Escribe el nombre del autor");
        var nombreAutor = input.nextLine();

        Optional<AutorBD> autorBuscado = repositorioautor.findByNombreContainsIgnoreCase(nombreAutor);
        if (autorBuscado.isPresent()) {
            System.out.println(autorBuscado.get());
        }

    }

    public void listarLibrosRegistrados() {
        System.out.println("Lista de libros registrados");
        List<LibroBD> libros = repositorioautor.buscarTodosLosLibros();
        System.out.println(libros);

    }

    public void listarAutoresRegistrados() {
        System.out.println("Lista de autores registrados");
        List<AutorBD> autores = repositorioautor.findAll();
        System.out.println(autores);

    }

    public void listarAutoresVivos() {
        System.out.println("Por favor escribe el año en cuestion");
        var anio = input.nextInt();
        List<AutorBD> autores = repositorioautor.buscarAutoresVivos(anio);
        System.out.println(autores);
        for (AutorBD autor :autores)
        {
            System.out.println("nombre:"+autor.getNombre());
            var edad=anio-autor.getNacimiento();
            System.out.println("tenia esta "+edad+" en ese año");


        }
    }

    public void listarLibrosPorIdioma() {
        List <String> idiomas= repositoriolibro.EncontrarlosIdimoas();
        System.out.println("lista de abrevitauras de idiomas existentes en la base");
        for ( String i :idiomas) {
            System.out.println(i);
        }

        System.out.println("Escribe la abreviatura del idioma ");
        var idioma = input.nextLine();
        List<LibroBD> libros = repositorioautor.buscarPorIdima(idioma);
        System.out.println(libros);

    }

    public void top10Libros () {
        System.out.println("""
        TOP 10 LIBROS MÁS BUSCADOS 
                            """);
        List<LibroBD> libros = repositoriolibro.findTop10ByOrderByDescargasDesc();
        System.out.println();
        libros.forEach(l -> System.out.println(

                "\nTítulo: " + l.getTitulo() +
                        "\nAutor: " + l.getAutor().getNombre() +
                        "\nNúmero de descargas: " + l.getDescargas()));
    }
    public void generarEstadisticas () {
        System.out.println("""
               GENERAR ESTADÍSTICAS 
                     """);
        var json = consumoAPI.obtenerDatos(direccion_URL);
        var datos = conversor.obtenerDatos(json, Resultado.class);
        IntSummaryStatistics estadisticas = datos.resultados().stream()
                .filter(libros -> libros.descargas()>0)
                .collect(Collectors.summarizingInt(Libros::descargas));
//
        Integer media = (int) estadisticas.getAverage();

        System.out.println("Media de descargas: " + media);
        System.out.println("Máxima de descargas: " + estadisticas.getMax());
        System.out.println("Mínima de descargas: " + estadisticas.getMin());
        System.out.println("Total registros para calcular las estadísticas: " + estadisticas.getCount());

    }
}