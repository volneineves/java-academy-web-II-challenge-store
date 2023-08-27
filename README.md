# Avanade Store

![Logo do Projeto](./assets/project_logo.png)

# API RESTful para Gerenciamento de Pedidos de Compra

## 💻 Objetivo do Projeto

Este projeto visa implementar uma API RESTful robusta e escalável para o gerenciamento de pedidos de compra. O projeto é um desafio do curso Java Academy, em um oferecimento da [Avanade](https://www.avanade.com/pt-br) e [Ada Tech](https://ada.tech/).

## ✨ Tecnologias Utilizadas

Esse projeto foi desenvolvido com as seguintes tecnologias:

- [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html): Linguagem de programação utilizada.
- [Spring Boot](https://spring.io/projects/spring-boot): Framework para facilitar o bootstrapping e o desenvolvimento de novas aplicações Spring.
   - [Spring Data JPA](https://spring.io/projects/spring-data-jpa): Para persistência de dados e manipulação do banco de dados.
   - [Spring Boot Starter Security](https://spring.io/guides/gs/securing-web/): Para funcionalidades relacionadas à segurança, como autenticação e autorização.
   - [Spring Boot Starter Validation](https://spring.io/guides/gs/validating-form-input/): Para validação de dados em formulários e outras entradas.
   - [Spring Boot Starter Web](https://spring.io/guides/gs/spring-boot/): Para desenvolvimento de aplicações web e RESTful.   
   - [Spring Cloud Starter OpenFeign](https://spring.io/projects/spring-cloud-openfeign): Para realização de chamadas HTTP de forma simplificada.
   - [Spring Boot Starter Mail](https://spring.io/guides/gs/sending-email/): Para funcionalidades de envio de e-mail.

- [Query DSL](http://querydsl.com/): Alternativa ao JPA Repository com foco nas operações e expressividades do SQL.
- [H2 Database](https://www.h2database.com/html/main.html): Banco de dados em memória para ambiente de desenvolvimento.
- [PostgreSQL](https://www.postgresql.org/): Banco de dados usado em produção.
- [Maven](https://maven.apache.org/): Ferramenta de gerenciamento de projetos e builds.

## ⚙️ Funcionalidades

- [x] Cadastro de usuários
    - [x] Validação de nome, CPF, e-mail e endereço
- [x] Integração com API externa para produtos: [DummyJSON API](https://dummyjson.com/products/search?q=phone)
    - [x] Recuperação e armazenamento de produtos
- [x] Listagem de produtos disponíveis
- [x] Finalização de pedidos de compra
- [x] Autenticação via JWT
- [x] Validação de estoque
- [x] Atualização de estoque
- [x] Envio de e-mail de confirmação para o cliente
- [ ] Envio de e-mail para o responsável do departamento de vendas

## 📄 Licença

Esse projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE.md) para mais detalhes.
