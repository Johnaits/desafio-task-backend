🚀 Task Management API
Uma API RESTful robusta e escalável para gestão de projetos e tarefas, desenvolvida com Spring Boot 3 e Java. Este projeto aplica princípios sólidos de Arquitetura de Software, Clean Code e Design Patterns para entregar uma solução sustentável e de alta performance.

⚙️ Tecnologias Utilizadas
Java 17+

Spring Boot 3 (Web, Data JPA, Security, Validation)

Banco de Dados: H2 Database (In-Memory para facilidade de testes)

Segurança: Spring Security + JWT (JSON Web Tokens)

Documentação: Swagger/OpenAPI 3 & Postman Collection

Testes: JUnit 5 & Mockito

🛠️ Instruções para Rodar o Projeto
Como o projeto utiliza um banco em memória (H2), não é necessária nenhuma configuração externa de infraestrutura para testá-lo localmente.

Pré-requisitos: Ter o Java (JDK 17 ou superior) e o Maven instalados.

Clone o repositório:

Bash
git clone https://github.com/Johnaits/desafio-task-backend.git
cd desafio-task-backend
Execute a aplicação via Maven:

Bash
mvn spring-boot:run
Acesse a Documentação (Swagger):
Com a aplicação rodando, acesse no seu navegador: http://localhost:8080/swagger-ui.html

Acesse o Console do Banco de Dados (H2):
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:taskdb
User: admin (senha em branco)

Nota sobre o Postman: Uma collection completa com variáveis de ambiente está disponível na pasta docs/ na raiz do projeto.

🧠 Decisões Técnicas e Trade-offs
A arquitetura desta API foi desenhada com foco em manutenibilidade e separação de responsabilidades. Abaixo estão as principais decisões:

1. Design de Regras de Negócio (Strategy Pattern)
   O domínio de Tarefas (Task) possui regras de negócio complexas (ex: limite de WIP de 5 tarefas, permissões exclusivas de ADMIN para fechar tarefas críticas).

Decisão: Em vez de poluir o TaskService com dezenas de blocos if/else, foi implementado o padrão de projeto Strategy através da interface TaskRule. As regras são injetadas dinamicamente via Spring (List<TaskRule> validators).

Trade-off: Adiciona um pouco mais de verbosidade ao criar novas classes para cada regra, mas garante o princípio Open/Closed do SOLID. Para adicionar uma nova regra no futuro, basta criar uma nova classe, sem alterar o Service principal.

2. Buscas Dinâmicas (JPA Specifications)
   O requisito de listagem exigia múltiplos filtros opcionais (status, prioridade, responsável, range de datas e texto).

Decisão: Utilização do JPA Specifications (Criteria API) para construir as cláusulas WHERE de forma dinâmica e atômica.

Trade-off: A sintaxe do CriteriaBuilder é mais complexa e menos legível que uma anotação @Query com JPQL puro. No entanto, evita o famoso "Frankenstein" de queries com OR param IS NULL, melhorando drasticamente o plano de execução no banco de dados.

3. Relatório Estatístico (Class-based Projections)
   Decisão: Para o endpoint de relatórios estatísticos, a agregação (COUNT e GROUP BY) foi delegada inteiramente para o banco de dados. O JPA mapeia o resultado diretamente para um DTO (TaskReportDTO).

Trade-off: Exige a criação de construtores específicos no DTO usando o caminho completo da classe na query, o que gera um leve acoplamento estrutural, mas evita trazer milhares de entidades para a memória do Java, mantendo a performance O(1) em transferência de dados.

4. Busca Textual (Full Table Scan vs Full-Text Search)
   Decisão no Teste: Para manter o projeto portátil e rodando sem dependências externas (usando o H2), a busca de texto foi implementada usando LIKE '%termo%' com lower().

Nota de Arquitetura: Em um cenário real de produção (ex: rodando PostgreSQL), essa abordagem geraria um Full Table Scan, prejudicando a performance em grandes volumes. A solução definitiva seria substituir essa Specification por uma query nativa utilizando uma coluna tsvector com índice GIN ou a extensão pg_trgm nativa do Postgres.

🔮 O que eu faria diferente (com mais tempo)
Se o escopo de tempo fosse maior, os seguintes aprimoramentos seriam o próximo passo na evolução da arquitetura:

Migração e Containerização (Docker + PostgreSQL): Substituiria o banco H2 por um contêiner PostgreSQL usando o docker-compose. Isso permitiria implementar os recursos nativos de Full-Text Search mencionados acima.

Migrations de Banco de Dados: Removeria o spring.jpa.hibernate.ddl-auto=update e implementaria o Flyway ou Liquibase para versionamento seguro do schema do banco de dados.

Camada de Cache: Adicionaria suporte ao Redis para o endpoint de Relatórios, pois dados estatísticos não costumam exigir consistência em tempo real e se beneficiam enormemente de cache.

Frontend: Implementação de FrontEnd que consumisse os dados da API e mostrasse como um "Quadro Kanban" ao usuário.

Testes de Integração: ampliação dos testes para aumento da cobertura.