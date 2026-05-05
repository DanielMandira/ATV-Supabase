-- Tabela de Produtos
CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR NOT NULL,
    description TEXT,
    sku VARCHAR UNIQUE NOT NULL,
    category VARCHAR,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);

-- Tabela de Itens de Estoque
CREATE TABLE stock_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID REFERENCES products(id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL DEFAULT 0,
    unit_price DECIMAL(10, 2) NOT NULL,
    location VARCHAR,
    updated_at TIMESTAMP DEFAULT now()
);

-- View para o endpoint /stock/summary (agregação SQL)
CREATE VIEW stock_summary AS
SELECT
    p.id AS product_id,
    p.name AS product_name,
    COALESCE(SUM(s.quantity), 0) AS total_quantity
FROM
    products p
LEFT JOIN
    stock_items s ON p.id = s.product_id
GROUP BY
    p.id, p.name;

-- ==========================================
-- DADOS DE EXEMPLO (MOCK DATA)
-- ==========================================

-- Inserindo Produtos de Exemplo
INSERT INTO products (id, name, description, sku, category) VALUES
    ('11111111-1111-1111-1111-111111111111', 'Caneta Azul', 'Caneta esferográfica azul', 'CAN-AZ-01', 'Papelaria'),
    ('22222222-2222-2222-2222-222222222222', 'Caderno A4', 'Caderno universitário 200 folhas', 'CAD-A4-200', 'Papelaria'),
    ('33333333-3333-3333-3333-333333333333', 'Mochila Notebook', 'Mochila impermeável preta', 'MOC-NT-PR', 'Acessórios')
ON CONFLICT DO NOTHING;

-- Inserindo Itens de Estoque de Exemplo
INSERT INTO stock_items (product_id, quantity, unit_price, location) VALUES
    ('11111111-1111-1111-1111-111111111111', 200, 1.50, 'Corredor A - Prateleira 1'),
    ('11111111-1111-1111-1111-111111111111', 150, 1.50, 'Corredor A - Prateleira 2'),
    ('22222222-2222-2222-2222-222222222222', 120, 15.90, 'Corredor B - Prateleira 1'),
    ('33333333-3333-3333-3333-333333333333', 30, 120.00, 'Corredor C - Prateleira 3');
