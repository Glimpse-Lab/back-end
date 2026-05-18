package com.glimpse.gpm.service;

import com.glimpse.gpm.dto.usuario.UpdateUsuarioRequest;
import com.glimpse.gpm.entity.Usuario;
import com.glimpse.gpm.exception.DadosInvalidosException;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import com.glimpse.gpm.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Salva um novo usuário com a senha criptografada
     * @param usuario Dados do usuário a ser salvo
     * @return Usuário salvo
     */
    public Usuario salvar(Usuario usuario) {
        validarDadosUsuario(usuario);
        
        if (repository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new DadosInvalidosException("Email já cadastrado.");
        }

        // Criptografa a senha usando BCrypt
        String senhaHash = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaHash);

        return repository.save(usuario);
    }

    /**
     * Lista todos os usuários
     * @return Lista de usuários
     */
    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }

    /**
     * Busca um usuário por ID
     * @param id ID do usuário
     * @return Optional contendo o usuário ou vazio
     */
    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return repository.findById(id);
    }

    /**
     * Atualiza os dados de um usuário
     * @param id ID do usuário
     * @param usuarioAtualizado Dados atualizados
     * @return Usuário atualizado
     */
    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        Optional<Usuario> usuarioEncontrado = repository.findById(id);

        if (usuarioEncontrado.isEmpty()) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado.");
        }

        Usuario usuarioBanco = usuarioEncontrado.get();

        // Atualizar apenas campos que foram fornecidos (não null/vazios)
        if (usuarioAtualizado.getNome() != null && !usuarioAtualizado.getNome().isBlank()) {
            usuarioBanco.setNome(usuarioAtualizado.getNome());
        }
        if (usuarioAtualizado.getCpf() != null && !usuarioAtualizado.getCpf().isBlank()) {
            usuarioBanco.setCpf(usuarioAtualizado.getCpf());
        }
        if (usuarioAtualizado.getEmail() != null && !usuarioAtualizado.getEmail().isBlank()) {
            usuarioBanco.setEmail(usuarioAtualizado.getEmail());
        }
        
        // Se for atualizar a senha, criptografa
        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
            usuarioBanco.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
        }

        return repository.save(usuarioBanco);
    }

    /**
     * Atualização parcial com UpdateUsuarioRequest (campos opcionais)
     * @param id ID do usuário
     * @param updateRequest Dados com campos opcionais
     * @return Usuário atualizado
     */
    public Usuario atualizarParcial(Long id, UpdateUsuarioRequest updateRequest) {
        Optional<Usuario> usuarioEncontrado = repository.findById(id);

        if (usuarioEncontrado.isEmpty()) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado.");
        }

        Usuario usuarioBanco = usuarioEncontrado.get();

        // Atualizar apenas campos fornecidos (merge seguro)
        if (updateRequest.getNome() != null && !updateRequest.getNome().isBlank()) {
            usuarioBanco.setNome(updateRequest.getNome());
        }
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().isBlank()) {
            usuarioBanco.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getSenha() != null && !updateRequest.getSenha().isBlank()) {
            usuarioBanco.setSenha(passwordEncoder.encode(updateRequest.getSenha()));
        }
        if (updateRequest.getRole() != null) {
            usuarioBanco.setRole(updateRequest.getRole());
        }

        return repository.save(usuarioBanco);
    }

    /**
     * Deleta um usuário
     * @param id ID do usuário
     * @return true se deletado, false caso contrário
     */
    public boolean deletar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Valida as credenciais de um usuário
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @return Usuário autenticado
     */
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuarioEncontrado = repository.findByEmail(email);

        if (usuarioEncontrado.isEmpty()) {
            throw new DadosInvalidosException("Email ou senha inválidos.");
        }

        Usuario usuario = usuarioEncontrado.get();

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new DadosInvalidosException("Email ou senha inválidos.");
        }

        return usuario;
    }

    /**
     * Valida os dados básicos do usuário
     * @param usuario Usuário a ser validado
     */
    private void validarDadosUsuario(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new DadosInvalidosException("O nome do usuário é obrigatório.");
        }
        if (usuario.getCpf() == null || usuario.getCpf().trim().isEmpty()) {
            throw new DadosInvalidosException("O CPF do usuário é obrigatório.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new DadosInvalidosException("O email do usuário é obrigatório.");
        }
        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            throw new DadosInvalidosException("A senha do usuário é obrigatória.");
        }
    }
}
