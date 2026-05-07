# 📦 Estocadão API - Controle de Estoque

Bem-vindo à API do **Estocadão**! Este é um servidor REST construído com **Kotlin Multiplatform (Ktor)** e banco de dados **Supabase (PostgreSQL)** para gerenciamento completo de produtos e estoques.

---

## 🚀 Como Executar o Projeto Localmente

### 1. Requisitos
- **Java JDK 17+** instalado
- **IntelliJ IDEA** ou **Android Studio**
- Conta no **Supabase** (para ter as credenciais do banco)
- **Insomnia** (ou Postman) para testar as rotas

### 2. Configurar Variáveis de Ambiente
A API precisa se conectar ao Supabase. Para isso, você deve criar e configurar as variáveis de ambiente.

No diretório raiz, há um arquivo chamado `.env.example`. Você pode criar um arquivo `.env` com base nele ou configurar diretamente na sua IDE/Terminal:

**Variáveis Necessárias:**
- `SUPABASE_URL`: A URL do seu projeto (ex: `https://seu-id.supabase.co`)
- `SUPABASE_KEY`: Sua chave de API anon (ou service_role se for ignorar RLS)

> ⚠️ **IMPORTANTE:** Nunca adicione sua chave real no repositório! Use o `.env` apenas localmente.

### 3. Rodar o Banco de Dados (Supabase)
Vá até o painel do seu Supabase, abra o **SQL Editor** e cole as queries do arquivo `supabase_schema.sql` (disponível na raiz do projeto) para criar as tabelas `products`, `stock_items` e a view `stock_summary`.

### 4. Iniciar o Servidor

#### Usando IntelliJ/Android Studio (Com Variáveis de Ambiente):
1. No topo da tela, ao lado do botão verde de "Play" (Run), clique na caixa de seleção que diz `Current File` ou `ApplicationKt` e vá em **Edit Configurations...**.
2. Na janela que abrir, se não houver uma configuração para o servidor, clique no `+` (canto superior esquerdo) e escolha **Kotlin**.
   - Em **Main class**, coloque: `com.fatec.crud_estoque.ApplicationKt`
   - Em **Use classpath of module**, selecione `server.main` (ou o módulo correspondente).
3. Procure o campo chamado **Environment variables** (Variáveis de ambiente).
4. Clique no ícone de pasta/lista no canto direito desse campo e adicione as variáveis clicando no `+`:
   - Nome: `SUPABASE_URL` | Valor: `https://sua-url.supabase.co`
   - Nome: `SUPABASE_KEY` | Valor: `sua-chave`
5. Clique em **Apply** (Aplicar) e depois **OK**.
6. Agora, basta clicar no botão de Play verde na barra superior!
#### Usando o Terminal:
```powershell
# Windows (PowerShell)
$env:SUPABASE_URL="https://sua-url.supabase.co"
$env:SUPABASE_KEY="sua-chave"
./gradlew :server:run
```

O servidor iniciará em: **`http://127.0.0.1:8080`**

---

## 🛠️ Testando no Insomnia (Rotas e Exemplos)

Aqui estão as rotas e os exemplos exatos (JSON) que você pode copiar e colar no **Insomnia** para testar. A API roda em `http://127.0.0.1:8080`.

### 🟢 1. PRODUTOS (`/products`)

#### Listar Produtos
- **Método:** `GET`
- **URL:** `http://127.0.0.1:8080/products`

#### Cadastrar Produto
- **Método:** `POST`
- **URL:** `http://127.0.0.1:8080/products`
- **Body (JSON):**
```json
{
  "name": "Notebook Dell Inspiron",
  "description": "Notebook com processador i7 e 16GB RAM",
  "sku": "DELL-INSP-001",
  "category": "Eletrônicos"
}
```

#### Buscar Produto por ID
- **Método:** `GET`
- **URL:** `http://127.0.0.1:8080/products/{ID_DO_PRODUTO}` *(Pegue o ID listando os produtos ou criando um novo)*

#### Atualizar Produto
- **Método:** `PUT`
- **URL:** `http://127.0.0.1:8080/products/{ID_DO_PRODUTO}`
- **Body (JSON):**
```json
{
  "name": "Notebook Dell Inspiron 15",
  "description": "Notebook i7, 16GB RAM, 512GB SSD",
  "sku": "DELL-INSP-001",
  "category": "Informática"
}
```

#### Deletar Produto
- **Método:** `DELETE`
- **URL:** `http://127.0.0.1:8080/products/{ID_DO_PRODUTO}`

---

### 🔵 2. ESTOQUE (`/stock`)

#### Adicionar Item ao Estoque
- **Método:** `POST`
- **URL:** `http://127.0.0.1:8080/stock`
- **Body (JSON):** *(Atenção: Substitua `product_id` pelo ID real de um produto criado)*
```json
{
  "product_id": "COLE_O_UUID_DO_PRODUTO_AQUI",
  "quantity": 50,
  "unit_price": 4500.00,
  "location": "Armazém Principal - Corredor B"
}
```

#### Listar Itens de Estoque
- **Método:** `GET`
- **URL:** `http://127.0.0.1:8080/stock`

#### Buscar Item de Estoque por ID
- **Método:** `GET`
- **URL:** `http://127.0.0.1:8080/stock/{ID_DO_ESTOQUE}`

#### Atualizar Item de Estoque
- **Método:** `PUT`
- **URL:** `http://127.0.0.1:8080/stock/{ID_DO_ESTOQUE}`
- **Body (JSON):**
```json
{
  "product_id": "COLE_O_UUID_DO_PRODUTO_AQUI",
  "quantity": 40,
  "unit_price": 4350.00,
  "location": "Armazém Secundário"
}
```

#### Deletar Item de Estoque
- **Método:** `DELETE`
- **URL:** `http://127.0.0.1:8080/stock/{ID_DO_ESTOQUE}`

---

### 🟠 3. RESUMO DO ESTOQUE (Agregação)

#### Ver Total de Estoque por Produto
- **Método:** `GET`
- **URL:** `http://127.0.0.1:8080/stock/summary`
- **Retorno Esperado:**
```json
[
  {
    "product_id": "uuid-do-produto",
    "product_name": "Notebook Dell Inspiron",
    "total_quantity": 90
  }
]
```
*(Nota: Este endpoint calcula a quantidade total agrupada de forma otimizada via Banco de Dados SQL!)*

---
## 💻 Tecnologias e Critérios Atendidos
- **Kotlin Multiplatform (KMP) + Ktor** - Arquitetura de servidor
- **Supabase (PostgreSQL)** - Hospedagem e gerência do Banco de Dados
- **Integridade Referencial** - Cascade Delete no banco garante que apagar um Produto deleta seu Estoque
- **kotlinx.serialization** - Conversão de/para JSON nativa e rápida