package com.example.testefadesp.service;

import com.example.testefadesp.model.Pagamento;
import com.example.testefadesp.model.enums.MetodoDePagamento;
import com.example.testefadesp.model.enums.StatusPagamento;
import com.example.testefadesp.repositories.PagamentoRepository;
import com.example.testefadesp.service.dto.AtualizarStatusPagamentoDTO;
import com.example.testefadesp.service.dto.NovoPagamentoDTO;
import com.example.testefadesp.service.dto.PagamentoDTO;
import com.example.testefadesp.service.exceptions.ArgumentoInvalidoException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    @Transactional
    public Pagamento buscarPorId(Long id) {
        return pagamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado para o ID: " + id));
    }
    @Transactional
    public PagamentoDTO receberPagamento(NovoPagamentoDTO dto) {

        if(dto.getMetodoDePagamento() == null) {
            throw  new ArgumentoInvalidoException("Método de pagamento não pode ser nulo");
        }
        if((dto.getMetodoDePagamento() != MetodoDePagamento.CARTAO_DEBITO && dto.getMetodoDePagamento() != MetodoDePagamento.CARTAO_CREDITO) && dto.getNumeroCartao() != null) {
            throw  new ArgumentoInvalidoException("Numero do cartão não pode ser informado para o método de pagamento informado");
        }
        if((dto.getMetodoDePagamento() == MetodoDePagamento.CARTAO_DEBITO || dto.getMetodoDePagamento() == MetodoDePagamento.CARTAO_CREDITO) && dto.getNumeroCartao() == null) {
            throw  new ArgumentoInvalidoException("Numero do cartão deve ser informado para o método de pagamento informado");
        }
        Pagamento pagamento = new Pagamento(dto);
        pagamento.setStatus(StatusPagamento.PENDENTE_DE_PROCESSAMENTO);
        return new PagamentoDTO(pagamentoRepository.save(pagamento));
    }

    @Transactional
    public PagamentoDTO atualizarStatus(AtualizarStatusPagamentoDTO dto) {

        Pagamento entidadeBanco = buscarPorId(dto.getId());

        if (dto.getStatus() == null) {
            throw new ArgumentoInvalidoException("Status do pagamento não pode ser nulo");
        }

        switch (entidadeBanco.getStatus()) {
            case PENDENTE_DE_PROCESSAMENTO:
                if (dto.getStatus() == StatusPagamento.PROCESSADO_COM_SUCESSO ||
                        dto.getStatus() == StatusPagamento.PROCESSADO_COM_FALHA) {
                    entidadeBanco.setStatus(dto.getStatus());
                    return new PagamentoDTO(pagamentoRepository.save(entidadeBanco));
                } else {
                    throw new ArgumentoInvalidoException("Status inválido para pagamento pendente de processamento");
                }
            case PROCESSADO_COM_SUCESSO:
                throw new ArgumentoInvalidoException("Pagamento já processado com sucesso");
            case PROCESSADO_COM_FALHA:
                if (dto.getStatus() == StatusPagamento.PENDENTE_DE_PROCESSAMENTO) {
                    entidadeBanco.setStatus(dto.getStatus());
                    return new PagamentoDTO(pagamentoRepository.save(entidadeBanco));
                } else {
                    throw new ArgumentoInvalidoException("Pagamento processado com falha nao pode ser alterado para outro status diferente de pendente de processamento");
                }
            default:
                throw new ArgumentoInvalidoException("Status inválido para pagamento");
        }
    }
    @Transactional
    public List<PagamentoDTO> listarTodos(Integer codigoDebito, String cpfOuCnpjPagador, StatusPagamento statusPagamento) {

        Specification<Pagamento> specification = ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(codigoDebito != null) {
                predicates.add(criteriaBuilder.equal(root.get("codigoDebito"), codigoDebito));
            }
            if(cpfOuCnpjPagador != null) {
                predicates.add(criteriaBuilder.equal(root.get("cpfOuCnpjPagador"), cpfOuCnpjPagador));
            }
            if(statusPagamento != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), statusPagamento));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

        return pagamentoRepository.findAll(specification).stream().map(PagamentoDTO::new).toList();
    }
    @Transactional
    public void delete(Long id) {

        try {
            Pagamento pagamento = buscarPorId(id);
            if (pagamento.getStatus() == StatusPagamento.PROCESSADO_COM_SUCESSO) {
                throw new ArgumentoInvalidoException("Pagamento processado com sucesso não pode ser apagado");
            }
            pagamentoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Integridade violada ao apagar pagamento");
        }
    }
}
