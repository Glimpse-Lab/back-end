package com.glimpse.gpm.service;

import com.glimpse.gpm.entity.Orcamento;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import com.glimpse.gpm.repository.OrcamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrcamentoService {

    private final OrcamentoRepository repository;

    public OrcamentoService(OrcamentoRepository repository) {
        this.repository = repository;
    }

    public Orcamento salvar(Orcamento orcamento) {
        orcamento.setDataCriacao(LocalDateTime.now());
        orcamento.setDataAtualizacao(LocalDateTime.now());
        return repository.save(orcamento);
    }

    public List<Orcamento> listarOrcamentos() {
        return repository.findAll();
    }

    public Optional<Orcamento> buscarOrcamentoPorId(Long id) {
        return repository.findById(id);
    }

    public Orcamento atualizar(Long id, Orcamento orcamentoAtualizado) {
        Optional<Orcamento> orcamentoEncontrado = repository.findById(id);

        if (orcamentoEncontrado.isEmpty()) {
            throw new RecursoNaoEncontradoException("Orçamento não encontrado.");
        }

        Orcamento orcamentoBanco = orcamentoEncontrado.get();

        orcamentoBanco.setNomeCliente(orcamentoAtualizado.getNomeCliente());
        orcamentoBanco.setEmailCliente(orcamentoAtualizado.getEmailCliente());
        orcamentoBanco.setTelefoneCliente(orcamentoAtualizado.getTelefoneCliente());
        orcamentoBanco.setAssunto(orcamentoAtualizado.getAssunto());
        orcamentoBanco.setDescricao(orcamentoAtualizado.getDescricao());
        orcamentoBanco.setStatus(orcamentoAtualizado.getStatus());
        orcamentoBanco.setValoresEstimados(orcamentoAtualizado.getValoresEstimados());
        orcamentoBanco.setDataAtualizacao(LocalDateTime.now());

        return repository.save(orcamentoBanco);
    }

    public boolean excluir(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
