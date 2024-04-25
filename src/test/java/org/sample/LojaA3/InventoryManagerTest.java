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
    private PrecoService precoServiceMock; //mock para o servico de preco

    @InjectMocks
    private InventoryManager inventoryManager; //instancia do gerenciador de estoque
    private Storage storage; //objeto de armazenamento simulado

    @org.junit.Test 
    public void adicionarProdutoSucesso() { //teste positivo
        int sku = 3232;
        String nome = "Camiseta";
        int quantidade = 100;
        double valor = 50.00;

        // Armazena o objeto retornado pelo metodo
        Storage storage = inventoryManager.adicionarProduto(sku, nome, quantidade, valor); //adiciona um produto

        // Verifica se o mock foi chamado com o sku e o Storage correto
        verify(estoqueMock).put(sku, storage); 
    }
    
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void adicionarProdutoStorageNull() { //teste negativo com excecao
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

    public void removerProdutoSucesso() { //teste positivo
        // Mocks e dados de teste
        Map<Integer, Storage> estoqueMock = mock(Map.class);
        InventoryManager inventoryManager = new InventoryManager(estoqueMock, null);
        int sku = 3232;
        String nome = "Camisa";
        int quantidadeInicial = 10;
        int quantidadeRemover = 5;
        double valor = 50.00;

        // Configuracao do mock para retornar um obj Storage quando o metodo get for chamado com o sku 3232
        when(estoqueMock.get(sku)).thenReturn(new Storage(sku, nome, quantidadeInicial, valor));

        // Execução do metodo a ser testado
        inventoryManager.removerProduto(sku, quantidadeRemover); //remove uma quantidade do produto

        // Verificação se o metodo put foi chamado com o obj Storage correto
        verify(estoqueMock).put(eq(sku), any(Storage.class));

        // Verificacao se a quantidade foi atualizada corretamente
        assertEquals(quantidadeInicial - quantidadeRemover, estoqueMock.get(sku).getQuantidade());
    }


    @org.junit.Test(expected = IllegalArgumentException.class)
    public void removerProdutoSkuInvalido() { //teste negativo com excecao
        inventoryManager.removerProduto(0, 5); //tentativa de remover produto com SKU invalido
        
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void removerProdutoQuantidadeInvalida() { //teste negativo com excecao
        inventoryManager.removerProduto(123, -5); //tentativa de remover produto com quantidade invalida
    }
    
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void removerProdutoComValorInvalido() { //teste negativo com exceçao
        inventoryManager.removerProduto(50, -5); //tentativa de remover produto com valor invalida
    }

    @org.junit.Test
    public void verificarEstoqueSucesso() { //teste positivo
        int sku = 3232;
        String nome = "Camisa";
        int quantidade = 10;
        double valor = 50.00;
        storage = new Storage(sku, nome, quantidade, valor);
        when(estoqueMock.get(sku)).thenReturn(storage);
        
        inventoryManager.adicionarProduto(sku, nome, quantidade, valor); //adiciona produto ao estoque

        Storage storageEstoque = inventoryManager.verificarEstoque(sku); //verifica o estoque para um sku existente

        assertEquals(storageEstoque.getSku(), sku); //verifica se o sku corresponde
        assertEquals(storageEstoque.getNome(), nome); //verifica se o nome corresponde
        assertEquals(storageEstoque.getQuantidade(), quantidade); //verifica se a quantidade corresponde
        assertEquals(storageEstoque.getValor(), valor, 0.01); //verifica se o valor corresponde
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void verificarEstoqueSkuInvalido() { //teste negativo
        inventoryManager.verificarEstoque(0); //verifica o estoque para um sku invalido

        verify(estoqueMock, never()).get(anyInt()); //verifica se a operacao get nunca foi chamada no mock
                  
    }
    

    @org.junit.Test
    public void verificarEstoqueSkuNaoExiste() { //teste negativo
        inventoryManager.verificarEstoque(3232); //verifica o estoque para um sku que nao existe

        verify(estoqueMock).get(3232); //verifica se a operação get foi chamada com o slu especificado
        assertNull(inventoryManager.verificarEstoque(3232)); //verifica se o resultado é nulo
    }

    public void adicionarProdutoSucessoComPrecoExterno() {
        // Mocks e dados de teste
        Map<Integer, Storage> estoqueMock = mock(Map.class);
        PrecoService precoServiceMock = mock(PrecoService.class); // Crie o mock para PrecoService
        Storage storageMock = mock(Storage.class);
        InventoryManager inventoryManager = new InventoryManager(estoqueMock, precoServiceMock); // Injeta o mock
        int produtoSku = 3232; 
        String nomeProduto = "Camisa";
        int quantidadeEstoque = 10;
        double precoProdutoExterno = 55.00;

        when(storageMock.getValor()).thenReturn(precoProdutoExterno);
        // Define o comportamento do servico de preco antes de chamar o método de adicao
        when(precoServiceMock.getPreco(produtoSku)).thenReturn(precoProdutoExterno);

        // Execuçao do metodo a ser testado
        Storage storage = inventoryManager.adicionarProduto(produtoSku, nomeProduto, quantidadeEstoque, 0.0); //adiciona produto com preco externo

        // Verificacao se o serviço de preco foi chamado com o sku especificado
        verify(precoServiceMock).getPreco(produtoSku);

        // Verifica se o valor do produto corresponde ao preco externo
        assertEquals(precoProdutoExterno, storage.getValor(), 0.01);

        // Verifica se o produto foi adicionado ao estoque
        verify(estoqueMock).put(produtoSku, storage);
    }
    }
