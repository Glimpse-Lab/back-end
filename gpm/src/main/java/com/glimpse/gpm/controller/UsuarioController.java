package com.glimpse.gpm.controller;

import com.glimpse.gpm.config.ApiErrorResponses;
import com.glimpse.gpm.dto.usuario.*;
import com.glimpse.gpm.entity.Usuario;
import com.glimpse.gpm.enums.Role;
import com.glimpse.gpm.service.AutenticacaoService;
import com.glimpse.gpm.service.UsuarioService;
import com.glimpse.gpm.service.TokenService;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AutenticacaoService autenticacaoService;

    public UsuarioController(UsuarioService service, AuthenticationManager authenticationManager, TokenService tokenService, AutenticacaoService autenticacaoService) {
        this.service = service;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.autenticacaoService = autenticacaoService;
    }

    @Operation(summary = "Cadastrar novo usuário", description = "Rota pública. Cria um novo administrador/usuário para acesso ao painel do sistema.")
    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioRequest.getNome());
        usuario.setCpf(usuarioRequest.getCpf());
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setSenha(usuarioRequest.getSenha());
        
        // Atribuir role se informado, senão usa PADRAO como padrão
        if (usuarioRequest.getRole() != null) {
            usuario.setRole(usuarioRequest.getRole());
        } else {
            usuario.setRole(Role.PADRAO);
        }

        Usuario usuarioSalvo = service.salvar(usuario);
        UsuarioResponse response = new UsuarioResponse(usuarioSalvo.getId(), usuarioSalvo.getNome(), usuarioSalvo.getCpf(), usuarioSalvo.getEmail(), usuarioSalvo.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar todos os usuários", description = "Requer autenticação JWT. Apenas ADMINs podem listar todos. Usuários PADRAO veem apenas a si mesmos.")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PADRAO')")
    public ResponseEntity<List<UsuarioResponse>> listar() {
        // Obter usuário logado do contexto de segurança
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        List<Usuario> usuarios;
        
        // Se o usuário é ADMIN, lista todos
        if (principal instanceof Usuario) {
            Usuario usuarioLogado = (Usuario) principal;
            if (usuarioLogado.getRole() == Role.ADMIN) {
                usuarios = service.listarUsuarios();
            } else {
                // Se é PADRAO, lista apenas a si mesmo
                usuarios = List.of(usuarioLogado);
            }
        } else {
            // Fallback: retorna lista vazia se não conseguir autenticar
            usuarios = List.of();
        }
        
        List<UsuarioResponse> responses = usuarios.stream()
                .map(u -> new UsuarioResponse(u.getId(), u.getNome(), u.getCpf(), u.getEmail(), u.getRole()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Requer autenticação JWT. Retorna os detalhes de um usuário específico.")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PADRAO')")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = service.buscarUsuarioPorId(id);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            UsuarioResponse response = new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getCpf(), usuario.getEmail(), usuario.getRole());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualizar usuário", description = "Requer autenticação JWT. Usuários PADRAO só podem atualizar a si mesmos (nome, email, senha) - NÃO podem alterar role. ADMINs podem atualizar qualquer usuário e sua role.")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PADRAO')")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id, @RequestBody UpdateUsuarioRequest usuarioRequest) {
        try {
            // Obter usuário logado do contexto de segurança
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            
            // Verificar permissões
            if (principal instanceof Usuario) {
                Usuario usuarioLogado = (Usuario) principal;
                
                // Se não é ADMIN e não é o próprio usuário, retornar 403
                if (usuarioLogado.getRole() != Role.ADMIN && !usuarioLogado.getId().equals(id)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
                
                // Se é PADRAO, NÃO pode alterar role de forma alguma (nem sua própria)
                if (usuarioLogado.getRole() == Role.PADRAO && usuarioRequest.getRole() != null) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            // Passar a requisição de atualização para o service (que fará o merge)
            Usuario usuarioAtualizado = service.atualizarParcial(id, usuarioRequest);
            UsuarioResponse response = new UsuarioResponse(usuarioAtualizado.getId(), usuarioAtualizado.getNome(), usuarioAtualizado.getCpf(), usuarioAtualizado.getEmail(), usuarioAtualizado.getRole());
            return ResponseEntity.ok(response);
        } catch (RecursoNaoEncontradoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Excluir usuário", description = "Requer autenticação JWT. Apenas ADMINs podem excluir usuários.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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

            // 5. Monta a resposta enviando o TOKEN com a role:
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
            // Se a senha estiver errada ou usuário não existir:
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
