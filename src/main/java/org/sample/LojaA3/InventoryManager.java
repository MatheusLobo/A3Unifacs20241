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

    public Storage adicionarProduto(int sku, String nome, int quantidade, double valor) {
        try {
            if (sku < 0) {
                throw new SkuInvalidoException("SKU inválido: " + sku);
            }
            if (nome == null || nome.isEmpty()) {
                throw new EstoqueException("Nome inválido: " + nome);
            }
            if (quantidade <= 0) {
                throw new QuantidadeInvalidaException("Quantidade inválida: " + quantidade);
            }

            if (valor <= 0) {
                if (precoService == null) {
                    throw new PrecoInvalidoException("Serviço de preço não configurado");
                }
                valor = precoService.getPreco(sku);
            }

            Storage storage = new Storage(sku, nome, quantidade, valor);
            estoque.put(storage.getSku(), storage);
            return storage;
        } catch (EstoqueException | QuantidadeInvalidaException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    
    public void removerProduto(int sku, int quantidade) {
        try {
            if (sku <= 0 || quantidade <= 0) {
                if (quantidade < 0) {
                    throw new QuantidadeInvalidaException("Quantidade invalida: " + quantidade);
                } else {
                    throw new SkuInvalidoException("SKU invalido: " + sku);
                }
            }

            // Chamada do metodo get para obter o objeto Storage do mapa
            Storage storage = estoque.get(sku);

            if (storage != null) { // Verifica se o objeto Storage foi encontrado no mapa
                int quantidadeAtual = storage.getQuantidade();

                if (quantidadeAtual < quantidade) {
                    throw new QuantidadeInvalidaException("Quantidade excede o estoque disponivel");
                }

                storage.setQuantidade(quantidadeAtual - quantidade);

                if (storage.getQuantidade() == 0) {
                    estoque.remove(sku);
                }
            } else {
                throw new EstoqueException("Produto nao encontrado no estoque");
            }
        } catch (EstoqueException e) {
            System.err.println(e.getMessage());
        }
    }

    public Storage verificarEstoque(int sku) throws SkuInvalidoException {
    	  if (sku <= 0) {
    	      throw new SkuInvalidoException("SKU invalido: " + sku);
    	  }
    	  return estoque.get(sku); 
    	}

    interface PrecoService {
        double getPreco(int sku);
    }
}
