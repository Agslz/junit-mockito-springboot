package org.ags.mockito.ejemplos.services;

import org.ags.mockito.ejemplos.models.Examen;
import org.ags.mockito.ejemplos.repositories.ExamenRepository;
import org.ags.mockito.ejemplos.repositories.PreguntaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

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

    @Captor
    ArgumentCaptor<Long> captor;

    @InjectMocks
    ExamenServiceImpl examenService;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        repository = mock(ExamenRepositoryImpl.class);
//        preguntaRepository = mock(PreguntaRepositoryImpl.class);
//        service = new ExamenServiceImpl(repository, preguntaRepository);
    }

    public static class MiArgsMatchers implements ArgumentMatcher<Long> {

        private Long argument;

        @Override
        public String toString() {

            return "Es para un mensaje personalizado de error que imprime mockito en caso de que falle el test" + argument + " debe ser un numero positivo";
        }

        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument != null && argument > 0;

        }
    }

    @Test
    @DisplayName("Encontar examen por nombre")
    void testFindExamenPorNombre() {

        when(repository.findAll()).thenReturn(Datos.EXAMENES); //Cambiando las constantes podemos testear diferentes casos
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

        when(repository.findAll()).thenReturn(Datos.EXAMENES); //Cambiando las constantes podemos testear diferentes casos
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");

        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("Trigonometria"));

    }

    @Test
    @DisplayName("Preguntas de examen con Verify")
    void testPreguntasExamenVerify() {

        when(repository.findAll()).thenReturn(Datos.EXAMENES); //Cambiando las constantes podemos testear diferentes casos
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

        //Given
        when(repository.findAll()).thenReturn(Datos.EXAMENES); //Cambiando las constantes podemos testear diferentes casos
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);

        //When
        Examen examen = examenService.findExamenPorNombreConPreguntas("Matematicas");

        //Then
        assertNotNull(examen);
        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(5L);
    }

    @Test
    @DisplayName("Guardar examen")
    void testGuardarExamen() {

        //Given
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS); //Cambiando las constantes podemos testear diferentes casos

        when(repository.guardar(any(Examen.class))).then(new Answer<Examen>() {
            Long secuencia = 8L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {

                Examen examen = invocationOnMock.getArgument(0);

                examen.setId(secuencia++);
                return examen;
            }

        });

        //When
        Examen examen = examenService.guardar(newExamen);

        //Then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());

        verify(repository).guardar(any(Examen.class));
        verify(preguntaRepository).guardarVariasPreguntas(anyList());
    }

    @Test
    @DisplayName("Manejo de excepciones")
    void testManejoExceptions() {
        //Given
        when(repository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL); //Cambiando las constantes podemos testear diferentes casos
        when(preguntaRepository.findPreguntasPorExamenId(isNull())).thenThrow(IllegalArgumentException.class);
        //When
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            examenService.findExamenPorNombreConPreguntas("Matematicas");
        });
        //Then
        assertEquals(IllegalArgumentException.class, exception.getClass());
        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(isNull());
    }

    @Test
    @DisplayName("Argumentos correctos")
    void testArgumentsMatchers() {
        //Given
        when(repository.findAll()).thenReturn(Datos.EXAMENES); //Cambiando las constantes podemos testear diferentes casos
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        examenService.findExamenPorNombreConPreguntas("Matematicas");

        verify(repository).findAll();
        //verify(preguntaRepository).findPreguntasPorExamenId(argThat(arg -> arg != null && arg.equals(5L)));
        //verify(preguntaRepository).findPreguntasPorExamenId(eq(5L));
        verify(preguntaRepository).findPreguntasPorExamenId(argThat(arg -> arg != null && arg >= 5L));

    }

    @Test
    @DisplayName("Argumentos correctos por metodo")
    void testArgumentsMatchersPorMetodo() {
        //Given
        when(repository.findAll()).thenReturn(Datos.EXAMENES); //Cambiando las constantes podemos testear diferentes casos
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        examenService.findExamenPorNombreConPreguntas("Matematicas");

        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(argThat(new MiArgsMatchers()));

    }

    @Test
    @DisplayName("Argumentos correctos por lambdas")
    void testArgumentsMatchersPorLambdas() {
        //Given
        when(repository.findAll()).thenReturn(Datos.EXAMENES); //Cambiando las constantes podemos testear diferentes casos
        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        examenService.findExamenPorNombreConPreguntas("Matematicas");

        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(argThat((argument) -> argument != null && argument > 0));

    }

    @Test
    @DisplayName("Capturar argumento")
    void testArgumentCapture() {
        when(repository.findAll()).thenReturn(Datos.EXAMENES);
//        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        examenService.findExamenPorNombreConPreguntas("Matematicas");

//        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

        verify(preguntaRepository).findPreguntasPorExamenId(captor.capture());
        assertEquals(5L, captor.getValue());
    }
}