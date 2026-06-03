package com.glimpse.gpm.config;

import com.glimpse.gpm.entity.Categoria;
import com.glimpse.gpm.entity.Usuario;
import com.glimpse.gpm.enums.Role;
import com.glimpse.gpm.repository.CategoriaRepository;
import com.glimpse.gpm.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Lendo as chaves de segurança do application.properties / .env
    @Value("${app.admin.nome}")
    private String adminNome;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.cpf}")
    private String adminCpf;

    @Value("${app.admin.senha}")
    private String adminSenha;

    public DataSeeder(CategoriaRepository categoriaRepository,
                      UsuarioRepository usuarioRepository,
                      PasswordEncoder passwordEncoder) {
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // 1. Seeding de Categorias
        if (categoriaRepository.count() == 0) {
            Categoria c1 = new Categoria(); c1.setNome("Janelas");
            Categoria c2 = new Categoria(); c2.setNome("Portas");
            Categoria c3 = new Categoria(); c3.setNome("Sistemas");
            Categoria c4 = new Categoria(); c4.setNome("Acessórios");

            Categoria c5 = new Categoria(); c5.setNome("Residencial");
            Categoria c6 = new Categoria(); c6.setNome("Comercial");
            Categoria c7 = new Categoria(); c7.setNome("Corporativo");

            categoriaRepository.saveAll(Arrays.asList(c1, c2, c3, c4, c5, c6, c7));
            System.out.println("✅ [DataSeeder] Categorias iniciais populadas no PostgreSQL.");
        }

        // 2. Seeding do Usuário Admin
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setNome(adminNome);
            admin.setEmail(adminEmail);
            admin.setCpf(adminCpf);

            // Senha criptografada via BCrypt injetado do SecurityConfig
            admin.setSenha(passwordEncoder.encode(adminSenha));

            // Atribuindo a Role do Enum oficial
            admin.setRole(Role.ADMIN);

            usuarioRepository.save(admin);
            System.out.println("✅ [DataSeeder] Conta Administrador criada com sucesso com os dados do ambiente.");
        }
    }
}