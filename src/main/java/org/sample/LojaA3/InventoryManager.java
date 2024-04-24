package org.sample.LojaA3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.mockito.Mockito.*;

public class InventoryManager {

    private final Map<Produto, Integer> estoque;

    public InventoryManager() {
        this.estoque = new HashMap<>();
    }

    public void adicionarProduto(Produto produto, int quantidade) {
        if (produto == null || quantidade <= 0) {
            throw new IllegalArgumentException("Produto inválido ou quantidade não positiva");
        }

        estoque.put(produto, estoque.getOrDefault(produto, 0) + quantidade);
    }

    public void removerProduto(Produto produto, int quantidade) {
        if (produto == null || quantidade <= 0) {
            throw new IllegalArgumentException("Produto inválido ou quantidade não positiva");
        }

        if (estoque.containsKey(produto)) {
            int estoqueAtual = estoque.get(produto);
            if (estoqueAtual < quantidade) {
                throw new IllegalArgumentException("Quantidade excede o estoque disponível");
            }

            estoque.put(produto, estoqueAtual - quantidade);
        }
    }

    public int verificarEstoque(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto inválido");
        }

        return estoque.getOrDefault(produto, 0);
    }
}