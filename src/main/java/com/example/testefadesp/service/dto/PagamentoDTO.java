package com.example.testefadesp.service.dto;

import com.example.testefadesp.model.Pagamento;
import com.example.testefadesp.model.enums.MetodoDePagamento;
import com.example.testefadesp.model.enums.StatusPagamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoDTO {

    private Long id;
    private Integer codigoDebito;
    private String cpfOuCnpjPagador;
    private MetodoDePagamento metodoDePagamento;
    private String numeroCartao;
    private BigDecimal valor;
    private StatusPagamento status;

    public PagamentoDTO(Pagamento entidade) {
        this.id = entidade.getId();
        this.codigoDebito = entidade.getCodigoDebito();
        this.cpfOuCnpjPagador = entidade.getCpfOuCnpjPagador();
        this.metodoDePagamento = entidade.getMetodoDePagamento();
        this.numeroCartao = entidade.getNumeroCartao();
        this.valor = entidade.getValor();
        this.status = entidade.getStatus();
    }
}
