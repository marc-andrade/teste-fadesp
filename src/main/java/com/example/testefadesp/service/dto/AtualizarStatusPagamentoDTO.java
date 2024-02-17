package com.example.testefadesp.service.dto;

import com.example.testefadesp.model.enums.StatusPagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarStatusPagamentoDTO {

    @NotNull(message = "ID do pagamento não pode ser nulo")
    private Long id;
    @NotNull(message = "Status do pagamento não pode ser nulo")
    private StatusPagamento status;
}
