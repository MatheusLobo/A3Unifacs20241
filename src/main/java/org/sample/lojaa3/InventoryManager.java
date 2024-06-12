package org.sample.lojaa3;

import java.util.Map;

public class InventoryManager {

    private final Map<Integer, StockItem> estoque;
    private final PrecoService precoService;

    public InventoryManager(Map<Integer, StockItem> estoque, PrecoService precoService) {
        this.estoque = estoque;
        this.precoService = precoService;
    }

    public static final String SKU_INVALID_MESSAGE = "SKU inválido: ";

    public StockItem adicionarProduto(int sku, String nome, int quantidade, Double valor) {
        try {
            validarSku(sku);
            validarNome(nome);
            validarQuantidade(quantidade);
            double precoFinal = calcularPrecoFinal(sku, valor);

            StockItem stockItem = new StockItem(sku, nome, quantidade, precoFinal);
            estoque.put(stockItem.getSku(), stockItem);
            return stockItem;
        } catch (StockException | QuantityInvalidException | SkuInvalidException | PriceInvalidException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private void validarSku(int sku) {
        if (sku <= 0) {
            throw new SkuInvalidException(SKU_INVALID_MESSAGE + sku);
        }
    }

    private void validarNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            throw new StockException("Nome inválido: " + nome);
        }
    }

    private void validarQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new QuantityInvalidException("Quantidade inválida: " + quantidade);
        }
    }

    private double calcularPrecoFinal(int sku, Double valor) {
        if (valor != null && valor > 0) {
            return valor;
        } else {
            double precoFinal = precoService != null ? precoService.getPreco(sku) : 0.0;
            if (precoFinal <= 0) {
                throw new PriceInvalidException("Preço inválido: " + precoFinal);
            }
            return precoFinal;
        }
    }

    public double calcularValorTotalEstoque() {
        return estoque.values().stream().mapToDouble(StockItem::getValor).sum();
    }

    public void removerProduto(int sku, int quantidade) {
        validarSku(sku);
        validarQuantidade(quantidade);

        StockItem stockItem = estoque.get(sku);
        if (stockItem == null) {
            throw new StockUnavailableException("Produto não encontrado no estoque: SKU " + sku);
        }

        int estoqueAtual = stockItem.getQuantidade();
        if (quantidade > estoqueAtual) {
            throw new StockUnavailableException("Estoque indisponível para remover " + quantidade + " unidades do produto: SKU " + sku);
        }

        stockItem.setQuantidade(estoqueAtual - quantidade);
    }

    public StockItem verificarEstoque(int sku) {
        validarSku(sku);
        return estoque.get(sku);
    }

    interface PrecoService {
        double getPreco(int sku);
    }
}
