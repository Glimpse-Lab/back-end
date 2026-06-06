package com.glimpse.gpm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glimpse.gpm.dto.cliente.ClienteResponse;
import com.glimpse.gpm.dto.orcamento.OrcamentoComClienteRequest;
import com.glimpse.gpm.dto.orcamento.OrcamentoRequest;
import com.glimpse.gpm.dto.orcamento.OrcamentoResponse;
import com.glimpse.gpm.dto.orcamento.OrcamentoStatusRequest;
import com.glimpse.gpm.entity.Endereco;
import com.glimpse.gpm.enums.StatusOrcamento;
import com.glimpse.gpm.service.OrcamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class OrcamentoControllerMockMvcTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private OrcamentoService service;

    @InjectMocks
    private OrcamentoController controller;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new com.glimpse.gpm.exception.GlobalExceptionHandler())
                .build();
    }

    private OrcamentoResponse sampleResponse(Long id) {
        ClienteResponse cliente = new ClienteResponse(1L, "João", "1199999999", "joao@email.com");
        Endereco endereco = new Endereco(); endereco.setCep("01001-000");
        return new OrcamentoResponse(id, cliente, "Projeto teste", endereco, StatusOrcamento.PENDENTE, LocalDateTime.now());
    }

    @Test
    void criar_Retorna201_QuandoRequisicaoValida() throws Exception {
        OrcamentoRequest req = new OrcamentoRequest();
        req.setClienteId(1L); req.setDescricaoProjeto("Projeto teste"); req.setCep("01001-000"); req.setNumEndereco(10);

        when(service.salvar(any(OrcamentoRequest.class))).thenReturn(sampleResponse(100L));

        mockMvc.perform(post("/orcamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100));
    }

    @Test
    void criarComCliente_Retorna201_QuandoValido() throws Exception {
        OrcamentoComClienteRequest req = new OrcamentoComClienteRequest();
        req.setDescricaoProjeto("Projeto com cliente"); req.setCep("01001-000"); req.setNumEndereco(5);

        when(service.salvarComCliente(any(OrcamentoComClienteRequest.class))).thenReturn(sampleResponse(101L));

        mockMvc.perform(post("/orcamentos/com-cliente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(101));
    }

    @Test
    void listar_RetornaLista() throws Exception {
        when(service.listarOrcamentos()).thenReturn(List.of(sampleResponse(1L)));

        mockMvc.perform(get("/orcamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void listar_RetornaVazio_QuandoNenhum() throws Exception {
        when(service.listarOrcamentos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/orcamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void buscarPorId_RetornaOk_QuandoExistir() throws Exception {
        when(service.buscarOrcamentoPorId(5L)).thenReturn(sampleResponse(5L));

        mockMvc.perform(get("/orcamentos/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void atualizar_RetornaOk_QuandoValido() throws Exception {
        OrcamentoRequest req = new OrcamentoRequest(); req.setClienteId(1L); req.setDescricaoProjeto("Atualiza"); req.setCep("01001-000"); req.setNumEndereco(2);
        when(service.atualizar(eq(2L), any(OrcamentoRequest.class))).thenReturn(sampleResponse(2L));

        mockMvc.perform(put("/orcamentos/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void atualizarStatus_RetornaOk() throws Exception {
        OrcamentoStatusRequest req = new OrcamentoStatusRequest("APROVADO");
        when(service.atualizarStatus(3L, "APROVADO")).thenReturn(sampleResponse(3L));

        mockMvc.perform(put("/orcamentos/3/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    void excluir_RetornaNoContent_QuandoExcluido() throws Exception {
        when(service.excluir(7L)).thenReturn(true);

        mockMvc.perform(delete("/orcamentos/7"))
                .andExpect(status().isNoContent());
    }

    @Test
    void excluir_RetornaNotFound_QuandoNaoExistir() throws Exception {
        when(service.excluir(8L)).thenReturn(false);

        mockMvc.perform(delete("/orcamentos/8"))
                .andExpect(status().isNotFound());
    }

    @Test
    void criar_RetornaBadRequest_QuandoFaltarCamposObrigatorios() throws Exception {
        OrcamentoRequest req = new OrcamentoRequest(); // vazio, falta campos obrigatórios

        mockMvc.perform(post("/orcamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}

