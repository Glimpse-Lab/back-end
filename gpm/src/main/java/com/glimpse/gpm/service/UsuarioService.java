package com.glimpse.gpm.service;

import com.glimpse.gpm.entity.Usuario;
import com.glimpse.gpm.exception.DadosInvalidosException;
import com.glimpse.gpm.exception.RecursoNaoEncontradoException;
import com.glimpse.gpm.repository.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario salvar(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new DadosInvalidosException("O nome do usuário é obrigatório.");
        }
        if (usuario.getCpf() == null || usuario.getCpf().trim().isEmpty()) {
            throw new DadosInvalidosException("O CPF do usuário é obrigatório.");
        }

        String senhaHash = org.mindrot.jbcrypt.BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
        usuario.setSenha(senhaHash);

        return repository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }

    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return repository.findById(id);
    }

    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        Optional<Usuario> usuarioEncontrado = repository.findById(id);

        if (usuarioEncontrado.isEmpty()) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado.");
        }

        Usuario usuarioBanco = usuarioEncontrado.get();

        usuarioBanco.setNome(usuarioAtualizado.getNome());
        usuarioBanco.setCpf(usuarioAtualizado.getCpf());
        usuarioBanco.setEmail(usuarioAtualizado.getEmail());

        return repository.save(usuarioBanco);
    }

    public boolean deletar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuarioEncontrado = repository.findByEmail(email);

        if (usuarioEncontrado.isEmpty()) {
            throw new DadosInvalidosException("Usuário não encontrado.");
        }

        Usuario usuario = usuarioEncontrado.get();

        if (!BCrypt.checkpw(senha, usuario.getSenha())) {
            throw new DadosInvalidosException("Senha inválida.");
        }

        return usuario;
    }
}
