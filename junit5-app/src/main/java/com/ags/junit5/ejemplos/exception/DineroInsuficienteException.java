package com.ags.junit5.ejemplos.exception;

public class DineroInsuficienteException extends RuntimeException {

    public DineroInsuficienteException(String message) {
        super(message);
    }
}
