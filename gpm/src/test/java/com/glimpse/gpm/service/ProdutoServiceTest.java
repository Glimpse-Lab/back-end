package com.glimpse.gpm.service;

import com.glimpse.gpm.entity.Categoria;
import com.glimpse.gpm.entity.Produto;
import com.glimpse.gpm.exception.DadosInvalidosException;
import com.glimpse.gpm.repository.CategoriaRepository;
import com.glimpse.gpm.repository.ProdutoRepository;
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
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private ProdutoService service;

    private Produto produto;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Janelas");

        produto = new Produto();
        produto.setNome("Janela de Vidro");
        produto.setDescricao("Janela blindex 10mm");
        produto.setPreco(1500.0);
        produto.setQuantidadeEstoque(10);
        produto.setCategoria(categoria);
    }

    @Test
    void salvar_DeveLancarException_QuandoPrecoForInvalido() {
        // Arrange
        produto.setPreco(-50.0);

        // Act & Assert
        DadosInvalidosException exception = assertThrows(DadosInvalidosException.class, () -> {
            service.salvar(produto);
        });

        assertEquals("O preço do produto deve ser maior que zero.", exception.getMessage());
        verify(repository, never()).save(any(Produto.class));
    }

    @Test
    void salvar_DeveSalvarProduto_QuandoDadosValidos() {
        // Arrange
        when(repository.existsByNome(produto.getNome())).thenReturn(false);
        when(categoriaRepository.findById(categoria.getId())).thenReturn(Optional.of(categoria));
        when(repository.save(any(Produto.class))).thenReturn(produto);

        // Act
        Produto salvo = service.salvar(produto);

        // Assert
        assertNotNull(salvo);
        assertEquals("Janela de Vidro", salvo.getNome());
        verify(repository, times(1)).save(produto);
    }
}