package org.ags.mockito.ejemplos;

import org.ags.mockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {

    public static final List<Examen> EXAMENES = Arrays.asList(new Examen(5L, "Matematicas"), new Examen(7L, "Historia"),
            new Examen(6L, "Lengua"));

    public static final List<Examen> EXAMENES_ID_NULL = Arrays.asList(new Examen(null, "Matematicas"), new Examen(null, "Historia"),
            new Examen(null, "Lengua"));

    public static final List<Examen> EXAMENES_ID_NEGATIVOS = Arrays.asList(new Examen(-5L, "Matematicas"), new Examen(-6L, "Historia"),
            new Examen(null, "Lengua"));

    public static final List<String> PREGUNTAS = Arrays.asList("Aritmetica", "Integrales","Derivadas","Trigonometria","Geometria");

    public static final Examen EXAMEN = new Examen(null, "Fisica");
}
