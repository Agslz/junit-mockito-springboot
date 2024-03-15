package org.ags.mockito.ejemplos.repositories;

import org.ags.mockito.ejemplos.models.Examen;

import java.util.List;

public interface ExamenRepository {

    Examen guardar(Examen examen);
    List<Examen> findAll();

}
