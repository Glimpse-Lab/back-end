package com.glimpse.gpm.controller;

import com.glimpse.gpm.config.ApiErrorResponses;
import com.glimpse.gpm.dto.usuario.LoginRequest;
import com.glimpse.gpm.dto.usuario.LoginResponse;
import com.glimpse.gpm.dto.usuario.RecuperacaoSenhaRequest;
import com.glimpse.gpm.dto.usuario.RedefinicaoSenhaRequest;
import com.glimpse.gpm.dto.usuario.UsuarioRequest;
import com.glimpse.gpm.dto.usuario.UsuarioResponse;
import com.glimpse.gpm.entity.Usuario;
import com.glimpse.gpm.enums.Role;
import com.glimpse.gpm.service.AutenticacaoService;
import com.glimpse.gpm.service.TokenService;
import com.glimpse.gpm.service.UsuarioService;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Map;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Usuários e Autenticação", description = "Gestão de credenciais de acesso e endpoint de login do sistema")
@RestController
@RequestMapping("/usuarios")
@ApiErrorResponses
public class UsuarioController {
    private final UsuarioService service;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final AutenticacaoService autenticacaoService;

    public UsuarioController(UsuarioService service, AuthenticationManager authenticationManager, TokenService tokenService, AutenticacaoService autenticacaoService) {
        this.service = service;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.autenticacaoService = autenticacaoService;
    }

    private Role parseRole(String r) {
        if (r == null) return Role.PADRAO;
        if ("PADRÃO".equalsIgnoreCase(r)) r = "PADRAO";
        try {
            return Role.valueOf(r.toUpperCase());
        } catch (Exception e) {
            return Role.PADRAO;
        }
    }

    @Operation(summary = "Cadastrar novo usuário", description = "Rota pública. Cria um novo administrador/usuário para acesso ao painel do sistema.")
    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioRequest.getNome());
        usuario.setCpf(usuarioRequest.getCpf());
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setSenha(usuarioRequest.getSenha());
        if (usuarioRequest.getRole() != null) {
            usuario.setRole(usuarioRequest.getRole().name());
        }

        Usuario usuarioSalvo = service.salvar(usuario);
        Role roleResp = parseRole(usuarioSalvo.getRole());
        UsuarioResponse response = new UsuarioResponse(usuarioSalvo.getId(), usuarioSalvo.getNome(), usuarioSalvo.getCpf(), usuarioSalvo.getEmail(), roleResp);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos os usuários", description = "Requer autenticação JWT. Retorna a lista de usuários cadastrados no painel.")
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        List<Usuario> usuarios = service.listarUsuarios();
        List<UsuarioResponse> responses = usuarios.stream()
                .map(u -> new UsuarioResponse(u.getId(), u.getNome(), u.getCpf(), u.getEmail(), parseRole(u.getRole())))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Requer autenticação JWT. Retorna os detalhes de um usuário específico.")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = service.buscarUsuarioPorId(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            UsuarioResponse response = new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getCpf(), usuario.getEmail(), parseRole(usuario.getRole()));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualizar usuário", description = "Requer autenticação JWT. Atualiza os dados de acesso de um usuário existente.")
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable Long id, @RequestBody UsuarioRequest usuarioRequest) {
        try {
            var usuarioOpt = service.buscarUsuarioPorId(id);
            // Recupera usuário logado para verificar permissões
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Usuario usuarioLogado = null;
            if (principal instanceof Usuario) usuarioLogado = (Usuario) principal;

            // Se usuário logado for PADRAO, não pode alterar permissões (role)
            if (usuarioLogado != null && usuarioLogado.getRole() != null && usuarioLogado.getRole().equalsIgnoreCase("PADRAO") && usuarioRequest.getRole() != null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Você não tem permissão para alterar permissões."));
            }
            if (usuarioOpt.isEmpty()) return ResponseEntity.notFound().build();

            Usuario existente = usuarioOpt.get();
            Usuario usuario = new Usuario();
            // evitar que o valor default da entidade ('PADRAO') sobrescreva o role quando não informado
            usuario.setRole(null);
            usuario.setNome(usuarioRequest.getNome() != null ? usuarioRequest.getNome() : existente.getNome());
            usuario.setCpf(usuarioRequest.getCpf() != null ? usuarioRequest.getCpf() : existente.getCpf());
            usuario.setEmail(usuarioRequest.getEmail() != null ? usuarioRequest.getEmail() : existente.getEmail());
            usuario.setSenha(usuarioRequest.getSenha());
            if (usuarioRequest.getRole() != null) usuario.setRole(usuarioRequest.getRole().name());

            Usuario usuarioAtualizado = service.atualizar(id, usuario);
            UsuarioResponse response = new UsuarioResponse(usuarioAtualizado.getId(), usuarioAtualizado.getNome(), usuarioAtualizado.getCpf(), usuarioAtualizado.getEmail(), parseRole(usuarioAtualizado.getRole()));
            return ResponseEntity.ok(response);
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Excluir usuário", description = "Requer autenticação JWT. Remove o acesso de um usuário ao painel do sistema.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluir(@PathVariable Long id) {
        // Recupera usuário logado e impede exclusão por usuários PADRAO
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Usuario) {
            Usuario usuarioLogado = (Usuario) principal;
            if (usuarioLogado.getRole() != null && usuarioLogado.getRole().equalsIgnoreCase("PADRAO")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Usuários padrão não podem excluir contas."));
            }
        }

        boolean excluido = service.deletar(id);
        if (excluido) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Realizar Login", description = "Rota pública. Valida as credenciais e retorna o Token JWT necessário para acessar as rotas protegidas.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha());
            var authentication = authenticationManager.authenticate(authenticationToken);
            Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
            String tokenJWT = tokenService.gerarToken(usuarioLogado);

            LoginResponse response = new LoginResponse(usuarioLogado.getId(), usuarioLogado.getNome(), usuarioLogado.getEmail(), usuarioLogado.getCpf(), tokenJWT);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Solicitar recuperação de senha", description = "Rota pública. Dispara e-mail com link se o usuário existir.")
    @PostMapping("/esqueci-senha")
    public ResponseEntity<Void> solicitarRecuperacao(@RequestBody @Valid RecuperacaoSenhaRequest request) {
        autenticacaoService.solicitarRecuperacao(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Redefinir senha com token", description = "Rota pública. Recebe token do e-mail e salva a nova senha.")
    @PostMapping("/redefinir-senha")
    public ResponseEntity<Void> redefinirSenha(@RequestBody @Valid RedefinicaoSenhaRequest request) {
        autenticacaoService.redefinirSenha(request.getToken(), request.getNovaSenha());
        return ResponseEntity.ok().build();
    }
}