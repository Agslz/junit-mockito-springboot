package com.ags.junit5.ejemplos.models;

import java.math.BigDecimal;

public class Cuenta {

    private String persona;
    private BigDecimal sueldo;

    public Cuenta(String persona, BigDecimal sueldo) {
        this.sueldo = sueldo;
        this.persona = persona;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public BigDecimal getSueldo() {
        return sueldo;
    }

    public void setSueldo(BigDecimal sueldo) {
        this.sueldo = sueldo;
    }
}
