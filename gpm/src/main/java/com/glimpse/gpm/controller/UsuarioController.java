package com.glimpse.gpm.controller;

import com.glimpse.gpm.config.ApiErrorResponses;
import com.glimpse.gpm.dto.usuario.LoginRequest;
import com.glimpse.gpm.dto.usuario.LoginResponse;
import com.glimpse.gpm.dto.usuario.RecuperacaoSenhaRequest;
import com.glimpse.gpm.dto.usuario.RedefinicaoSenhaRequest;
import com.glimpse.gpm.dto.usuario.UpdateUsuarioRequest;
import com.glimpse.gpm.dto.usuario.UsuarioCadastroRequest;
import com.glimpse.gpm.dto.usuario.UsuarioRequest;
import com.glimpse.gpm.dto.usuario.UsuarioResponse;
import com.glimpse.gpm.entity.Usuario;
import com.glimpse.gpm.enums.Role;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import com.glimpse.gpm.service.AutenticacaoService;
import com.glimpse.gpm.service.TokenService;
import com.glimpse.gpm.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Operation(summary = "Cadastrar novo usuário (cadastro público - papel forçado para PADRAO)", description = "Rota pública. Cria um novo usuário com papel PADRAO para acesso ao painel do sistema.")
    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioCadastroRequest cadastroRequest) {
        Usuario usuario = new Usuario();
        usuario.setNome(cadastroRequest.getNome());
        usuario.setCpf(cadastroRequest.getCpf());
        usuario.setEmail(cadastroRequest.getEmail());
        usuario.setSenha(cadastroRequest.getSenha()); // Senha será criptografada no service
        usuario.setRole(Role.PADRAO); // Forçado para PADRAO - não aceita role do cliente

        Usuario usuarioSalvo = service.salvar(usuario);
        UsuarioResponse response = new UsuarioResponse(
                usuarioSalvo.getId(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getCpf(),
                usuarioSalvo.getEmail(),
                usuarioSalvo.getRole()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Criar novo usuário ADMIN (apenas administradores)", description = "Requer role ADMIN. Cria um novo usuário com papel definido (ADMIN ou PADRAO).")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<UsuarioResponse> criarAdmin(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioRequest.getNome());
        usuario.setCpf(usuarioRequest.getCpf());
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setSenha(usuarioRequest.getSenha()); // Senha será criptografada no service
        usuario.setRole(usuarioRequest.getRole() != null ? usuarioRequest.getRole() : Role.PADRAO);

        Usuario usuarioSalvo = service.salvar(usuario);
        UsuarioResponse response = new UsuarioResponse(
                usuarioSalvo.getId(),
                usuarioSalvo.getNome(),
                usuarioSalvo.getCpf(),
                usuarioSalvo.getEmail(),
                usuarioSalvo.getRole()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos os usuários", description = "Requer autenticação JWT. Admin vê todos os usuários, PADRAO vê apenas a si mesmo.")
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        Usuario usuarioLogado = usuarioAutenticado();
        List<Usuario> usuarios = usuarioLogado.getRole() == Role.ADMIN
                ? service.listarUsuarios()
                : List.of(usuarioLogado);

        List<UsuarioResponse> responses = usuarios.stream()
                .map(u -> new UsuarioResponse(u.getId(), u.getNome(), u.getCpf(), u.getEmail(), u.getRole()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Requer autenticação JWT. Retorna os detalhes de um usuário específico.")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorId(@PathVariable Long id) {
        Usuario usuarioLogado = usuarioAutenticado();
        if (usuarioLogado.getRole() != Role.ADMIN && !usuarioLogado.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Usuario> usuarioOptional = service.buscarUsuarioPorId(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            UsuarioResponse response = new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getCpf(), usuario.getEmail(), usuario.getRole());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Atualizar usuário", description = "Requer autenticação JWT. Admin pode atualizar qualquer usuário; PADRAO só o próprio usuário e sem alterar role.")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id, @RequestBody UpdateUsuarioRequest usuarioRequest) {
        Usuario usuarioLogado = usuarioAutenticado();

        if (usuarioLogado.getRole() != Role.ADMIN && !usuarioLogado.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if (usuarioLogado.getRole() == Role.PADRAO && usuarioRequest.getRole() != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Usuario usuarioAtualizado = service.atualizarParcial(id, usuarioRequest);
            UsuarioResponse response = new UsuarioResponse(
                    usuarioAtualizado.getId(),
                    usuarioAtualizado.getNome(),
                    usuarioAtualizado.getCpf(),
                    usuarioAtualizado.getEmail(),
                    usuarioAtualizado.getRole()
            );
            return ResponseEntity.ok(response);
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Excluir usuário", description = "Requer autenticação JWT. Apenas ADMINs podem excluir usuários.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        Usuario usuarioLogado = usuarioAutenticado();
        if (usuarioLogado.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        boolean excluido = service.deletar(id);
        if (excluido) {
            return ResponseEntity.noContent().build();
        }
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

            LoginResponse response = new LoginResponse(
                    usuarioLogado.getId(),
                    usuarioLogado.getNome(),
                    usuarioLogado.getEmail(),
                    usuarioLogado.getCpf(),
                    usuarioLogado.getRole(),
                    tokenJWT
            );
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

    private Usuario usuarioAutenticado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Usuario usuario) {
            return usuario;
        }
        throw new RecursoNaoEncontradoException("Usuário autenticado não encontrado.");
    }
}
