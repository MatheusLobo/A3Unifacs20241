package org.sample.lojaa3;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.sample.lojaa3.InventoryManager.PrecoService;

@RunWith(MockitoJUnitRunner.class)
public class InventoryManagerTest {

    @Mock
    private Map<Integer, StockItem> estoqueMock;
    @Mock
    private PrecoService precoServiceMock;
    @InjectMocks
    private InventoryManager inventoryManager;

    private StockItem stockItem;

    @Test
    public void adicionarProdutoSucesso() {
        int sku = 3232;
        String nome = "Camiseta";
        int quantidade = 100;
        double valor = 50.00;

        StockItem localStockItem = null;

        try {
            localStockItem = inventoryManager.adicionarProduto(sku, nome, quantidade, valor);
        } catch (StockException e) {
            fail("Nenhuma exceção esperada, mas uma EstoqueException foi lançada: " + e.getMessage());
        }
        verify(estoqueMock).put(sku, localStockItem);
    }

  
    @Test
    public void adicionarProdutoStorageNull() {
        try {
            inventoryManager.adicionarProduto(0, null, 0, 0.0);
        } catch (QuantityInvalidException e) {
            // excecao esperada
        } catch (StockException e) {
            fail("Quantidade errada");
        }
    }

    
    @Test
    public void adicionarProdutoQuantidadeInvalida() {
        try {
            inventoryManager.adicionarProduto(3232, "Camiseta", -10, 50.00);
        } catch (QuantityInvalidException e) {
            // excecao esperada
        } catch (StockException e) {
            fail("Quantidade errada");
        }
    }

   
    @Test(expected = PriceInvalidException.class)
    public void adicionarProdutoPrecoInvalido() {
        int sku = 1234;
        String nome = "Camiseta";
        int quantidade = 10;
        double valor = 0.00;
        when(precoServiceMock.getPreco(sku)).thenThrow(PriceInvalidException.class);
        inventoryManager.adicionarProduto(sku, nome, quantidade, valor);
    }


    
    @Test
    public void adicionarProdutoSucessoComPrecoExterno() {
        int produtoSku = 3232;
        String nomeProduto = "Camisa";
        int quantidadeEstoque = 10;
        double precoProdutoExterno = 55.00;
        when(precoServiceMock.getPreco(produtoSku)).thenReturn(precoProdutoExterno);
        StockItem localStockItem = inventoryManager.adicionarProduto(produtoSku, nomeProduto, quantidadeEstoque, 0.0);
        verify(precoServiceMock).getPreco(produtoSku);
        assertEquals(precoProdutoExterno, localStockItem.getValor(), 0.01);
        verify(estoqueMock).put(produtoSku, localStockItem);
    }

    
    @Test
    public void removerProdutoSkuInvalido() {
        assertThrows(SkuInvalidException.class, () -> {
            inventoryManager.removerProduto(0, 5);
        });
    }

    
    @Test
    public void adicionarProdutoSucesso_Caminho() {
        int sku = 1234;
        String nome = "Camiseta";
        int quantidade = 10;
        double valor = 20.00;
        StockItem localStockItem = inventoryManager.adicionarProduto(sku, nome, quantidade, valor);
        assertNotNull(localStockItem);
        assertEquals(sku, localStockItem.getSku());
        assertEquals(nome, localStockItem.getNome());
        assertEquals(quantidade, localStockItem.getQuantidade());
        assertEquals(valor, localStockItem.getValor(), 0.01);
        verify(estoqueMock).put(sku, localStockItem);
    }

    
    @Test
    public void verificarEstoqueSucesso() {
        int sku = 3232;
        String nome = "Camisa";
        int quantidade = 10;
        double valor = 50.00;

        lenient().when(precoServiceMock.getPreco(eq(sku))).thenReturn(50.00);

        inventoryManager.adicionarProduto(sku, nome, quantidade, valor);

        StockItem storageEstoque = inventoryManager.verificarEstoque(sku);
        if (storageEstoque != null) {
            assertEquals(sku, storageEstoque.getSku());
            assertEquals(nome, storageEstoque.getNome());
            assertEquals(quantidade, storageEstoque.getQuantidade());
            assertEquals(valor, storageEstoque.getValor(), 0.01);
        }
    }
  //(Teste de Integração)
    @Test
    public void removerProdutoSucesso() {
        Map<Integer, StockItem> localEstoqueMock = mock(Map.class);
        InventoryManager localInventoryManager = new InventoryManager(localEstoqueMock, null);
        int sku = 3232;
        String nome = "Camisa";
        int quantidadeInicial = 10;
        int quantidadeRemover = 5;
        double valor = 50.00;
        when(localEstoqueMock.get(sku)).thenReturn(new StockItem(sku, nome, quantidadeInicial, valor));
        try {
            localInventoryManager.removerProduto(sku, quantidadeRemover);
        } catch (StockException e) {
            fail("Nenhuma exceção esperada, mas uma EstoqueException foi lançada: " + e.getMessage());
        }
        verify(localEstoqueMock).get(sku);
        assertEquals(quantidadeInicial - quantidadeRemover, ((StockItem) localEstoqueMock.get(sku)).getQuantidade());
    }

    
    //(Teste de caixa Branca)
    @Test
    public void removerProdutoEstoqueVazio() {
        int sku = 1234;
        int quantidadeRemover = 5;

        when(estoqueMock.get(sku)).thenReturn(new StockItem(sku, "Produto Teste", 0, 10.0));

        assertThrows(StockUnavailableException.class, () -> {
            inventoryManager.removerProduto(sku, quantidadeRemover);
        });

        verify(estoqueMock).get(sku);
    }


    

    // (Teste de Caixa Preta)
    @Test
    public void storageSettersGetters() {
        int sku = 9876;
        String nome = "Calça";
        int quantidade = 20;
        double valor = 65.99;
        StockItem localStockItem = new StockItem(sku, nome, quantidade, valor);
        localStockItem.setSku(1234);
        localStockItem.setNome("Jaqueta");
        localStockItem.setQuantidade(30);
        localStockItem.setValor(79.99);
        assertEquals(1234, localStockItem.getSku());
        assertEquals("Jaqueta", localStockItem.getNome());
        assertEquals(30, localStockItem.getQuantidade());
        assertEquals(79.99, localStockItem.getValor(), 0.01);
    }


    }

