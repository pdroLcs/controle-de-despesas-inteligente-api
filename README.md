# Controle de Despesas Inteligente API

API REST para controle de despesas pessoais com recursos de Inteligência Artificial via OpenAI. O projeto permite registrar, consultar e classificar transações financeiras, além de processar comandos por áudio em português brasileiro para transformar fala em lançamentos de despesas.

## Sumário

- [Sobre o projeto](#sobre-o-projeto)
- [Principais recursos](#principais-recursos)
- [Arquitetura e tecnologias](#arquitetura-e-tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Configuração do ambiente](#configuração-do-ambiente)
- [Como executar](#como-executar)
- [Endpoints](#endpoints)
- [Categorias disponíveis](#categorias-disponíveis)
- [Exemplos de uso](#exemplos-de-uso)
- [Testes](#testes)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Observações de segurança](#observações-de-segurança)

## Sobre o projeto

O **Controle de Despesas Inteligente API** é uma aplicação Spring Boot voltada para gerenciamento de gastos. Além das operações tradicionais de cadastro e consulta, a API utiliza recursos da OpenAI por meio do Spring AI para:

- transcrever áudios com descrição de despesas;
- interpretar a intenção do usuário em linguagem natural;
- acionar ferramentas internas para persistir ou consultar transações;
- responder em áudio usando síntese de fala.

O banco de dados utilizado é MySQL e pode ser iniciado localmente com Docker Compose.

## Principais recursos

- Cadastro de transações financeiras com descrição, categoria e valor.
- Consulta de transações por categoria.
- Integração com OpenAI Chat para interpretação de mensagens em linguagem natural.
- Integração com transcrição de áudio para comandos em português brasileiro.
- Integração com text-to-speech para retorno em áudio MP3.
- Banco MySQL provisionado via Docker Compose.
- Persistência com Spring Data JPA.
- Tool calling com Spring AI para conectar o modelo de IA aos casos de uso da aplicação.

## Arquitetura e tecnologias

| Camada/Área | Tecnologia |
| --- | --- |
| Linguagem | Java 21 |
| Framework | Spring Boot 3.5.0 |
| IA | Spring AI 1.0.0 + OpenAI |
| Persistência | Spring Data JPA |
| Banco de dados | MySQL |
| Containerização | Docker Compose |
| Build | Maven Wrapper |
| Utilitários | Lombok |

## Pré-requisitos

Antes de executar a aplicação, tenha instalado:

- Java 21 ou superior;
- Docker e Docker Compose;
- uma chave de API da OpenAI;
- terminal compatível com execução do Maven Wrapper (`./mvnw`).

## Configuração do ambiente

A aplicação lê a chave da OpenAI pela variável de ambiente `OPENAI_API_KEY`.

```bash
export OPENAI_API_KEY="sua-chave-da-openai"
```

As principais configurações da aplicação estão em `src/main/resources/application.properties`:

```properties
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-4o-mini
spring.ai.openai.audio.transcription.options.model=whisper-1
spring.ai.openai.audio.transcription.options.language=pt
```

O MySQL é definido em `compose.yml` com as seguintes credenciais locais:

| Item | Valor |
| --- | --- |
| Database | `transaction` |
| Usuário | `app` |
| Senha | `app` |
| Porta local | `3307` |
| Porta do container | `3306` |
| Volume | `transaction_data` |

> **Importante:** a propriedade `spring.jpa.hibernate.ddl-auto=create` recria o schema ao iniciar a aplicação. Para ambientes com dados importantes, altere essa configuração antes de usar em produção.

## Como executar

### 1. Subir o banco de dados

```bash
docker compose up -d
```

### 2. Executar a aplicação

```bash
./mvnw spring-boot:run
```

Por padrão, a API ficará disponível em:

```text
http://localhost:8080
```

> O projeto também inclui `spring-boot-docker-compose`, permitindo integração do Spring Boot com o arquivo `compose.yml` durante a execução local.

## Endpoints

### Transações

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `POST` | `/transactions` | Cria uma transação manualmente. |
| `GET` | `/transactions/{category}` | Lista transações de uma categoria. |
| `POST` | `/transactions/ai` | Recebe áudio, transcreve com OpenAI, interpreta o pedido, usa ferramentas internas e retorna áudio MP3. |

### Endpoints auxiliares de IA

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `GET` | `/api/chat?prompt=...` | Envia um prompt para o `ChatClient`. |
| `GET` | `/api/chat-model?prompt=...` | Envia um prompt diretamente para o modelo de chat. |
| `POST` | `/api/transcribe` | Transcreve um arquivo de áudio enviado como multipart. |
| `POST` | `/api/sinthesize` | Gera áudio MP3 a partir de texto. |

> Observação: o endpoint `/api/sinthesize` segue o nome implementado atualmente no controller.

## Categorias disponíveis

As categorias aceitas pela API são:

- `GROCERIES` — mercado, alimentação e compras essenciais;
- `PHARMA` — farmácia e saúde;
- `AUTO` — carro, combustível e manutenção;
- `LEISURE` — lazer, entretenimento e passeios;
- `TRIP` — viagens e deslocamentos.

## Exemplos de uso

### Criar uma transação manualmente

```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Compra no mercado",
    "category": "GROCERIES",
    "amount": 153.75
  }'
```

Resposta esperada:

```json
{
  "id": "3f2b89f0-9e0b-4c3a-8a4f-5c6a8e5f7c1d",
  "description": "Compra no mercado",
  "category": "GROCERIES",
  "amount": 153.75
}
```

### Listar transações por categoria

```bash
curl http://localhost:8080/transactions/GROCERIES
```

### Enviar um áudio para processamento inteligente

```bash
curl -X POST http://localhost:8080/transactions/ai \
  -F "file=@./audio.mp3" \
  --output resposta.mp3
```

Fluxo executado por esse endpoint:

1. recebe um arquivo de áudio via multipart;
2. transcreve o áudio com OpenAI;
3. envia a transcrição ao chat configurado com prompt de sistema;
4. o modelo decide se deve registrar ou consultar transações usando as ferramentas da aplicação;
5. retorna uma resposta em áudio MP3.

### Transcrever áudio diretamente

```bash
curl -X POST http://localhost:8080/api/transcribe \
  -F "file=@./audio.mp3"
```

### Gerar áudio a partir de texto

```bash
curl -X POST http://localhost:8080/api/sinthesize \
  -H "Content-Type: application/json" \
  -d '{"text":"Sua despesa foi registrada com sucesso."}' \
  --output audio.mp3
```

### Testar chat diretamente

```bash
curl "http://localhost:8080/api/chat?prompt=Liste%20minhas%20despesas%20de%20mercado"
```

## Testes

Para executar a suíte de testes:

```bash
./mvnw test
```

Alguns testes de integração com OpenAI são executados somente quando a variável `OPENAI_API_KEY` está configurada.

```bash
export OPENAI_API_KEY="sua-chave-da-openai"
./mvnw test
```

## Estrutura do projeto

```text
src/main/java/dev/pdrolcs/budgeting
├── application                 # Casos de uso da aplicação
├── config                      # Configurações de beans
├── controllers                 # Controllers auxiliares de IA
├── domains                     # Entidades e objetos de domínio
├── infrastucture/http          # API HTTP de transações
├── infrastucture/persistence   # Entidades e repositórios JPA
└── repository                  # Contratos de repositório
```

## Observações de segurança

- Nunca versione sua chave `OPENAI_API_KEY`.
- Use variáveis de ambiente ou secret managers em ambientes compartilhados.
- Revise `spring.jpa.hibernate.ddl-auto=create` antes de qualquer uso fora de desenvolvimento.
- Restrinja logs detalhados de IA em produção, pois eles podem conter dados sensíveis de prompts ou respostas.
- Valide e sanitize arquivos enviados por upload antes de expor a API publicamente.

## Licença

Este projeto é somente para fins educacionais e para portfólio.