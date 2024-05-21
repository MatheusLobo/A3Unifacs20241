package org.sample.LojaA3;

import org.sample.LojaA3.StockException;
import java.util.Map;

public class InventoryManager {

    private final Map<Integer, StockItem> estoque;
    private final PrecoService precoService;

    public InventoryManager(Map<Integer, StockItem> estoque, PrecoService precoService) {
        this.estoque = estoque;
        this.precoService = precoService;
    }

    public StockItem adicionarProduto(int sku, String nome, int quantidade, Double valor) {
        try {
            if (sku <= 0) {
                throw new SkuInvalidException("SKU inválido: " + sku);
            }
            if (nome == null || nome.isEmpty()) {
                throw new StockException("Nome inválido: " + nome);
            }
            if (quantidade <= 0) {
                throw new QuantityInvalidException("Quantidade inválida: " + quantidade);
            }

            double precoFinal;
            if (valor != null && valor > 0) {
                precoFinal = valor; 
            } else {
                precoFinal = precoService != null ? precoService.getPreco(sku) : 0.0; 
                }

            if (precoFinal <= 0) {
                throw new PriceInvalidException("Preço inválido: " + precoFinal);
            }

            StockItem stockItem = new StockItem(sku, nome, quantidade, precoFinal);
            estoque.put(stockItem.getSku(), stockItem);
            return stockItem;
        } catch (StockException | QuantityInvalidException | SkuInvalidException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    
    public void removerProduto(int sku, int quantidade) throws StockException {
        if (sku <= 0) {
            throw new SkuInvalidException("SKU inválido: " + sku);
        }
        if (quantidade <= 0) {
            throw new QuantityInvalidException("Quantidade inválida: " + quantidade);
        }

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

    public StockItem verificarEstoque(int sku) throws SkuInvalidException {
        if (sku <= 0) {
            throw new SkuInvalidException("SKU inválido: " + sku);
        }
        return estoque.get(sku);
    }

    interface PrecoService {
        double getPreco(int sku);
    }
}
