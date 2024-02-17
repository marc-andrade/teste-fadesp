package com.example.testefadesp.service.dto;

import com.example.testefadesp.model.enums.MetodoDePagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NovoPagamentoDTO {

    private Integer codigoDebito;
    private String cpfOuCnpjPagador;
    @NotNull(message = "campo obrigatório")
    private MetodoDePagamento metodoDePagamento;
    private String numeroCartao;
    private BigDecimal valor;
}
