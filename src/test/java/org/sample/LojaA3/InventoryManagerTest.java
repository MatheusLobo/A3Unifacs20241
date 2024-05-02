package org.sample.LojaA3;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.sample.LojaA3.InventoryManager.PrecoService;

@RunWith(MockitoJUnitRunner.class) //necessario para rodar os mocks
public class InventoryManagerTest {

    @Mock
    private Map<Integer, Storage> estoqueMock; //mock para o mapa de estoque
    @Mock
    private PrecoService precoServiceMock; //mock para o servico de preco

    @InjectMocks
    private InventoryManager inventoryManager; //instancia do gerenciador de estoque
    private Storage storage; //objeto de armazenamento simulado
    
    @Test
    public void adicionarProdutoSucesso() { //teste positivo
        int sku = 3232;
        String nome = "Camiseta";
        int quantidade = 100;
        double valor = 50.00;

        // Armazena o objeto retornado pelo metodo
        Storage storage = null;
        try {
            storage = inventoryManager.adicionarProduto(sku, nome, quantidade, valor); //adiciona um produto
        } catch (EstoqueException e) {
            // Se uma EstoqueException for lancada, o teste falhara
            fail("Nenhuma exceção esperada, mas uma EstoqueException foi lançada: " + e.getMessage());
        }

        // Verifica se o mock foi chamado com o sku e o Storage correto
        verify(estoqueMock).put(sku, storage); 
    }
    @Test
    public void adicionarProdutoStorageNull() { //teste negativo com excecao
        try {
            inventoryManager.adicionarProduto(0, null, 0, 0); //tentativa de adicionar produto com parametros invalidos
        } catch (QuantidadeInvalidaException e) {
        	// excecao esperada
        } catch (EstoqueException e) {
        
            fail("Quantidade errada");
        }
    }
    
    @Test
    public void adicionarProdutoQuantidadeInvalida() { //teste negativo com excecao
        try {
        	inventoryManager.adicionarProduto(3232, "Camiseta", -10, 50.00);//tentativa de adicionar produto com parametros invalidos
        } catch (QuantidadeInvalidaException e) {
            // excecao esperada
        } catch (EstoqueException e) {
            
            fail("Quantidade errada");
        }
    }
              
 
    
    @Test
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
        when(estoqueMock.get(eq(sku))).thenReturn(new Storage(sku, nome, quantidadeInicial, valor));

        // Execucao do metodo a ser testado
        try {
            inventoryManager.removerProduto(sku, quantidadeRemover); //remove uma quantidade do produto
        } catch (EstoqueException e) {
            // Se uma EstoqueException for lancada, o teste falhara
            fail("Nenhuma exceção esperada, mas uma EstoqueException foi lançada: " + e.getMessage());
        }

        verify(estoqueMock).get(eq(sku));
        assertEquals(quantidadeInicial - quantidadeRemover, ((Storage) estoqueMock.get(sku)).getQuantidade());
    }

    @Test(expected = SkuInvalidoException.class)
    public void removerProdutoSkuInvalido() { //teste negativo com excecao
        try {
            inventoryManager.removerProduto(0, 5); //tentativa de remover produto com SKU invalido
        } catch (EstoqueException e) {
            assertEquals("SKU invalido: 0", e.getMessage());
            throw e; // Garante que o teste falhe se a excecao nao for lançada
        }
    }

    @Test(expected = QuantidadeInvalidaException.class)
    public void removerProdutoQuantidadeInvalida() { //teste negativo com excecao
        try {
            inventoryManager.removerProduto(123, -5); //tentativa de remover produto com quantidade invalida
        } catch (QuantidadeInvalidaException e) {
            assertEquals("Quantidade invalida: -5", e.getMessage()); //verifica se dois valores são iguais e lança uma excecao
            throw e; // Garante que o teste falhe se a exceção nao for lançada
        }
    }

    @Test
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

    @Test(expected = SkuInvalidoException.class)
    public void verificarEstoqueSkuInvalido() { //teste negativo
        inventoryManager.verificarEstoque(0); //verifica o estoque para um sku invalido
    }
    
    @Test
    public void verificarEstoqueSkuNaoExiste() { //teste negativo
        inventoryManager.verificarEstoque(3232); //verifica o estoque para um sku que nao existe

        verify(estoqueMock).get(3232); //verifica se a operação get foi chamada com o slu especificado
        assertNull(inventoryManager.verificarEstoque(3232)); //verifica se o resultado é nulo
    }
    @Test   
    public void adicionarProdutoSucessoComPrecoExterno() {
        // Mocks e dados de teste
        int produtoSku = 3232; 
        String nomeProduto = "Camisa";
        int quantidadeEstoque = 10;
        double precoProdutoExterno = 55.00;

        // Define o comportamento do serviço de preco antes de chamar o método de adicao
        when(precoServiceMock.getPreco(produtoSku)).thenReturn(precoProdutoExterno);

        // Execução do mettodo a ser testado
        Storage storage = inventoryManager.adicionarProduto(produtoSku, nomeProduto, quantidadeEstoque, 0.0); //adiciona produto com preco externo

        // Verificacao se o serviço de preço foi chamado com o SKU especificado
        verify(precoServiceMock).getPreco(produtoSku);

        // Verifica se o valor do produto corresponde ao preço externo
        assertEquals(precoProdutoExterno, storage.getValor(), 0.01);

        // Verifica se o produto foi adicionado ao estoque
        verify(estoqueMock).put(produtoSku, storage);
    }
   }