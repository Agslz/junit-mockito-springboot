package org.ags.mockito.springboot.app;

import org.ags.mockito.springboot.app.models.Banco;
import org.ags.mockito.springboot.app.models.Cuenta;

import java.math.BigDecimal;

public class Datos {
    public static final Cuenta CUENTA_001 = new Cuenta(1L,"Agustin", new BigDecimal("1000"));
    public static final Cuenta CUENTA_002 = new Cuenta(2L,"Andres", new BigDecimal("2000"));
    public static final Banco BANCO = new Banco(2L,"Banco Galicia", 0);
}
