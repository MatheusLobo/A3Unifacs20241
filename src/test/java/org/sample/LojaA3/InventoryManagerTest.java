package org.sample.LojaA3;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.sample.LojaA3.InventoryManager.PrecoService;

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
    public void adcionarProdutoSucesso() {
        int sku = 3232;
        String nome = "Camiseta";
        int quantidade = 100;
        double valor = 50.00;
        
        StockItem stockItem = null;
        
        try {
            stockItem = inventoryManager.adicionarProduto(sku, nome, quantidade, valor);
        } catch (StockException e) {
            fail("Nenhuma exceção esperada, mas uma EstoqueException foi lançada: " + e.getMessage());
        }

        verify(estoqueMock).put(sku, stockItem); 
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
              
 
    
    @Test
    public void removerProdutoSucesso() {
        Map<Integer, StockItem> estoqueMock = mock(Map.class);
        InventoryManager inventoryManager = new InventoryManager(estoqueMock, null);
        int sku = 3232;
        String nome = "Camisa";
        int quantidadeInicial = 10;
        int quantidadeRemover = 5;
        double valor = 50.00;

        when(estoqueMock.get(eq(sku))).thenReturn(new StockItem(sku, nome, quantidadeInicial, valor));

        try {
            inventoryManager.removerProduto(sku, quantidadeRemover);
        } catch (StockException e) {
            fail("Nenhuma exceção esperada, mas uma EstoqueException foi lançada: " + e.getMessage());
        }

        verify(estoqueMock).get(eq(sku));
        assertEquals(quantidadeInicial - quantidadeRemover, ((StockItem) estoqueMock.get(sku)).getQuantidade());
    }

    @Test(expected = SkuInvalidException.class)
    public void removerProdutoSkuInvalido() {
        try {
            inventoryManager.removerProduto(0, 5);
        } catch (StockException e) {
            assertEquals("SKU invalido: 0", e.getMessage());
            throw e; 
        }
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

    @Test(expected = SkuInvalidException.class)
    public void verificarEstoqueSkuInvalido() {
        inventoryManager.verificarEstoque(0);
    }
    
    @Test
    public void verificarEstoqueSkuNaoExiste() {
        inventoryManager.verificarEstoque(3232);
        verify(estoqueMock).get(3232);
        assertNull(inventoryManager.verificarEstoque(3232));
    }
    @Test
    public void adicionarProdutoSucessoComPrecoExterno() {
        int produtoSku = 3232;
        String nomeProduto = "Camisa";
        int quantidadeEstoque = 10;
        double precoProdutoExterno = 55.00;

        when(precoServiceMock.getPreco(produtoSku)).thenReturn(precoProdutoExterno);

        StockItem stockItem = inventoryManager.adicionarProduto(produtoSku, nomeProduto, quantidadeEstoque, 0.0);

        verify(precoServiceMock).getPreco(produtoSku); 
        assertEquals(precoProdutoExterno, stockItem.getValor(), 0.01);
        verify(estoqueMock).put(produtoSku, stockItem);
    }

    @Test
    public void adicionarProdutoSucesso_Caminho() {
        int sku = 1234;
        String nome = "Camiseta";
        int quantidade = 10;
        double valor = 20.00;

        StockItem stockItem = inventoryManager.adicionarProduto(sku, nome, quantidade, valor);

        assertNotNull(stockItem); 
        assertEquals(sku, stockItem.getSku()); 
        assertEquals(nome, stockItem.getNome()); 
        assertEquals(quantidade, stockItem.getQuantidade()); 
        assertEquals(valor, stockItem.getValor(), 0.01); 
        verify(estoqueMock).put(eq(sku), eq(stockItem)); 
    }
 
    @Test(expected = PriceInvalidException.class)
    public void adicionarProdutoPrecoInvalido() {
        int sku = 1234;
        String nome = "Camiseta";
        int quantidade = 10;
        double valor = 0.00;

        when(precoServiceMock.getPreco(eq(sku))).thenThrow(PriceInvalidException.class);

        inventoryManager.adicionarProduto(sku, nome, quantidade, valor);

       
    }
    @Test
    public void storageSettersGetters() {
        int sku = 9876;
        String nome = "Calça";
        int quantidade = 20;
        double valor = 65.99;

        StockItem stockItem = new StockItem(sku, nome, quantidade, valor);

        stockItem.setSku(1234);
        stockItem.setNome("Jaqueta");
        stockItem.setQuantidade(30);
        stockItem.setValor(79.99);

        assertEquals(1234, stockItem.getSku());
        assertEquals("Jaqueta", stockItem.getNome());
        assertEquals(30, stockItem.getQuantidade());
        assertEquals(79.99, stockItem.getValor(), 0.01);
    }
    @Test(expected = SkuInvalidException.class)
    public void verificarEstoqueSkuInvalidoNegativo() {
        int sku = -1234;

        inventoryManager.verificarEstoque(sku);

        fail("Exceção SkuInvalidoException não foi lançada");
    }
    @Test(expected = PriceInvalidException.class)
    public void adicionarProdutoPrecoInvalidoZero() {
        int sku = 4321;
        String nome = "Camiseta";
        int quantidade = 50;
        double valor = 0.0;

        when(precoServiceMock.getPreco(eq(sku))).thenReturn(0.0);

        inventoryManager.adicionarProduto(sku, nome, quantidade, valor);
    }
    @Test(expected = StockUnavailableException.class)
    public void removerProdutoEstoqueIndisponivel() {
        int sku = 1234;
        int quantidade = 100;

        inventoryManager.removerProduto(sku, quantidade);

        fail("Exceção EstoqueIndisponivelException não foi lançada");
    }
}