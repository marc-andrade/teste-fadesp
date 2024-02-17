package com.example.testefadesp.model;

import com.example.testefadesp.model.enums.MetodoDePagamento;
import com.example.testefadesp.model.enums.StatusPagamento;
import com.example.testefadesp.service.dto.NovoPagamentoDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer codigoDebito;
    private String cpfOuCnpjPagador;
    @Enumerated(EnumType.STRING)
    private MetodoDePagamento metodoDePagamento;
    private String numeroCartao;
    private BigDecimal valor;
    @Enumerated(EnumType.STRING)
    @NotNull
    private StatusPagamento status;

    public Pagamento(NovoPagamentoDTO dto) {
        this.codigoDebito = dto.getCodigoDebito();
        this.cpfOuCnpjPagador = dto.getCpfOuCnpjPagador();
        this.metodoDePagamento = dto.getMetodoDePagamento();
        this.numeroCartao = dto.getNumeroCartao();
        this.valor = dto.getValor();
        this.status = StatusPagamento.PENDENTE_DE_PROCESSAMENTO;
    }
}
