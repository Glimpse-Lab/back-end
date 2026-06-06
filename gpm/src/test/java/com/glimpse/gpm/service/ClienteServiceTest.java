package com.glimpse.gpm.service;

import com.glimpse.gpm.dto.cliente.ClienteRequest;
import com.glimpse.gpm.entity.Cliente;
import com.glimpse.gpm.exception.DadosInvalidosException;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import com.glimpse.gpm.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    @Test
    void deveSalvarCliente() {

        ClienteRequest request = new ClienteRequest();
        request.setNome("Andre");
        request.setEmail("andre@email.com");
        request.setCelular("11999999999");

        when(repository.existsByEmail(anyString()))
                .thenReturn(false);

        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Andre");

        when(repository.save(any()))
                .thenReturn(cliente);

        var response = service.salvar(request);

        assertEquals("Andre", response.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {

        ClienteRequest request = new ClienteRequest();
        request.setEmail("andre@email.com");

        when(repository.existsByEmail(anyString()))
                .thenReturn(true);

        assertThrows(
                DadosInvalidosException.class,
                () -> service.salvar(request)
        );
    }

    @Test
    void deveBuscarClientePorId() {

        Cliente cliente = new Cliente();
        cliente.setId(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(cliente));

        assertNotNull(service.buscarClientePorId(1L));
    }

    @Test
    void deveLancarQuandoClienteNaoExiste() {

        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.buscarClientePorId(1L)
        );
    }

    @Test
    void deveExcluirCliente() {

        when(repository.existsById(1L))
                .thenReturn(true);

        assertTrue(service.excluir(1L));

        verify(repository).deleteById(1L);
    }
}