package com.glimpse.gpm.service;

import com.glimpse.gpm.model.Usuario;
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
            throw new IllegalArgumentException("O nome do usuário é obrigatório.");
        }
        if (usuario.getCpf() == null || usuario.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do usuário é obrigatório.");
        }
        if (repository.existsByCpf(usuario.getCpf())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com esse CPF.");
        }

        String senhaHash = org.mindrot.jbcrypt.BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
        usuario.setSenha(senhaHash);

        return repository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return repository.findAll();
    }

    public Optional<Usuario> buscarUsuarioPorId(Integer id) {
        return repository.findById(id);
    }

    public Usuario atualizar(Integer id, Usuario usuarioAtualizado) {
        Optional<Usuario> usuarioEncontrado = repository.findById(id);

        if (usuarioEncontrado.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        Usuario usuarioBanco = usuarioEncontrado.get();

        if (!usuarioBanco.getCpf().equals(usuarioAtualizado.getCpf()) && repository.existsByCpf(usuarioAtualizado.getCpf())) {
            throw new IllegalArgumentException("O novo CPF fornecido já está em uso por outro usuário.");
        }

        usuarioBanco.setNome(usuarioAtualizado.getNome());
        usuarioBanco.setCpf(usuarioAtualizado.getCpf());
        usuarioBanco.setEmail(usuarioAtualizado.getEmail());
        usuarioBanco.setCelular(usuarioAtualizado.getCelular());

        return repository.save(usuarioBanco);
    }

    public boolean deletar(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
