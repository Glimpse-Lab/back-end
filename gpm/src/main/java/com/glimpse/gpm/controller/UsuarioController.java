package com.glimpse.gpm.controller;

import com.glimpse.gpm.config.ApiErrorResponses;
import com.glimpse.gpm.entity.Usuario;
import com.glimpse.gpm.service.UsuarioService;
import com.glimpse.gpm.service.TokenService;
import com.glimpse.gpm.dto.usuario.UsuarioRequest;
import com.glimpse.gpm.dto.usuario.UsuarioResponse;
import com.glimpse.gpm.dto.usuario.LoginRequest;
import com.glimpse.gpm.dto.usuario.LoginResponse;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
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

    public UsuarioController(UsuarioService service, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.service = service;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Cadastrar novo usuário", description = "Rota pública. Cria um novo administrador/usuário para acesso ao painel do sistema.")
    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioRequest.getNome());
        usuario.setCpf(usuarioRequest.getCpf());
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setSenha(usuarioRequest.getSenha());

        Usuario usuarioSalvo = service.salvar(usuario);
        UsuarioResponse response = new UsuarioResponse(usuarioSalvo.getId(), usuarioSalvo.getNome(), usuarioSalvo.getCpf(), usuarioSalvo.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos os usuários", description = "Requer autenticação JWT. Retorna a lista de usuários cadastrados no painel.")
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        List<Usuario> usuarios = service.listarUsuarios();
        List<UsuarioResponse> responses = usuarios.stream()
                .map(u -> new UsuarioResponse(u.getId(), u.getNome(), u.getCpf(), u.getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Requer autenticação JWT. Retorna os detalhes de um usuário específico.")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = service.buscarUsuarioPorId(id);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            UsuarioResponse response = new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getCpf(), usuario.getEmail());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualizar usuário", description = "Requer autenticação JWT. Atualiza os dados de acesso de um usuário existente.")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest usuarioRequest) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNome(usuarioRequest.getNome());
            usuario.setCpf(usuarioRequest.getCpf());
            usuario.setEmail(usuarioRequest.getEmail());
            usuario.setSenha(usuarioRequest.getSenha());

            Usuario usuarioAtualizado = service.atualizar(id, usuario);
            UsuarioResponse response = new UsuarioResponse(usuarioAtualizado.getId(), usuarioAtualizado.getNome(), usuarioAtualizado.getCpf(), usuarioAtualizado.getEmail());
            return ResponseEntity.ok(response);
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Excluir usuário", description = "Requer autenticação JWT. Remove o acesso de um usuário ao painel do sistema.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        boolean excluido = service.deletar(id);

        if (excluido) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Realizar Login", description = "Rota pública. Valida as credenciais e retorna o Token JWT necessário para acessar as rotas protegidas.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // 1. Cria o token de tentativa de login (Ainda não é o JWT)
            var authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha());

            // 2. O Spring bate no banco e confere se a senha bate com o hash BCrypt
            var authentication = authenticationManager.authenticate(authenticationToken);

            // 3. Pega o usuário logado
            Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

            // 4. Gera o Token JWT real
            String tokenJWT = tokenService.gerarToken(usuarioLogado);

            // 5. Monta a resposta enviando o TOKEN:
            LoginResponse response = new LoginResponse(
                    usuarioLogado.getId(),
                    usuarioLogado.getNome(),
                    usuarioLogado.getEmail(),
                    usuarioLogado.getCpf(),
                    tokenJWT
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Se a senha estiver errada ou usuário não existir:
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}