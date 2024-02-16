package com.example.testefadesp.controller;

import com.example.testefadesp.model.Pagamento;
import com.example.testefadesp.model.enums.StatusPagamento;
import com.example.testefadesp.service.PagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<Pagamento> receberPagamento(Pagamento pagamento) {
        Pagamento novoPagamento = pagamentoService.receberPagamento(pagamento);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/id")
                .buildAndExpand(novoPagamento.getId()).toUri();
        return ResponseEntity.created(uri).body(novoPagamento);
    }

    @PutMapping("/atualizar-status")
    public ResponseEntity<Pagamento> atualizarStatus(Pagamento pagamento) {
        return ResponseEntity.ok().body(pagamentoService.atualizarStatus(pagamento));
    }

    @GetMapping
    public ResponseEntity<List<Pagamento>> listarTodos(@RequestParam(required = false) Integer codigoDebito,
                                                       @RequestParam(required = false) String cpfOuCnpjPagador,
                                                       @RequestParam(required = false) StatusPagamento statusPagamento) {
        return ResponseEntity.ok().body(pagamentoService.listarTodos(codigoDebito, cpfOuCnpjPagador, statusPagamento));
    }

}
