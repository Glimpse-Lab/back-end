package com.glimpse.gpm.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.glimpse.gpm.dto.cliente.ClienteResponse;
import com.glimpse.gpm.dto.orcamento.OrcamentoComClienteRequest;
import com.glimpse.gpm.dto.orcamento.OrcamentoRequest;
import com.glimpse.gpm.dto.orcamento.OrcamentoResponse;
import com.glimpse.gpm.entity.Cliente;
import com.glimpse.gpm.entity.Orcamento;
import com.glimpse.gpm.enums.StatusOrcamento;
import com.glimpse.gpm.exception.DadosInvalidosException;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import com.glimpse.gpm.repository.ClienteRepository;
import com.glimpse.gpm.repository.OrcamentoRepository;

@Service
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final ClienteRepository clienteRepository;

    // Injeção dos dois repositórios necessários
    public OrcamentoService(OrcamentoRepository orcamentoRepository, ClienteRepository clienteRepository) {
        this.orcamentoRepository = orcamentoRepository;
        this.clienteRepository = clienteRepository;
    }

    public OrcamentoResponse salvar(OrcamentoRequest request) {
        // Busca o cliente dono do orçamento
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado para o ID informado."));

        Orcamento orcamento = new Orcamento();
        orcamento.setCliente(cliente);
        orcamento.setDescricaoProjeto(request.getDescricaoProjeto());
        orcamento.setCep(request.getCep());
        orcamento.setNumEndereco(request.getNumEndereco());
        orcamento.setComplemento(request.getComplemento());

        // Regras de negócio automáticas na criação
        orcamento.setDataSolicitacao(LocalDateTime.now());
        orcamento.setStatus(StatusOrcamento.PENDENTE);

        return mapearParaResponse(orcamentoRepository.save(orcamento));
    }

    public List<OrcamentoResponse> listarOrcamentos() {
        return orcamentoRepository.findAll().stream()
                .map(this::mapearParaResponse)
                .collect(Collectors.toList());
    }

    public OrcamentoResponse buscarOrcamentoPorId(Long id) {
        Orcamento orcamento = orcamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orçamento não encontrado."));
        return mapearParaResponse(orcamento);
    }

    public OrcamentoResponse atualizar(Long id, OrcamentoRequest request) {
        Orcamento orcamentoBanco = orcamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Orçamento não encontrado."));

        // Se houver troca de cliente no orçamento (raro, mas possível)
        if (!orcamentoBanco.getCliente().getId().equals(request.getClienteId())) {
            Cliente novoCliente = clienteRepository.findById(request.getClienteId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Novo cliente não encontrado."));
            orcamentoBanco.setCliente(novoCliente);
        }

        orcamentoBanco.setDescricaoProjeto(request.getDescricaoProjeto());
        orcamentoBanco.setCep(request.getCep());
        orcamentoBanco.setNumEndereco(request.getNumEndereco());
        orcamentoBanco.setComplemento(request.getComplemento());

        return mapearParaResponse(orcamentoRepository.save(orcamentoBanco));
    }

    public boolean excluir(Long id) {
        if (orcamentoRepository.existsById(id)) {
            orcamentoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public OrcamentoResponse salvarComCliente(OrcamentoComClienteRequest request) {
        Cliente cliente;

        if (request.temClienteId()) {
            // Usar cliente existente
            cliente = clienteRepository.findById(request.getClienteId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado para o ID informado."));
        } else if (request.temDadosCliente()) {
            // Verificar se cliente com email/celular já existe
            var clienteExistente = clienteRepository.findByEmailOrCelular(
                request.getEmailCliente(),
                request.getCelularCliente()
            );

            if (clienteExistente.isPresent()) {
                // Cliente já existe, usar o existente
                cliente = clienteExistente.get();
            } else {
                // Criar novo cliente
                cliente = new Cliente();
                cliente.setNome(request.getNomeCliente());
                cliente.setEmail(request.getEmailCliente());
                cliente.setCelular(request.getCelularCliente());
                cliente = clienteRepository.save(cliente);
            }
        } else {
            throw new DadosInvalidosException("É necessário fornecer clienteId OU dados do cliente (nome, email, celular).");
        }

        // Criar o orçamento com o cliente (novo ou existente)
        Orcamento orcamento = new Orcamento();
        orcamento.setCliente(cliente);
        orcamento.setDescricaoProjeto(request.getDescricaoProjeto());
        orcamento.setCep(request.getCep());
        orcamento.setNumEndereco(request.getNumEndereco());
        orcamento.setComplemento(request.getComplemento());
        orcamento.setDataSolicitacao(LocalDateTime.now());
        orcamento.setStatus(StatusOrcamento.PENDENTE);

        return mapearParaResponse(orcamentoRepository.save(orcamento));
    }

    // Metodo auxiliar de conversão (aninhando o ClienteResponse)
    private OrcamentoResponse mapearParaResponse(Orcamento orcamento) {
        ClienteResponse clienteResponse = new ClienteResponse(
                orcamento.getCliente().getId(),
                orcamento.getCliente().getNome(),
                orcamento.getCliente().getCelular(),
                orcamento.getCliente().getEmail()
        );

        return new OrcamentoResponse(
                orcamento.getId(),
                clienteResponse,
                orcamento.getDescricaoProjeto(),
                orcamento.getCep(),
                orcamento.getNumEndereco(),
                orcamento.getComplemento(),
                orcamento.getStatus(),
                orcamento.getDataSolicitacao()
        );
    }
}