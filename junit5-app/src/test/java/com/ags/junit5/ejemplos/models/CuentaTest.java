package com.ags.junit5.ejemplos.models;


import com.ags.junit5.ejemplos.exception.DineroInsuficienteException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class CuentaTest {

    Cuenta cuenta;

    @BeforeEach
    void initMetodoTest(TestInfo info, TestReporter reporter) {
        this.cuenta = new Cuenta("Agustin", new BigDecimal("1000.12345"));
        System.out.println("Iniciando el metodo.");
        System.out.println("ejecutando:"+info.getTags()+info.getTestMethod()+info.getTestClass());
    }

    @AfterEach
    void tearDown() {
        System.out.println("Finalizando metodo");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Incializando el test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Terminando el test");
    }

    @Nested
    @Tag("cuenta")
    @DisplayName("Test sobre cuenta y saldo")
    class CuentaTestNombreYSaldo {
        @Test
        @DisplayName("Test nombre de la cuenta")
        void testNombreCuenta() {
            //    cuenta.setPersona("Agustin");
            String esperado = "Agustin";
            String actual = cuenta.getPersona();
            assertNotNull(actual, () -> "La cuenta no puede ser nula");
            assertEquals(esperado, actual, () -> "El nombre de la cuenta no es el que se esperaba:");
            assertTrue(actual.equals("Agustin"), () -> "Nombre cuenta esperada debe ser igual a la actual");
        }

        @Test
        @DisplayName("Test saldo de la cuenta")
        void testSaldoCuenta() {
            assertNotNull(cuenta.getSueldo());
            assertEquals(1000.12345, cuenta.getSueldo().doubleValue());
            assertFalse(cuenta.getSueldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getSueldo().compareTo(BigDecimal.ZERO) > 0);

        }

        @Test
        @DisplayName("Test referencia de la cuenta")
        void testReferenciaDeCuenta() {
            cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
            Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));
            //       assertNotEquals(cuenta2, cuenta);
            assertEquals(cuenta2, cuenta);
        }
    }

    @Nested
    @DisplayName("Test sobre debito y credito de la cuenta")
    class CuentaOperacionesTest {
        @Test
        @Tag("cuenta")
        @DisplayName("Test debito de la cuenta")
        void testDebitoCuenta() {
            cuenta.debito(new BigDecimal(100));
            assertNotNull(cuenta.getSueldo());
            assertEquals(900, cuenta.getSueldo().intValue());
            assertEquals("900.12345", cuenta.getSueldo().toPlainString());
        }

        @Test
        @Tag("cuenta")
        @DisplayName("Test credito de la cuenta")
        void testCreditoCuenta() {
            cuenta.credito(new BigDecimal(100));
            assertNotNull(cuenta.getSueldo());
            assertEquals(1100, cuenta.getSueldo().intValue());
            assertEquals("1100.12345", cuenta.getSueldo().toPlainString());

        }

        @Test
        @Tag("cuenta")
        @Tag("banco")
        @DisplayName("Test transferir dinero entre cuentas")
        void transferirDineroCuentas() {
            Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
            Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));
            Banco banco = new Banco();
            banco.setNombre("Banco del estado");
            banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
            assertEquals("1000.8989", cuenta2.getSueldo().toPlainString());
            assertEquals("3000", cuenta1.getSueldo().toPlainString());

        }
    }

    @Nested
    @Tag("cuenta")
    @Tag("error")
    @DisplayName("Test sobre excepciones de le cuenta")
    class ExceptionCuentasTest {
        @Test
        @DisplayName("Test dinero insuficiente")
        void dineroInsuficienteException() {
            Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
                cuenta.debito(new BigDecimal(1500));
            });
            String actual = exception.getMessage();
            String esperado = "Dinero Insuficiente";
            assertEquals(esperado, actual);
        }


        @Test
        @DisplayName("Test relacion banco a cuentas")
        void testRelacionBancoCuentas() {
            Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
            Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

            Banco banco = new Banco();
            banco.addCuenta(cuenta1);
            banco.addCuenta(cuenta2);

            banco.setNombre("Banco del estado");
            banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

            assertAll(() ->

                            assertEquals("1000.8989", cuenta2.getSueldo().toPlainString()
                                    , () -> " el valor del saldo de la cuenta2 no es el esparado.")

                    , () ->
                            assertEquals("3000", cuenta1.getSueldo().toPlainString()
                                    , () -> " el valor del saldo de la cuenta1 no es el esparado.")

                    , () ->
                            assertEquals(2, banco.getCuentas().size()
                                    , () -> " el banco no tiene las cuentas esperadas")

                    , () ->
                            assertEquals("Banco del estado", cuenta1.getBanco().getNombre())
                    , () ->
                            assertEquals("Andres", banco.getCuentas().stream()
                                    .filter(cuenta -> cuenta.getPersona().equals("Andres"))
                                    .findFirst()
                                    .get().getPersona())
                    ,
                    () ->
                            assertTrue(banco.getCuentas().stream()
                                    .anyMatch(cuenta -> cuenta.getPersona().equals("Andres")))
            );


        }
    }


    @Nested
    @DisplayName("Test sobre sistemas operativos")
    class SistemaOperativoTest {

        @Test
        @EnabledOnOs({OS.WINDOWS})
        @DisplayName("Test solo en Windows")
        void testSoloWindows() { //Solo debe funcionar en Windows

        }

        @Test
        @EnabledOnOs({OS.MAC, OS.LINUX})
        @DisplayName("Test solo en Mac y Windows")
        void testSoloLinuxMac() { //Solo debe funcionar en linux y Mac

        }

        @Test
        @DisabledOnOs(OS.LINUX)
        void testNoLinux() { //No deebe funcionar en Linux

        }
    }

    @Nested
    @DisplayName("Test sobre versiones de java")
    class JavaVersionTest {
        @EnabledOnJre(JRE.OTHER)
        @Test
        void testSolo17() { //Solo funciona en el JRE 17

        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = "17.0.2")
        void testJavaVersion() { //Solo funciona si la versión de java es la 17.0.2

        }
    }

    @Nested
    @DisplayName("Test sobre propiedades del sistema")
    class SystemPropertiesTest {
        @Test
        void imprimirSystemProperties() { //Muestras las propiedades del sistema
            Properties properties = System.getProperties();
            properties.forEach((k, y) -> System.out.printf(k + ":" + y));
        }

        @Test
        void imprimirVariablesSistema() { //Mustra mas variables del sistema
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k, y) -> System.out.println(k + "" + y));
        }

    }

    @Nested
    @DisplayName("Test sobre variables de entorno")
    class VariableAmbienteTest {
        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDev() { //Solo funciona en el ENV se encuentra la variable dev

        }


        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = "/usr/bin/java")
        void testJavaHome() { //Solamente corre si el JAVA_HOME se encuentra en la ruta indicada

        }
    }

    @Nested
    @DisplayName("Test sobre saldo cuenta Dev o Prod")
    class SaldoCuentaTest {
        @Test
        @DisplayName("Test saldo cuenta DEV")
        void testSaldoCuentaDev() {
            boolean esDev = "dev".equals(System.getProperty("ENV")); //Solo corre si asumimos que el booleano es verdadero
            assumingThat(esDev, () -> {
                assertEquals(1000.12345, cuenta.getSueldo().doubleValue());
                assertFalse(cuenta.getSueldo().compareTo(BigDecimal.ZERO) < 0);
                assertTrue(cuenta.getSueldo().compareTo(BigDecimal.ZERO) > 0);
            });
        }

        @Test
        @DisplayName("Test saldo cuenta PROD")
        void testSaldoCuentaProd() {
            boolean esDev = "prod".equals(System.getProperty("ENV")); //Solo corre si asumimos que el booleano es verdadero pero los metodos fuera en caso de fallar siguen testeando
            assumingThat(esDev, () -> {
                assertEquals(1000.12345, cuenta.getSueldo().doubleValue());
            });
            assertFalse(cuenta.getSueldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getSueldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @DisplayName("Probando DebitoCuentaRepetir")
        @RepeatedTest(value = 5, name = "Repeticion numero {currentRepetition} de {totalRepetitions}")
            //Lo mismo que test pero podemos repetirlo varias veces, tambien tiene una tolerancia a fallos
        void testDebitoCuentaRepetir(@NotNull RepetitionInfo info) {
            if (info.getCurrentRepetition() == 3) { //Obentemos el numero actual del test y si es igual a 3 muestra algo
                System.out.println("Repetición numero: " + info.getCurrentRepetition());
            }
            cuenta.debito(new BigDecimal(100));
            assertNotNull(cuenta.getSueldo());
            assertEquals(900, cuenta.getSueldo().intValue());
            assertEquals("900.12345", cuenta.getSueldo().toPlainString());
        }

    }

    @Tag("param")
    @Nested
    @DisplayName("Test sobre pruebas parametrizadas")
    class PruebasParametrizadasTest{
        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        // Podemos utiliar 0 o argumentsWithNames para ver el numero actual
        @DisplayName("Test debito de la cuenta utilizando Value Source")
        @ValueSource(strings = {"100", "200", "300", "500", "700", "1000.12345"})
            // @ValueSource(doubles = {100,200,300,500,700,1000.12345}) con double se comporta distinto y hay perdida de presición debido a los decimales
        void testDebitoCuentaValueSource(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSueldo());
            assertTrue(cuenta.getSueldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @DisplayName("Test debito de la cuenta utilizando Csv Source")
        @CsvSource({"1,100", "2,200", "3,300", "4,500", "5,700", "6,1000"})
        void testDebitoCuentaCsvSoruce(String indice, String monto) {
            System.out.println(indice + "" + monto);
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSueldo());
            assertTrue(cuenta.getSueldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @DisplayName("Test debito de la cuenta utilizando CsvFile Source")
        @CsvFileSource(resources = "/data.csv")
        void testDebitoCuentaCsvFile(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSueldo());
            assertTrue(cuenta.getSueldo().compareTo(BigDecimal.ZERO) > 0);
        }

        static List<String> montoList() { return Arrays.asList("100", "200", "300", "500", "700", "1000"); }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @DisplayName("Test debito de la cuenta utilizando MethodSource")
        @MethodSource("montoList")
        void testDebitoCuentaMethodSource(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSueldo());
            assertTrue(cuenta.getSueldo().compareTo(BigDecimal.ZERO) > 0);
        }
    }

}