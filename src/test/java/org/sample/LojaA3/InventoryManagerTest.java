package org.sample.LojaA3;

import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.sample.LojaA3.InventoryManager.PrecoService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class) //necessario para rodar os mocks
public class InventoryManagerTest {

    @Mock
    private Map<Integer, Storage> estoqueMock; //mock para o mapa de estoque
    @Mock
    private PrecoService precoServiceMock; //mock para o serviço de preço

    @InjectMocks
    private InventoryManager inventoryManager; //instância do gerenciador de estoque
    private Storage storage; //objeto de armazenamento simulado

    @org.junit.Test 
    public void adicionarProdutoSucesso() { //teste positivo
        int sku = 3232;
        String nome = "Camiseta";
        int quantidade = 100;
        double valor = 50.00;

        Storage storage = inventoryManager.adicionarProduto(sku, nome, quantidade, valor); //adiciona um produto

        verify(estoqueMock).put(sku, storage); //verifica se o produto foi adicionado ao estoque
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void adicionarProdutoStorageNull() { //teste negativo com exceção
        inventoryManager.adicionarProduto(0, null, 0, 0); //tentativa de adicionar produto com parametros invalidos
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void adicionarProdutoQuantidadeInvalida() { //teste negativo com exceção
        int sku = 3232;
        String nome = "Camiseta";
        int quantidade = -10; //quantidade negativa deve causar excecao
        double valor = 50.00;

        inventoryManager.adicionarProduto(sku, nome, quantidade, valor); //tentativa de adicionar produto com quantidade invalida
    }

    @org.junit.Test
    public void removerProdutoSucesso() { //teste positivo
        int sku = 3232;
        String nome = "Camiseta";
        int quantidadeInicial = 10;
        int quantidadeRemover = 5;
        double valor = 50.00;

        inventoryManager.adicionarProduto(sku, nome, quantidadeInicial, valor); //adiciona produto
        inventoryManager.removerProduto(sku, quantidadeRemover); //remove uma quantidade do produto

        verify(estoqueMock).put(sku, storage); //verifica se o produto foi atualizado no estoque
        assertEquals(quantidadeInicial - quantidadeRemover, storage.getQuantidade()); //verifica se a quantidade foi atualizada corretamente
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void removerProdutoSkuInvalido() { //teste negativo com exceção
        inventoryManager.removerProduto(0, 5); //tentativa de remover produto com SKU inválido
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void removerProdutoQuantidadeInvalida() { //teste negativo com exceção
        inventoryManager.removerProduto(123, -5); //tentativa de remover produto com quantidade inválida
    }

    @org.junit.Test
    public void verificarEstoqueSucesso() { //teste positivo
        int sku = 123;
        String nome = "Camiseta";
        int quantidade = 10;
        double valor = 50.00;

        inventoryManager.adicionarProduto(sku, nome, quantidade, valor); //adiciona produto ao estoque

        Storage storageEstoque = inventoryManager.verificarEstoque(sku); //verifica o estoque para um sku existente

        assertEquals(storageEstoque.getSku(), sku); //verifica se o sku corresponde
        assertEquals(storageEstoque.getNome(), nome); //verifica se o nome corresponde
        assertEquals(storageEstoque.getQuantidade(), quantidade); //verifica se a quantidade corresponde
        assertEquals(storageEstoque.getValor(), valor, 0.01); //verifica se o valor corresponde
    }

    @org.junit.Test
    public void verificarEstoqueSkuInvalido() { //teste negativo
        inventoryManager.verificarEstoque(0); //verifica o estoque para um sku inválido

        verify(estoqueMock, never()).get(anyInt()); //verifica se a operação get nunca foi chamada no mock
    }

    @org.junit.Test
    public void verificarEstoqueSkuNaoExiste() { //teste negativo
        inventoryManager.verificarEstoque(123); //verifica o estoque para um sku que nao existe

        verify(estoqueMock).get(123); //verifica se a operação get foi chamada com o slu especificado
        assertNull(inventoryManager.verificarEstoque(123)); //verifica se o resultado é nulo
    }

    @org.junit.Test
    public void adicionarProdutoSucessoComPrecoExterno() { //teste positivo com preco externo
        int sku = 3232;
        String nome = "Camiseta";
        int quantidade = 10;
        double precoExterno = 45.00;

        when(precoServiceMock.getPreco(sku)).thenReturn(precoExterno); //define o comportamento do serviço de preco

        Storage storage = inventoryManager.adicionarProduto(sku, nome, quantidade, precoExterno); //adiciona produto com preco externo

        verify(precoServiceMock).getPreco(sku); //verifica se o serviço de preco foi chamado com o sku especificado
        assertEquals(precoExterno, storage.getValor(), 0.01); //verifica se o valor do produto corresponde ao preco externo
        verify(estoqueMock).put(sku, storage); //verifica se o produto foi adicionado ao estoque
    }
}
