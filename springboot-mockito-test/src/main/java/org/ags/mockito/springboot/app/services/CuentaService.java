package org.ags.mockito.springboot.app.services;

import org.ags.mockito.springboot.app.models.Cuenta;

import java.math.BigDecimal;

public interface CuentaService {

    Cuenta findById(Long id);

    int revisarTotalTransferencias(Long bancoId);

    BigDecimal revisarSaldo(Long cuentaId);

    void transferir(Long numeroCuentaOrigen, Long numCuentaDestino, BigDecimal monto, Long bancoId);


}
