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
import com.glimpse.gpm.mapper.StatusOrcamentoMapper;
import com.glimpse.gpm.exception.DadosInvalidosException;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import com.glimpse.gpm.repository.ClienteRepository;
import com.glimpse.gpm.repository.OrcamentoRepository;
import com.glimpse.gpm.repository.EnderecoRepository;
import com.glimpse.gpm.entity.Endereco;

@Service
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;
    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;

    // Injeção dos repositórios necessários
    public OrcamentoService(OrcamentoRepository orcamentoRepository, ClienteRepository clienteRepository, EnderecoRepository enderecoRepository) {
        this.orcamentoRepository = orcamentoRepository;
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
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

        // criar e persistir Endereco normalizado
        Endereco endereco = new Endereco();
        endereco.setCep(request.getCep());
        endereco.setRua(request.getRua());
        endereco.setBairro(request.getBairro());
        endereco.setCidade(request.getCidade());
        endereco.setEstado(request.getEstado());
        endereco.setNumero(request.getNumEndereco());
        endereco.setComplemento(request.getComplemento());
        endereco = enderecoRepository.save(endereco);
        orcamento.setEndereco(endereco);

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

        // atualizar ou criar endereco associado
        Endereco enderecoBanco = orcamentoBanco.getEndereco();
        if (enderecoBanco == null) enderecoBanco = new Endereco();
        enderecoBanco.setCep(request.getCep());
        enderecoBanco.setRua(request.getRua());
        enderecoBanco.setBairro(request.getBairro());
        enderecoBanco.setCidade(request.getCidade());
        enderecoBanco.setEstado(request.getEstado());
        enderecoBanco.setNumero(request.getNumEndereco());
        enderecoBanco.setComplemento(request.getComplemento());
        enderecoBanco = enderecoRepository.save(enderecoBanco);
        orcamentoBanco.setEndereco(enderecoBanco);

        return mapearParaResponse(orcamentoRepository.save(orcamentoBanco));
    }

        /**
         * Atualiza apenas o status do orçamento
         */
        public OrcamentoResponse atualizarStatus(Long id, String statusFrontend) {
            Orcamento orcamentoBanco = orcamentoRepository.findById(id)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Orçamento não encontrado."));

            var statusEnum = StatusOrcamentoMapper.fromFrontend(statusFrontend);
            orcamentoBanco.setStatus(statusEnum);

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
            // Priorizar busca por e-mail. Se existir cliente com mesmo e-mail, usar. Caso contrário, criar novo.
            var clientePorEmail = clienteRepository.findByEmail(request.getEmailCliente());
            if (clientePorEmail.isPresent()) {
                cliente = clientePorEmail.get();
            } else {
                // Criar novo cliente (permite mesmo que celular coincida)
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

        Endereco endereco = new Endereco();
        endereco.setCep(request.getCep());
        endereco.setRua(request.getRua());
        endereco.setBairro(request.getBairro());
        endereco.setCidade(request.getCidade());
        endereco.setEstado(request.getEstado());
        endereco.setNumero(request.getNumEndereco());
        endereco.setComplemento(request.getComplemento());
        endereco = enderecoRepository.save(endereco);
        orcamento.setEndereco(endereco);

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

        String rua = null, bairro = null, cidade = null, estado = null;
        if (orcamento.getEndereco() != null) {
            rua = orcamento.getEndereco().getRua();
            bairro = orcamento.getEndereco().getBairro();
            cidade = orcamento.getEndereco().getCidade();
            estado = orcamento.getEndereco().getEstado();
        }

        return new OrcamentoResponse(
            orcamento.getId(),
            clienteResponse,
            orcamento.getDescricaoProjeto(),
            orcamento.getCep(),
            orcamento.getNumEndereco(),
            orcamento.getComplemento(),
            rua,
            bairro,
            cidade,
            estado,
            orcamento.getStatus(),
            orcamento.getDataSolicitacao()
        );
    }
}