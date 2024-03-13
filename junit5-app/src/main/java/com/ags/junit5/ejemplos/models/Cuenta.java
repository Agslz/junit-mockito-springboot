package com.ags.junit5.ejemplos.models;

import com.ags.junit5.ejemplos.exception.DineroInsuficienteException;

import java.math.BigDecimal;

public class Cuenta {

    private String persona;
    private BigDecimal sueldo;

    private Banco banco;


    public Cuenta(String persona, BigDecimal sueldo) {
        this.sueldo = sueldo;
        this.persona = persona;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public Banco getBanco() {
        return banco;
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

    public void debito(BigDecimal monto) {
        BigDecimal nuevoSaldo = this.sueldo.subtract(monto);
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new DineroInsuficienteException("Dinero Insuficiente");
        }
        this.sueldo = nuevoSaldo;
    }

    public void credito(BigDecimal monto) {
        this.sueldo = this.sueldo.add(monto);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Cuenta)) {
            return false;
        }
        Cuenta c = (Cuenta) obj;
        if (persona == null || this.sueldo == null) {
            return false;
        }
        return this.persona.equals(c.getPersona()) && this.sueldo.equals(c.getSueldo());
    }
}
