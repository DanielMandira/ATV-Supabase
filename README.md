# Estocadão API - Controle de Estoque (Ktor + Supabase)

Este repositório contém a implementação do servidor REST em Kotlin Multiplatform (utilizando Ktor) para o sistema de controle de estoque da empresa Estocadão. A persistência de dados é feita via Supabase (PostgreSQL).

## Requisitos do Projeto Atendidos
- **Kotlin Multiplatform & Ktor**: Configurado e estruturado no módulo `server`.
- **Supabase**: Utilização do cliente HTTP do Supabase (`supabase-kt`) para operações no banco.
- **Modelos**: Serialização e desserialização configuradas com `kotlinx.serialization` no módulo `shared`.
- **CRUD Produtos**: Implementados endpoints `GET`, `POST`, `PUT` e `DELETE` em `/products`.
- **CRUD Estoque**: Implementados endpoints `GET`, `POST`, `PUT` e `DELETE` em `/stock`.
- **Endpoint Especial**: Implementado `/stock/summary` fazendo uso de View SQL para garantir que a agregação (`GROUP BY + SUM`) seja processada nativamente no banco de dados.

## Estrutura do Banco de Dados (Supabase)

O esquema SQL necessário para rodar o projeto está localizado na raiz do repositório no arquivo `supabase_schema.sql`.

Execute as queries presentes no arquivo através do **SQL Editor** do Supabase para criar:
1. Tabela `products`
2. Tabela `stock_items` (com chave estrangeira para `products`)
3. View `stock_summary` (realiza agregação via `GROUP BY` e `SUM`)

> **Atenção:** Se as políticas de RLS (Row Level Security) estiverem ativadas para essas tabelas, lembre-se de configurar policies que permitam operações (SELECT, INSERT, UPDATE, DELETE) para as requisições da sua API, ou opte por desativá-las temporariamente durante o desenvolvimento local.

## Configuração do Ambiente Local

Para executar o servidor Ktor localmente, é obrigatório definir variáveis de ambiente contendo as credenciais de conexão da API do Supabase.

**Variáveis de Ambiente Necessárias:**
- `SUPABASE_URL`: A URL do projeto Supabase (ex: `https://xxx.supabase.co`).
- `SUPABASE_KEY`: A chave da API (geralmente `anon_key` para desenvolvimento com as devidas políticas ou `service_role_key` se você precisar ignorar o RLS através do servidor. Nunca exponha a `service_role` publicamente!).

### Como Executar

#### Via IntelliJ IDEA ou Android Studio
1. Acesse as **Run Configurations** (Edit Configurations).
2. Selecione a execução da classe `ApplicationKt` do módulo `server`.
3. No campo `Environment variables`, adicione: 
   `SUPABASE_URL=sua_url;SUPABASE_KEY=sua_chave`
4. Execute o projeto.

#### Via Terminal / Gradle

**No Windows (PowerShell):**
```powershell
$env:SUPABASE_URL="https://sua-url.supabase.co"
$env:SUPABASE_KEY="sua-chave"
./gradlew :server:run
```

**No macOS / Linux:**
```bash
export SUPABASE_URL="https://sua-url.supabase.co"
export SUPABASE_KEY="sua-chave"
./gradlew :server:run
```

O servidor Ktor será inicializado por padrão na porta **8080** (a porta está definida no `shared` module: `Constants.kt`).

## API Endpoints

### Produtos
- `GET /products` - Lista todos os produtos cadastrados.
- `GET /products/{id}` - Busca os detalhes de um produto através do seu UUID.
- `POST /products` - Cria um novo produto (envie JSON sem o `id` e datas).
- `PUT /products/{id}` - Atualiza todos os dados de um produto.
- `DELETE /products/{id}` - Remove um produto. Devido ao `ON DELETE CASCADE`, todos os itens de estoque vinculados também serão removidos garantindo integridade relacional.

### Estoque
- `GET /stock` - Lista todos os itens de estoque.
- `GET /stock/{id}` - Busca os detalhes de um item de estoque específico.
- `POST /stock` - Adiciona um novo item vinculando a um produto.
- `PUT /stock/{id}` - Atualiza a quantidade, preço e localização do item.
- `DELETE /stock/{id}` - Remove o item de estoque.

### Endpoint Especial: Resumo do Estoque
- `GET /stock/summary` - Consulta a View `stock_summary` via API do Supabase, retornando o agrupamento de `product_id`, `product_name` e o total de `total_quantity` por produto.