package com.example.testefadesp.testes;

import com.example.testefadesp.model.Pagamento;
import com.example.testefadesp.model.enums.MetodoDePagamento;
import com.example.testefadesp.model.enums.StatusPagamento;
import com.example.testefadesp.service.dto.AtualizarStatusPagamentoDTO;
import com.example.testefadesp.service.dto.NovoPagamentoDTO;

import java.math.BigDecimal;

public class Factory {
    public static Pagamento criarPagamento() {
        return new Pagamento(1L,1,"12345678901", MetodoDePagamento.BOLETO,null,new BigDecimal("100.50"), StatusPagamento.PENDENTE_DE_PROCESSAMENTO);
    }

    public static NovoPagamentoDTO criarNovoPagamentoDTO() {
        return new NovoPagamentoDTO(1,"12345678901", MetodoDePagamento.BOLETO,null,new BigDecimal("100.50"));
    }

    public static AtualizarStatusPagamentoDTO criarAtualizarStatusPagamentoDTO() {
        return new AtualizarStatusPagamentoDTO(1L, StatusPagamento.PROCESSADO_COM_SUCESSO);
    }


}
