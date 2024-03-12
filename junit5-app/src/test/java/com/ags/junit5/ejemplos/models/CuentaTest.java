package com.ags.junit5.ejemplos.models;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta("Agustin", new BigDecimal("1000.12345"));
    //    cuenta.setPersona("Agustin");
        String esperado = "Agustin";
        String actual = cuenta.getPersona();
        assertEquals(esperado,actual);
        assertTrue(actual.equals("Agustin"));
    }

}