package org.sample.LojaA3;

import org.sample.LojaA3.EstoqueException;
import java.util.Map;

public class InventoryManager {

    private final Map<Integer, Storage> estoque;
    private final PrecoService precoService;

    public InventoryManager(Map<Integer, Storage> estoque, PrecoService precoService) {
        this.estoque = estoque;
        this.precoService = precoService;
    }

    public Storage adicionarProduto(int sku, String nome, int quantidade, Double valor) {
        try {
            if (sku <= 0) {
                throw new SkuInvalidoException("SKU inválido: " + sku);
            }
            if (nome == null || nome.isEmpty()) {
                throw new EstoqueException("Nome inválido: " + nome);
            }
            if (quantidade <= 0) {
                throw new QuantidadeInvalidaException("Quantidade inválida: " + quantidade);
            }

            double precoFinal;
            if (valor != null && valor > 0) {
                precoFinal = valor; 
            } else {
                precoFinal = precoService != null ? precoService.getPreco(sku) : 0.0; 
                }

            if (precoFinal <= 0) {
                throw new PrecoInvalidoException("Preço inválido: " + precoFinal);
            }

            Storage storage = new Storage(sku, nome, quantidade, precoFinal);
            estoque.put(storage.getSku(), storage);
            return storage;
        } catch (EstoqueException | QuantidadeInvalidaException | SkuInvalidoException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    
    public void removerProduto(int sku, int quantidade) throws EstoqueException {
        if (sku <= 0) {
            throw new SkuInvalidoException("SKU inválido: " + sku);
        }
        if (quantidade <= 0) {
            throw new QuantidadeInvalidaException("Quantidade inválida: " + quantidade);
        }

        Storage storage = estoque.get(sku);
        if (storage == null) {
            throw new EstoqueIndisponivelException("Produto não encontrado no estoque: SKU " + sku);
        }

        int estoqueAtual = storage.getQuantidade();
        if (quantidade > estoqueAtual) {
            throw new EstoqueIndisponivelException("Estoque indisponível para remover " + quantidade + " unidades do produto: SKU " + sku);
        }

        storage.setQuantidade(estoqueAtual - quantidade);
    }

    public Storage verificarEstoque(int sku) throws SkuInvalidoException {
        if (sku <= 0) {
            throw new SkuInvalidoException("SKU inválido: " + sku);
        }
        return estoque.get(sku);
    }

    interface PrecoService {
        double getPreco(int sku);
    }
}
