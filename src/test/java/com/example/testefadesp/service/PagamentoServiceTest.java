package com.example.testefadesp.service;

import com.example.testefadesp.model.Pagamento;
import com.example.testefadesp.model.enums.MetodoDePagamento;
import com.example.testefadesp.model.enums.StatusPagamento;
import com.example.testefadesp.repositories.PagamentoRepository;
import com.example.testefadesp.service.dto.AtualizarStatusPagamentoDTO;
import com.example.testefadesp.service.dto.NovoPagamentoDTO;
import com.example.testefadesp.service.dto.PagamentoDTO;
import com.example.testefadesp.service.exceptions.ArgumentoInvalidoException;
import com.example.testefadesp.testes.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PagamentoServiceTest {

    @InjectMocks
    private PagamentoService pagamentoService;
    @Mock
    private PagamentoRepository pagamentoRepository;


    private Pagamento pagamento;
    private PagamentoDTO pagamentoDTO;
    private NovoPagamentoDTO novoPagamentoDTO;
    private AtualizarStatusPagamentoDTO atualizarStatusPagamentoDTO;
    private Long idExistente;
    private Long idNaoExistente;

    @BeforeEach
    void setUp() {
        idExistente = 1L;
        idNaoExistente = 2L;
        pagamento = Factory.criarPagamento();
        pagamentoDTO = Factory.criarPagamentoDTO();
        novoPagamentoDTO = Factory.criarNovoPagamentoDTO();
        atualizarStatusPagamentoDTO = Factory.criarAtualizarStatusPagamentoDTO();

        when(pagamentoRepository.findById(idExistente)).thenReturn(Optional.of(pagamento));
        when(pagamentoRepository.findById(idNaoExistente)).thenReturn(Optional.empty());

        when(pagamentoRepository.save(Mockito.any(Pagamento.class))).thenReturn(pagamento);
    }

    @Test
    void buscarPorIdEntaoRetorneUmaInstanciaDePagamentoQuandoIdForExistente() {

        Pagamento pagamento = pagamentoService.buscarPorId(idExistente);

        assertNotNull(pagamento);
        assertEquals(Pagamento.class, pagamento.getClass());
    }

    @Test
    void buscarPorIdEntaoLancarEntityNotFoundExceptionQuandoIdNaoForExistente() {

        assertThrows(EntityNotFoundException.class, ()
                -> pagamentoService.buscarPorId(idNaoExistente));
    }

    @Test
    void receberPagamentoEntaoRetornarInstanciaPagamentoQuandoSucesso() {
        PagamentoDTO pagamentoDTO = pagamentoService.receberPagamento(novoPagamentoDTO);

        assertNotNull(pagamentoDTO);
        assertEquals(PagamentoDTO.class, pagamentoDTO.getClass());
    }

    @Test
    void receberPagamentoEntaoLancarArgumentoInvalidoExceptionQuandoMetodoPagamentoForNull() {
        novoPagamentoDTO.setMetodoDePagamento(null);

        assertThrows(ArgumentoInvalidoException.class, () ->
                pagamentoService.receberPagamento(novoPagamentoDTO));
    }

    @Test
    void receberPagamentoEntaoLancarArgumentoInvalidoExceptionQuandoMetodoPagamentoNaoForCartaoDebitoOuCartaoCreditoENumeroCartaoNaoNull() {

        novoPagamentoDTO.setMetodoDePagamento(MetodoDePagamento.BOLETO);
        novoPagamentoDTO.setNumeroCartao("12345678901");

        assertThrows(ArgumentoInvalidoException.class, () ->
                pagamentoService.receberPagamento(novoPagamentoDTO));
    }

    @Test
    void receberPagamentoEntaoLancarArgumentoInvalidoExceptionQuandoMetodoPagamentoForCartaoDebitoOuCartaoCreditoENumeroCartaoForNull() {

        novoPagamentoDTO.setMetodoDePagamento(MetodoDePagamento.CARTAO_CREDITO);
        novoPagamentoDTO.setNumeroCartao(null);

        assertThrows(ArgumentoInvalidoException.class, () ->
                pagamentoService.receberPagamento(novoPagamentoDTO));
    }

    @Test
    void atualizarStatusEntaoLancarArgumentoInvalidoExceptionQuandoStatusDtoForNull() {
        atualizarStatusPagamentoDTO.setStatus(null);

        assertThrows(ArgumentoInvalidoException.class, () ->
                pagamentoService.atualizarStatus(atualizarStatusPagamentoDTO));

    }

    @Test
    void atualizarStatusEntaoRetornarInstanciaPagamentoDTOQuandoStatusDtoForProcessadoComSucesso() {
        PagamentoDTO pagamentoDTO = pagamentoService.atualizarStatus(atualizarStatusPagamentoDTO);

        assertNotNull(pagamentoDTO);
        assertEquals(pagamentoDTO.getStatus(), atualizarStatusPagamentoDTO.getStatus());
        assertEquals(PagamentoDTO.class, pagamentoDTO.getClass());
    }

    @Test
    void atualizarStatusEntaoRetornarInstanciaPagamentoDTOQuandoStatusDtoForProcessadoComFalha() {
        atualizarStatusPagamentoDTO.setStatus(StatusPagamento.PROCESSADO_COM_FALHA);
        PagamentoDTO pagamentoDTO = pagamentoService.atualizarStatus(atualizarStatusPagamentoDTO);

        assertNotNull(pagamentoDTO);
        assertEquals(pagamentoDTO.getStatus(), atualizarStatusPagamentoDTO.getStatus());
        assertEquals(PagamentoDTO.class, pagamentoDTO.getClass());
    }

    @Test
    void atualizarStatusEntaoLancarArgumentoInvalidoExceptionQuandoStatusDtoForProcessadoComSucessoOuProcessadoComFalhaEStatusEntidadeBancoForPendenteDeProcessamento() {
        atualizarStatusPagamentoDTO.setStatus(StatusPagamento.PENDENTE_DE_PROCESSAMENTO);

        assertThrows(ArgumentoInvalidoException.class, () ->
                pagamentoService.atualizarStatus(atualizarStatusPagamentoDTO));

    }

    @Test
    void atualizarStatusEntaoLancarArgumentoInvalidoExceptionQuandoStatusEntidadeBancoForProcessadoComSucesso() {
        pagamento.setStatus(StatusPagamento.PROCESSADO_COM_SUCESSO);

        assertThrows(ArgumentoInvalidoException.class, () ->
                pagamentoService.atualizarStatus(atualizarStatusPagamentoDTO));

    }

    @Test
    void atualizarStatusEntaoRetornarInstanciaPagamentoDTOQuandoStatusDtoForPendenteDeProcessamento() {
        atualizarStatusPagamentoDTO.setStatus(StatusPagamento.PENDENTE_DE_PROCESSAMENTO);
        pagamento.setStatus(StatusPagamento.PROCESSADO_COM_FALHA);

        PagamentoDTO pagamentoDTO = pagamentoService.atualizarStatus(atualizarStatusPagamentoDTO);

        assertNotNull(pagamentoDTO);
        assertEquals(pagamentoDTO.getStatus(), atualizarStatusPagamentoDTO.getStatus());
        assertEquals(PagamentoDTO.class, pagamentoDTO.getClass());
    }

    @Test
    void atualizarStatusEntaoLancarArgumentoInvalidoExceptionQuandoStatusEntidadeBancoForProcessadoFalha() {
        atualizarStatusPagamentoDTO.setStatus(StatusPagamento.PROCESSADO_COM_SUCESSO);
        pagamento.setStatus(StatusPagamento.PROCESSADO_COM_FALHA);

        assertThrows(ArgumentoInvalidoException.class, () ->
                pagamentoService.atualizarStatus(atualizarStatusPagamentoDTO));

    }


//
//    @Test
//    void listarTodos() {
//    }
//
//    @Test
//    void delete() {
//    }

}