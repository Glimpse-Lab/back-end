package com.glimpse.gpm.service;

import com.glimpse.gpm.entity.Categoria;
import com.glimpse.gpm.entity.GaleriaPortfolio;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import com.glimpse.gpm.repository.CategoriaRepository;
import com.glimpse.gpm.repository.ProjetoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjetoServiceTest {

    @Mock
    private ProjetoRepository repository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProjetoService service;

    private GaleriaPortfolio projeto;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Fachadas");

        projeto = new GaleriaPortfolio();
        projeto.setTitulo("Fachada Comercial");
        projeto.setCategoria(categoria);
    }

    @Test
    void salvar_DeveSalvarProjeto_QuandoCategoriaExistir() {
        // Arrange
        when(categoriaRepository.findById(categoria.getId())).thenReturn(Optional.of(categoria));
        when(repository.save(any(GaleriaPortfolio.class))).thenReturn(projeto);

        // Act
        GaleriaPortfolio salvo = service.salvar(projeto);

        // Assert
        assertNotNull(salvo);
        assertEquals("Fachada Comercial", salvo.getTitulo());
        verify(categoriaRepository, times(1)).findById(categoria.getId());
        verify(repository, times(1)).save(projeto);
    }

    @Test
    void atualizar_DeveLancarException_QuandoProjetoNaoEncontrado() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () -> {
            service.atualizar(1L, projeto);
        });

        assertEquals("Projeto não encontrado.", exception.getMessage());
        verify(repository, never()).save(any(GaleriaPortfolio.class));
    }
}