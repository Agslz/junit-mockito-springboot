package com.ags.junit5.ejemplos.models;


import com.ags.junit5.ejemplos.exception.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    Cuenta cuenta;

    @BeforeEach
    void initMetodoTest() {
        this.cuenta = new Cuenta("Agustin", new BigDecimal("1000.12345"));
        System.out.println("Iniciando el metodo.");
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

    @Test
    @DisplayName("Test debito de la cuenta")
    void testDebitoCuenta() {
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSueldo());
        assertEquals(900, cuenta.getSueldo().intValue());
        assertEquals("900.12345", cuenta.getSueldo().toPlainString());
    }

    @Test
    @DisplayName("Test credito de la cuenta")
    void testCreditoCuenta() {
        cuenta.credito(new BigDecimal(100));
        assertNotNull(cuenta.getSueldo());
        assertEquals(1100, cuenta.getSueldo().intValue());
        assertEquals("1100.12345", cuenta.getSueldo().toPlainString());
    }

    @Test
    @DisplayName("Test dinero insuficiento")
    void dineroInsuficienteException() {
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
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

    @Test
    @EnabledOnOs({OS.WINDOWS})
    @DisplayName("Test solo en Windows")
    void testSoloWindows() {

    }

    @Test
    @EnabledOnOs({OS.MAC, OS.LINUX})
    @DisplayName("Test solo en Mac y Windows")
    void testSoloLinuxMac() {

    }

    @Test
    @DisabledOnOs(OS.LINUX)
    void testNoLinux() {

    }

    @EnabledOnJre(JRE.OTHER)
    @Test
    void testSolo17() {

    }

    @Test
    void imprimirSystemProperties() {
        Properties properties = System.getProperties();
        properties.forEach((k, y) -> System.out.printf(k + ":" + y));
    }

    @Test
    @EnabledIfSystemProperty(named = "java.version", matches = "17.0.2")
    void testJavaVersion() {

    }

    @Test
    @EnabledIfSystemProperty(named = "ENV", matches = "dev")
    void testDev() {

    }

    @Test
    void imprimirVariablesSistema() {
        Map<String, String> getenv = System.getenv();
        getenv.forEach((k, y )-> System.out.println(k + "" + y));
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = "/usr/bin/java")
    void testJavaHome(){

    }
}