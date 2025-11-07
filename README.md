# DevOps Tools & Cloud Computing - Sprint 4

Este projeto é uma aplicação Spring Boot para rastreamento de motos com RFID, desenvolvida como parte da entrega obrigatória da Sprint 4 da disciplina **DevOps Tools & Cloud Computing**.

## 1. Descrição da Solução (Requisito 1)

A solução consiste em uma **API RESTful** desenvolvida em **Java** com o framework **Spring Boot**, que simula o rastreamento de motos por RFID. A aplicação permite o gerenciamento de Filiais, Motos e Registros RFID, implementando as operações básicas de **CRUD (Create, Read, Update, Delete)**.

### Stack de Tecnologias

| Componente | Tipo | Tecnologia/Ferramenta | Descrição Funcional |
| :--- | :--- | :--- | :--- |
| **Aplicação** | Backend API | Java 17, Spring Boot 3.2.5 | API RESTful para as operações de CRUD. |
| **Banco de Dados** | PaaS em Nuvem | PostgreSQL (Azure Database for PostgreSQL) | Armazenamento persistente dos dados da aplicação. |
| **Containerização** | Imagem | Docker | Empacotamento da aplicação para deploy em ambiente de contêiner. |
| **Repositório de Código** | SCM | GitHub | Versionamento do código-fonte. |
| **Orquestrador CI/CD** | Pipeline | Azure DevOps Pipelines | Automação do processo de Build (CI) e Deploy (CD). |
| **Ambiente de Deploy** | PaaS em Nuvem | Azure Web App for Containers | Serviço de aplicação para hospedar o contêiner Docker. |

## 2. Diagrama da Arquitetura e Fluxo CI/CD (Requisito 2)

O fluxo de CI/CD é acionado por um `push` de código para a branch `master` no **GitHub**.

1.  **Desenvolvedor** faz um `push` para o **GitHub**.
2.  O **Azure DevOps Pipelines** detecta a alteração (Regra II).
3.  **CI (Build)**: O pipeline executa o build do projeto (Maven), roda os testes unitários e constrói a **Imagem Docker** da aplicação.
4.  A **Imagem Docker** é enviada para o **Azure Container Registry (ACR)** (Regra V).
5.  **CD (Deploy)**: O pipeline de deploy é acionado (Regra III).
6.  O pipeline de deploy utiliza a **Conexão de Serviço** e as **Variáveis de Ambiente Protegidas** (Regra IV) para:
    *   Configurar o **Azure Web App for Containers** para puxar a nova imagem do **ACR**.
    *   O **Azure Web App** hospeda o contêiner.
7.  A aplicação se conecta ao **Azure Database for PostgreSQL** usando as variáveis de ambiente.
8.  O **Usuário Final** acessa a aplicação via URL do Azure Web App.

**(Nota: O diagrama visual deve ser incluído no PDF de entrega, conforme Requisito 2.)**

## 3. Detalhamento dos Componentes (Requisito 3)

| Nome do componente | Tipo | Descrição funcional | Tecnologia/Ferramenta |
| :--- | :--- | :--- | :--- |
| Repositório de código | SCM | Onde o código-fonte está versionado. | GitHub |
| Pipeline | Orquestrador CI | Compila e executa testes unitários. | Azure DevOps Pipelines |
| Pipeline | Orquestrador CD | Empacota em Docker e faz o deploy no Azure Web App. | Azure DevOps Pipelines |
| Banco de Dados | PaaS em Nuvem | Armazenamento persistente. | Azure Database for PostgreSQL |
| Imagem de Deploy | Container | Empacotamento da aplicação. | Docker |
| Ambiente de Execução | PaaS | Hospedagem do contêiner Docker. | Azure Web App for Containers |

## 4. Banco de Dados em Nuvem (Requisito 4)

*   **Tecnologia Aceita:** PostgreSQL
*   **Serviço em Nuvem:** Azure Database for PostgreSQL - Flexible Server (PaaS)
*   **Configuração:** As credenciais de conexão são injetadas no ambiente de execução do Azure Web App via variáveis de ambiente protegidas (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`), conforme configurado no arquivo `src/main/resources/application-prod.properties`.

## 5. Configuração do Projeto no Azure DevOps (Requisito 5)

*   **Project Name:** `Sprint 4 – Azure DevOps`
*   **Description:** (Preencher com as informações do grupo e professor)
*   **Visibility:** `Private`
*   **Version control:** `Git`
*   **Work item process:** `Scrum`

## 6. Pipelines CI/CD (Requisito 7)

O pipeline está configurado via **YAML** no arquivo `azure-pipelines.yml` na raiz do projeto.

### CI (Build + Testes Automáticos)

*   **Ação:** `mvn clean package` (compilação e execução de testes unitários).
*   **Containerização:** Build da imagem Docker (`Dockerfile`).
*   **Publicação do Artefato:** Push da imagem Docker para o Azure Container Registry (ACR).

### CD (Deploy Automático)

*   **Ação:** Deploy da imagem Docker do ACR para o **Azure Web App for Containers**.
*   **Variáveis de Ambiente:** As credenciais do banco de dados são passadas como variáveis de ambiente protegidas (Grupo de Variáveis `db-credentials`) para o Azure Web App.

## 7. Instruções para Execução e Demonstração

### Pré-requisitos no Azure

1.  **Azure Database for PostgreSQL:** Provisionar o serviço e obter as credenciais.
2.  **Azure Container Registry (ACR):** Provisionar o serviço.
3.  **Azure Web App for Containers:** Provisionar o serviço.

### Configuração no Azure DevOps

1.  **Criar Projeto:** Conforme Requisito 5.
2.  **Conexões de Serviço:** Criar conexões para o Azure Subscription e para o ACR.
3.  **Grupo de Variáveis:** Criar o Grupo de Variáveis `db-credentials` com as credenciais do PostgreSQL (marcar como seguras).
4.  **Configurar Pipeline:** Criar um novo pipeline no Azure DevOps, apontando para o arquivo `azure-pipelines.yml` no repositório GitHub. **Substituir os placeholders** (`<NOME_DA_SUA_SERVICE_CONNECTION_AZURE>`, etc.) no arquivo YAML com os nomes reais dos seus recursos.

### Demonstração (Requisito 8)

A demonstração em vídeo deve seguir o fluxo:

1.  Mostrar as ferramentas usadas (IDE, Azure DevOps, Banco de Dados, Portal do Azure).
2.  Apresentar a configuração do pipeline (`azure-pipelines.yml`).
3.  **Alterar o `README.md`** (ou outro arquivo) e fazer `push` para o GitHub.
4.  Mostrar o início automático do pipeline após o `push`.
5.  Explicar cada etapa do CI/CD durante a execução.
6.  Mostrar o artefato criado (a imagem no ACR) e os resultados dos testes.
7.  Demonstrar no Portal do Azure os recursos atualizados pelo deploy (Web App).
8.  Acessar a aplicação no Web App e executar o **CRUD completo** (inserir, consultar, atualizar, deletar), mostrando no banco de dados cada operação.

## Endpoints da API (CRUD)

A aplicação está acessível em `https://<NOME_DO_SEU_WEB_APP>.azurewebsites.net/mottu`.

| Recurso | Método | Descrição |
| :--- | :--- | :--- |
| `/mottu/filiais` | `GET` | Listar todas as filiais. |
| `/mottu/filiais` | `POST` | Criar nova filial. |
| `/mottu/filiais/{id}` | `GET` | Obter filial por ID. |
| `/mottu/filiais/{id}` | `PUT` | Atualizar filial. |
| `/mottu/filiais/{id}` | `DELETE` | Excluir filial. |
| `/mottu/motos` | `GET` | Listar todas as motos. |
| `/mottu/motos` | `POST` | Criar nova moto. |
| `/mottu/motos/{id}` | `GET` | Obter moto por ID. |
| `/mottu/motos/{id}` | `PUT` | Atualizar moto. |
| `/mottu/motos/{id}` | `DELETE` | Excluir moto. |
| `/mottu/registros` | `GET` | Listar todos os registros RFID. |
| `/mottu/registros` | `POST` | Criar novo registro RFID. |
| `/mottu/registros/{id}` | `GET` | Obter registro RFID por ID. |
| `/mottu/registros/{id}` | `PUT` | Atualizar registro RFID. |
| `/mottu/registros/{id}` | `DELETE` | Excluir registro RFID. |
