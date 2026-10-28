# GPM Esquadrias — Back-end

API REST para a **GPM Esquadrias**, desenvolvida com Spring Boot. Responsável por autenticação, gestão de produtos, projetos, orçamentos, clientes e usuários do sistema.

---

## Tecnologias

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.3 |
| Spring Security | (via Boot) |
| Spring Data JPA | (via Boot) |
| Spring Mail | (via Boot) |
| Thymeleaf | (via Boot) |
| PostgreSQL Driver | (via Boot) |
| JJWT | 0.11.5 |
| jBCrypt | 0.4 |
| SpringDoc OpenAPI (Swagger UI) | 2.8.0 |
| H2 (testes) | (via Boot) |
| Maven | 3.x |

---

## Pré-requisitos

- [Java 21](https://adoptium.net/) (JDK com preview habilitado)
- [Maven 3.x](https://maven.apache.org/) ou use o wrapper `mvnw` incluído
- [PostgreSQL](https://www.postgresql.org/) 14 ou superior em execução local
- [RabbitMQ](https://www.rabbitmq.com/) (opcional, para envio de e-mails assíncrono)

---

## Configuração

### Banco de dados

Crie um banco de dados PostgreSQL e configure as credenciais no arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA
```

O schema é gerado automaticamente via `spring.jpa.hibernate.ddl-auto=update`.

### Variáveis sensíveis (recomendado em produção)

| Propriedade | Variável de ambiente | Padrão (dev) |
|---|---|---|
| `app.jwt.secret` | `JWT_SECRET` | chave padrão insegura |
| `app.jwt.expiration` | `JWT_EXPIRATION` | `3600000` (1 hora) |

> **Atenção:** altere `app.jwt.secret` e as credenciais de banco antes de qualquer deploy em produção.

### E-mail (recuperação de senha)

O envio de e-mail utiliza o serviço **Brevo (SMTP)**. Configure em `application.properties`:

```properties
spring.mail.host=smtp-relay.brevo.com
spring.mail.port=587
spring.mail.username=SEU_USUARIO_BREVO
spring.mail.password=SUA_SENHA_BREVO
```

### Usuário administrador padrão

Na primeira inicialização, o `DataSeeder` cria automaticamente um usuário administrador com as credenciais definidas em:

```properties
app.admin.email=admin@gpm.com
app.admin.senha=admin123
```

---

## Instalação e execução

```bash
# Entrar na pasta do projeto
cd back-end/gpm

# Compilar e iniciar (usando o wrapper Maven)
./mvnw spring-boot:run        # Linux / macOS
mvnw.cmd spring-boot:run      # Windows

# Ou gerar o JAR e executar
./mvnw package
java -jar target/gpm-0.0.1-SNAPSHOT.jar
```

A API sobe na porta **8080** por padrão.

---

## Documentação da API (Swagger UI)

Com a aplicação em execução, acesse:

```
http://localhost:8080/swagger-ui.html
```

---

## Endpoints principais

### Autenticação — `/usuarios`

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| `POST` | `/usuarios` | Público | Cadastrar novo usuário |
| `POST` | `/usuarios/login` | Público | Login — retorna token JWT |
| `POST` | `/usuarios/recuperar-senha` | Público | Solicitar recuperação de senha |
| `POST` | `/usuarios/redefinir-senha` | Público | Redefinir senha via token |
| `GET` | `/usuarios` | JWT | Listar usuários |
| `GET` | `/usuarios/{id}` | JWT | Buscar usuário por ID |
| `PUT` | `/usuarios/{id}` | JWT | Atualizar usuário |
| `DELETE` | `/usuarios/{id}` | JWT | Excluir usuário |

### Produtos — `/produtos`

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| `POST` | `/produtos` | JWT | Cadastrar produto (multipart/form-data) |
| `GET` | `/produtos` | JWT | Listar todos os produtos |
| `GET` | `/produtos/{id}` | JWT | Buscar produto por ID |
| `PUT` | `/produtos/{id}` | JWT | Atualizar produto |
| `DELETE` | `/produtos/{id}` | JWT | Excluir produto |

### Projetos — `/projetos`

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| `POST` | `/projetos` | JWT | Adicionar projeto ao portfólio (multipart/form-data) |
| `GET` | `/projetos` | Público | Listar projetos do portfólio |
| `GET` | `/projetos/{id}` | Público | Buscar projeto por ID |
| `PUT` | `/projetos/{id}` | JWT | Atualizar projeto |
| `DELETE` | `/projetos/{id}` | JWT | Excluir projeto |

### Orçamentos — `/orcamentos`

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| `POST` | `/orcamentos` | Público | Criar orçamento |
| `POST` | `/orcamentos/com-cliente` | Público | Criar orçamento com criação automática de cliente |
| `GET` | `/orcamentos` | JWT | Listar todos os orçamentos |
| `GET` | `/orcamentos/{id}` | JWT | Buscar orçamento por ID |
| `PUT` | `/orcamentos/{id}` | JWT | Atualizar orçamento |
| `PUT` | `/orcamentos/{id}/status` | JWT | Atualizar status do orçamento |
| `DELETE` | `/orcamentos/{id}` | JWT | Excluir orçamento |

---

## Status de orçamento

| Valor | Descrição |
|---|---|
| `PENDENTE` | Aguardando análise |
| `PROPOSTA_ENVIADA` | Proposta encaminhada ao cliente |
| `EM_ANDAMENTO` | Em processo de execução |
| `APROVADO` | Orçamento aprovado |
| `RECUSADO` | Orçamento recusado |
| `CANCELADO` | Cancelado |

---

## Perfis de usuário (Roles)

| Role | Permissões |
|---|---|
| `ADMIN` | Acesso total — vê e gerencia todos os recursos |
| `PADRAO` | Acesso restrito — vê apenas seus próprios dados |

---

## Estrutura do projeto

```
src/
├── main/
│   ├── java/com/glimpse/gpm/
│   │   ├── GpmApplication.java         # Entry point
│   │   ├── config/                     # Configurações gerais
│   │   │   ├── AsyncConfig.java        # Pool de threads assíncrono
│   │   │   ├── DataSeeder.java         # Seed do usuário admin padrão
│   │   │   ├── OpenApiConfig.java      # Configuração do Swagger
│   │   │   └── WebConfig.java          # CORS e recursos estáticos
│   │   ├── controller/                 # Controllers REST
│   │   ├── dto/                        # Objetos de transferência (Request/Response)
│   │   ├── entity/                     # Entidades JPA
│   │   ├── enums/                      # Role, StatusOrcamento
│   │   ├── exception/                  # Exceções customizadas e handler global
│   │   ├── mapper/                     # Conversão entre entidades e DTOs
│   │   ├── repository/                 # Interfaces Spring Data JPA
│   │   ├── security/                   # SecurityConfig e SecurityFilter (JWT)
│   │   └── service/                    # Regras de negócio
│   └── resources/
│       ├── application.properties
│       └── templates/
│           └── email-recuperacao.html  # Template Thymeleaf para e-mail
└── test/
    ├── java/com/glimpse/gpm/
    │   ├── controller/                 # Testes de integração dos controllers
    │   └── service/                    # Testes unitários dos services
    └── resources/
        └── application-test.properties # H2 in-memory para testes
```

---

## Testes

```bash
# Executar todos os testes
./mvnw test        # Linux / macOS
mvnw.cmd test      # Windows
```

Os testes utilizam banco **H2 in-memory** com o perfil `test`, sem necessidade de PostgreSQL.

---

## Upload de arquivos

Imagens de produtos e projetos são salvas localmente na pasta `gpm_uploads/` (na raiz de `back-end/`). Os arquivos são servidos como recursos estáticos pela própria API.
