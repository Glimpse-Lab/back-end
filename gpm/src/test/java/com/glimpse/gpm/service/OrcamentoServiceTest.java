package com.glimpse.gpm.service;

import com.glimpse.gpm.dto.orcamento.OrcamentoRequest;
import com.glimpse.gpm.dto.orcamento.OrcamentoResponse;
import com.glimpse.gpm.entity.Cliente;
import com.glimpse.gpm.entity.Endereco;
import com.glimpse.gpm.entity.Orcamento;
import com.glimpse.gpm.enums.StatusOrcamento;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import com.glimpse.gpm.repository.ClienteRepository;
import com.glimpse.gpm.repository.EnderecoRepository;
import com.glimpse.gpm.repository.OrcamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrcamentoServiceTest {

    @Mock
    private OrcamentoRepository orcamentoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrcamentoService service;

    private OrcamentoRequest request;
    private Cliente cliente;
    private Endereco endereco;
    private Orcamento orcamentoSalvo;

    @BeforeEach
    void setUp() {
        request = new OrcamentoRequest();
        request.setClienteId(1L);
        request.setDescricaoProjeto("Fechamento de sacada");
        request.setCep("01001-000");
        request.setNumEndereco(150);

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Ana Clara");

        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setCep("01001-000");

        orcamentoSalvo = new Orcamento();
        orcamentoSalvo.setId(1L);
        orcamentoSalvo.setCliente(cliente);
        orcamentoSalvo.setEndereco(endereco);
        orcamentoSalvo.setDescricaoProjeto("Fechamento de sacada");
        orcamentoSalvo.setStatus(StatusOrcamento.PENDENTE);
        orcamentoSalvo.setDataSolicitacao(LocalDateTime.now());
    }

    @Test
    void salvar_DeveRetornarOrcamentoResponse_QuandoSucesso() {
        // Arrange
        when(clienteRepository.findById(request.getClienteId())).thenReturn(Optional.of(cliente));
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(endereco);
        when(orcamentoRepository.save(any(Orcamento.class))).thenReturn(orcamentoSalvo);

        // Act
        OrcamentoResponse response = service.salvar(request);

        // Assert
        assertNotNull(response);
        assertEquals("Fechamento de sacada", response.getDescricaoProjeto());
        assertEquals(StatusOrcamento.PENDENTE, response.getStatus());
        verify(clienteRepository, times(1)).findById(request.getClienteId());
        verify(enderecoRepository, times(1)).save(any(Endereco.class));
        verify(orcamentoRepository, times(1)).save(any(Orcamento.class));
    }

    @Test
    void salvar_DeveLancarException_QuandoClienteNaoEncontrado() {
        // Arrange
        when(clienteRepository.findById(request.getClienteId())).thenReturn(Optional.empty());

        // Act & Assert
        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () -> {
            service.salvar(request);
        });

        assertEquals("Cliente não encontrado para o ID informado.", exception.getMessage());
        verify(enderecoRepository, never()).save(any(Endereco.class));
        verify(orcamentoRepository, never()).save(any(Orcamento.class));
    }
}