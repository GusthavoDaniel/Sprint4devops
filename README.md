# RFID Tracking Application

Este projeto é uma aplicação Spring Boot para rastreamento de motos com RFID, desenvolvida como parte do Challenge 2025 - 2º Semestre.

## Tecnologias Utilizadas

*   **Java 17**
*   **Spring Boot 3.2.5**
*   **Spring Data JPA**
*   **PostgreSQL** (Azure Database for PostgreSQL Flexible Server)
*   **Flyway** para migrações de banco de dados
*   **ModelMapper** para DTOs
*   **Thymeleaf** para a camada de visualização (frontend)
*   **Spring Security** para autenticação e controle de acesso

## Configuração do Ambiente Local

### Pré-requisitos

*   Java Development Kit (JDK) 17
*   Maven 3.x
*   Docker (opcional, para rodar PostgreSQL localmente)

### Rodando a Aplicação Localmente

1.  **Clone o repositório:**

    ```bash
    git clone <URL_DO_SEU_REPOSITORIO>
    cd rfid-tracking
    ```

2.  **Configurar Banco de Dados (PostgreSQL - Local com Docker)**

    Se você deseja rodar o PostgreSQL localmente via Docker, execute:

    ```bash
    docker run --name rfid-postgres -e POSTGRES_USER=rfiduser -e POSTGRES_PASSWORD=rfidpass -e POSTGRES_DB=rfidtrackingdb -p 5432:5432 -d postgres:13
    ```

    Aguarde alguns segundos para o container iniciar.

3.  **Configurar `application-dev.properties` (se existir ou criar)**

    Certifique-se de que seu `src/main/resources/application-dev.properties` (ou `application.properties` se não usar perfis) esteja configurado para o PostgreSQL local:

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/rfidtrackingdb
    spring.datasource.username=rfiduser
    spring.datasource.password=rfidpass
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.format_sql=true
    spring.flyway.enabled=true
    ```

4.  **Build do Projeto:**

    ```bash
    mvn clean install
    ```

5.  **Executar a Aplicação:**

    ```bash
    mvn spring-boot:run
    ```

    

## Deploy para Azure App Service

Este projeto utiliza o Azure App Service para deploy e Azure Database for PostgreSQL Flexible Server como banco de dados. Os scripts CLI para provisionamento estão localizados em `devops_scripts/provision_azure.sh`.

### Pré-requisitos para Deploy

*   **Azure CLI** instalado e configurado.
*   Uma conta Azure ativa.

### Passos para o Deploy

1.  **Autenticar no Azure CLI:**

    ```bash
    az login
    ```

2.  **Executar o Script de Provisionamento:**

    Navegue até o diretório `devops_scripts` e execute o script:

    ```bash
    cd devops_scripts
    chmod +x provision_azure.sh
    ./provision_azure.sh
    ```

    **IMPORTANTE:** O script `provision_azure.sh` contém uma senha de exemplo (`YourStrongPassword123!`) para o administrador do PostgreSQL. **Substitua-a por uma senha forte e segura antes de executar o script em um ambiente real.**

    Este script irá:
    *   Criar um Grupo de Recursos.
    *   Criar um Plano do Serviço (App Service Plan).
    *   Criar o Serviço de Aplicativo (Web App) para Java 17.
    *   Criar um Azure Database for PostgreSQL Flexible Server.
    *   Criar o banco de dados `rfidtrackingdb` no servidor PostgreSQL.
    *   Configurar as variáveis de ambiente (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `SPRING_JPA_HIBERNATE_DDL_AUTO`) no App Service.

3.  **Build e Deploy da Aplicação:**

    Após o provisionamento dos recursos, você pode fazer o deploy da sua aplicação Spring Boot para o Azure App Service. Certifique-se de estar no diretório raiz do projeto (`rfid-tracking`).

    ```bash
    mvn clean package
    az webapp deploy --resource-group rfid-tracking-rg --name rfid-tracking-app --src-path target/*.jar
    ```

    A URL da sua aplicação será `https://rfid-tracking-app.azurewebsites.net/mottu` (substitua `rfid-tracking-app` pelo nome real do seu App Service, se diferente).

## Estrutura do Banco de Dados (DDL)

O DDL (Data Definition Language) para as tabelas do banco de dados pode ser encontrado no arquivo `script_bd.sql` na raiz do projeto.

## Testes (Exemplos de CRUD)

Após o deploy, você pode testar as funcionalidades CRUD da API. Utilize ferramentas como Postman, Insomnia ou `curl`.

**Exemplo de Endpoints (base: `https://rfid-tracking-app.azurewebsites.net/mottu`)**

*   **Filiais:**
    *   `GET /filiais` - Listar todas as filiais
    *   `POST /filiais` - Criar nova filial
    *   `GET /filiais/{id}` - Obter filial por ID
    *   `PUT /filiais/{id}` - Atualizar filial
    *   `DELETE /filiais/{id}` - Excluir filial

*   **Motos:**
    *   `GET /motos` - Listar todas as motos
    *   `POST /motos` - Criar nova moto
    *   `GET /motos/{id}` - Obter moto por ID
    *   `PUT /motos/{id}` - Atualizar moto
    *   `DELETE /motos/{id}` - Excluir moto

*   **Registros RFID:**
    *   `GET /registros` - Listar todos os registros RFID
    *   `POST /registros` - Criar novo registro RFID
    *   `GET /registros/{id}` - Obter registro RFID por ID
    *   `PUT /registros/{id}` - Atualizar registro RFID
    *   `DELETE /registros/{id}` - Excluir registro RFID

**Exemplo de `curl` para criar uma filiall:**

```bash
curl -X POST \\
  https://rfid-tracking-app.azurewebsites.net/mottu/filiais \\
  -H 'Content-Type: application/json' \\
  -d '{"nome": "Nova Filial", "endereco": "Rua Teste, 100"}'
```

Lembre-se de substituir a URL base pela URL da sua aplicação no Azure.


