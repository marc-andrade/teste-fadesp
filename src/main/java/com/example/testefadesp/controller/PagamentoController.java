package com.example.testefadesp.controller;

import com.example.testefadesp.model.enums.StatusPagamento;
import com.example.testefadesp.service.PagamentoService;
import com.example.testefadesp.service.dto.AtualizarStatusPagamentoDTO;
import com.example.testefadesp.service.dto.NovoPagamentoDTO;
import com.example.testefadesp.service.dto.PagamentoDTO;
import jakarta.validation.Valid;
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
    public ResponseEntity<PagamentoDTO> receberPagamento(@Valid @RequestBody NovoPagamentoDTO dto) {
        PagamentoDTO novoPagamento = pagamentoService.receberPagamento(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/id")
                .buildAndExpand(novoPagamento.getId()).toUri();
        return ResponseEntity.created(uri).body(novoPagamento);
    }

    @PutMapping("/atualizar-status")
    public ResponseEntity<PagamentoDTO> atualizarStatus(@Valid @RequestBody AtualizarStatusPagamentoDTO dto) {
        return ResponseEntity.ok().body(pagamentoService.atualizarStatus(dto));
    }

    @GetMapping
    public ResponseEntity<List<PagamentoDTO>> listarTodos(@RequestParam(required = false) Integer codigoDebito,
                                                       @RequestParam(required = false) String cpfOuCnpjPagador,
                                                       @RequestParam(required = false) StatusPagamento statusPagamento) {
        return ResponseEntity.ok().body(pagamentoService.listarTodos(codigoDebito, cpfOuCnpjPagador, statusPagamento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        pagamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
