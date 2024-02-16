package com.example.testefadesp.service;

import com.example.testefadesp.model.Pagamento;
import com.example.testefadesp.model.enums.StatusPagamento;
import com.example.testefadesp.repositories.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    public Pagamento receberPagamento(Pagamento pagamento){

        if(pagamento.getStatus() != null) {
            pagamento.setStatus(StatusPagamento.PENDENTE_DE_PROCESSAMENTO);
        }
        return pagamentoRepository.save(pagamento);
    }

    public Pagamento atualizarStatus(Pagamento pagamento) {
        Pagamento entidadeBanco = pagamentoRepository
                .findById(pagamento.getId()).orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado"));
        return null;
    }
}
