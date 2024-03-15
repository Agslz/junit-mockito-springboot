package org.ags.mockito.ejemplos.services;

import org.ags.mockito.ejemplos.models.Examen;
import org.ags.mockito.ejemplos.repositories.ExamenRepository;
import org.ags.mockito.ejemplos.repositories.PreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    @Mock
    ExamenRepository repository;
    @Mock
    PreguntaRepository preguntaRepository;

    @InjectMocks
    ExamenServiceImpl examenService;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        repository = mock(ExamenRepositoryImpl.class);
//        preguntaRepository = mock(PreguntaRepositoryImpl.class);
//        service = new ExamenServiceImpl(repository, preguntaRepository);
    }

    @Test
    @DisplayName("Encontar examen por nombre")
    void testFindExamenPorNombre() {

        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        Optional<Examen> examen = examenService.findExamenPorNombre("Matematicas");

        assertTrue(examen.isPresent());

        assertEquals(5L, examen.orElseThrow().getId());
        assertEquals("Matematicas", examen.get().getNombre());
    }

    @Test
    @DisplayName("Encontar examen por nombre con lista vacia")
    void testFindExamenPorNombreListaVacia() {

        List<Examen> datos = Collections.emptyList();

        when(repository.findAll()).thenReturn(datos);
        Optional<Examen> examen = examenService.findExamenPorNombre("Matematicas");

        assertFalse(examen.isPresent());
    }

    @Test
    @DisplayName("Preguntas de examen")
    void testPreguntasExamen() {

        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Trigonometria"));

    }

    @Test
    @DisplayName("Preguntas de examen con Verify")
    void testPreguntasExamenVerify() {

        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Trigonometria"));

        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong()); //Forzamos a que funcione con cualquier Long
    }

    @Test
    @DisplayName("Preguntas de no existe examen con Verify")
    void testNoExisteExamenVerify() {

        when(repository.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");

        assertNull(examen);

        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(5L);
    }

    @Test
    @DisplayName("Guardar examen")
    void testGuardarExamen() {

        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        when(repository.guardar(any(Examen.class))).thenReturn(Datos.EXAMEN);
        Examen examen = examenService.guardar(newExamen);
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());

        verify(repository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVariasPreguntas(anyList());
    }
}