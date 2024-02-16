package com.example.testefadesp.service;

import com.example.testefadesp.model.Pagamento;
import com.example.testefadesp.model.enums.MetodoDePagamento;
import com.example.testefadesp.model.enums.StatusPagamento;
import com.example.testefadesp.repositories.PagamentoRepository;
import com.example.testefadesp.service.exceptions.ArgumentoInvalidoException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    public Pagamento findById(Long id) {
        return pagamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado para o ID: " + id));
    }

    public Pagamento receberPagamento(Pagamento pagamento) {

        pagamento.setStatus(StatusPagamento.PENDENTE_DE_PROCESSAMENTO);
        if(pagamento.getMetodoDePagamento() != null &&
                (pagamento.getMetodoDePagamento() != MetodoDePagamento.CARTAO_DEBITO &&
                pagamento.getMetodoDePagamento() != MetodoDePagamento.CARTAO_CREDITO) && pagamento.getNumeroCartao() != null) {
            throw  new ArgumentoInvalidoException("Numero do cartão não pode ser informado para o método de pagamento informado");
        }
        return pagamentoRepository.save(pagamento);
    }

    public Pagamento atualizarStatus(Pagamento pagamento) {

        Pagamento entidadeBanco = findById(pagamento.getId());

        switch (entidadeBanco.getStatus()) {
            case PENDENTE_DE_PROCESSAMENTO:
                if(pagamento.getStatus() == StatusPagamento.PROCESSADO_COM_SUCESSO ||
                pagamento.getStatus() == StatusPagamento.PROCESSADO_COM_FALHA) {
                    entidadeBanco.setStatus(pagamento.getStatus());
                    return pagamentoRepository.save(entidadeBanco);
                } else {
                    throw  new ArgumentoInvalidoException("Status inválido para pagamento pendente de processamento");
                }
            case PROCESSADO_COM_SUCESSO:
                throw  new ArgumentoInvalidoException("Pagamento já processado com sucesso");
            case PROCESSADO_COM_FALHA:
                if(pagamento.getStatus() == StatusPagamento.PENDENTE_DE_PROCESSAMENTO) {
                    entidadeBanco.setStatus(pagamento.getStatus());
                    return pagamentoRepository.save(entidadeBanco);
                } else {
                    throw  new ArgumentoInvalidoException("Pagamento processado com falha nao pode ser alterado para outro status diferente de pendente de processamento");
                }
                default:
                    throw  new ArgumentoInvalidoException("Status inválido para pagamento");
        }
    }
}
