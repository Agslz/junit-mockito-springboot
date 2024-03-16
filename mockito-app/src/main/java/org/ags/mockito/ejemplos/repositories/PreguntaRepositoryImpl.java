package org.ags.mockito.ejemplos.repositories;

import org.ags.mockito.ejemplos.Datos;

import java.util.List;

public class PreguntaRepositoryImpl implements PreguntaRepository{
    @Override
    public List<String> findPreguntasPorExamenId(Long id) {
        System.out.println("PreguntaRepositoryImpl.findPreguntasPorExamenId");
        return Datos.PREGUNTAS;
    }

    @Override
    public void guardarVariasPreguntas(List<String> preguntas) {
        System.out.println("PreguntaRepositoryImpl.guardarVariasPreguntas");
    }
}
