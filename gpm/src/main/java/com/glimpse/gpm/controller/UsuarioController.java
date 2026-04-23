package com.glimpse.gpm.controller;

import com.glimpse.gpm.entity.Usuario;
import com.glimpse.gpm.service.UsuarioService;
import com.glimpse.gpm.service.TokenService;
import com.glimpse.gpm.dto.usuario.UsuarioRequest;
import com.glimpse.gpm.dto.usuario.UsuarioResponse;
import com.glimpse.gpm.dto.usuario.LoginRequest;
import com.glimpse.gpm.dto.usuario.LoginResponse;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
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

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService service;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UsuarioController(UsuarioService service, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.service = service;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/cadastrar")
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

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        List<Usuario> usuarios = service.listarUsuarios();
        List<UsuarioResponse> responses = usuarios.stream()
                .map(u -> new UsuarioResponse(u.getId(), u.getNome(), u.getCpf(), u.getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        boolean excluido = service.deletar(id);

        if (excluido) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // 1. Cria o token de tentativa de login (Ainda não é o JWT)
            var authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha());

            // 2. O Spring bate no banco e confere se a senha bate com o hash BCrypt
            var authentication = authenticationManager.authenticate(authenticationToken);

            // 3. Deu certo! Pega o usuário logado
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