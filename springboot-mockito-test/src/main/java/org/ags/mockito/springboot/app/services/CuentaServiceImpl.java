package org.ags.mockito.springboot.app.services;

import org.ags.mockito.springboot.app.models.Banco;
import org.ags.mockito.springboot.app.models.Cuenta;
import org.ags.mockito.springboot.app.repositories.BancoRepository;
import org.ags.mockito.springboot.app.repositories.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class CuentaServiceImpl implements CuentaService {

    public CuentaServiceImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    private CuentaRepository cuentaRepository;
    private BancoRepository bancoRepository;

    @Override
    public Cuenta findById(Long id) {
        return cuentaRepository.findById(id);
    }

    @Override
    public int revisarTotalTransferencias(Long bancoId) {
        Banco banco = bancoRepository.findById(bancoId);
        return banco.getTotalTrasnferencias();
    }

    @Override
    public BigDecimal revisarSalgo(Long cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId);
        return cuenta.getSaldo();
    }

    @Override
    public void transferir(Long numeroCuentaOrigen, Long numCuentaDestino, BigDecimal monto) {
        Banco banco = bancoRepository.findById(1L);
        int totalTransferencias = banco.getTotalTrasnferencias();
        banco.setTotalTrasnferencias(++totalTransferencias);
        bancoRepository.update(banco);

        Cuenta cuentaOrigen = cuentaRepository.findById(numeroCuentaOrigen);
        cuentaOrigen.debito(monto);
        cuentaRepository.update(cuentaOrigen);

        Cuenta cuentaDestino = cuentaRepository.findById(numCuentaDestino);
        cuentaDestino.credito(monto);
        cuentaRepository.update(cuentaDestino);

    }
}
