package org.sample.LojaA3;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class InventoryManagerTest {

	    @Mock
	    private Map<Produto, Integer> estoqueMock;

	    @InjectMocks
	    private InventoryManager inventoryManager;

	    @org.junit.Test
	    public void adicionarProdutoSucesso() {
	        Produto produto = new Produto("Camiseta", 50.00);
	        int quantidade = 10;

	        inventoryManager.adicionarProduto(produto, quantidade);

	        verify(estoqueMock).put(produto, quantidade);
	    }

	    @org.junit.Test(expected = IllegalArgumentException.class)
	    public void adicionarProdutoProdutoNull() {
	        int quantidade = 10;

	        inventoryManager.adicionarProduto(null, quantidade);
	    }

	    @org.junit.Test(expected = IllegalArgumentException.class)
	    public void adicionarProdutoQuantidadeInvalida() {
	        Produto produto = new Produto("Camiseta", 50.00);
	        int quantidade = -10;

	        inventoryManager.adicionarProduto(produto, quantidade);
	    }

	    @org.junit.Test
	    public void removerProdutoSucesso() {
	        Produto produto = new Produto("Camiseta", 50.00);
	        int quantidade = 10;

	        inventoryManager.adicionarProduto(produto, quantidade);
	        inventoryManager.removerProduto(produto, 5);

	        verify(estoqueMock).put(produto, 5);
	    }

	    @org.junit.Test(expected = IllegalArgumentException.class)
	    public void removerProdutoProdutoNull() {
	        int quantidade = 5;

	        inventoryManager.removerProduto(null, quantidade);
	    }

	    @org.junit.Test(expected = IllegalArgumentException.class)
	    public void removerProdutoQuantidadeInvalida() {
	        Produto produto = new Produto("Camiseta", 50.00);
	        int quantidade = -5;

	        inventoryManager.removerProduto(produto, quantidade);
	    }

	    @org.junit.Test(expected = IllegalArgumentException.class)
	    public void removerProdutoQuantidadeExcedeEstoque() {
	        Produto produto = new Produto("Camiseta", 50.00);
	        int quantidade = 15;

	        inventoryManager.removerProduto(produto, quantidade);
	    }
	    @org.junit.Test
	    public void verificarEstoqueSucesso() {
	        Produto produto = new Produto("Camiseta", 50.00);
	        int quantidadeEsperada = 10;

	        when(estoqueMock.getOrDefault(produto, 0)).thenReturn(quantidadeEsperada);

	        int quantidadeEstoque = inventoryManager.verificarEstoque(produto);

	        assertEquals(quantidadeEsperada, quantidadeEstoque);
	        verify(estoqueMock).getOrDefault(produto, 0);
	    }

	    @org.junit.Test
	    public void verificarEstoqueProdutoNull() {
	        inventoryManager.verificarEstoque(null);

	        verify(estoqueMock, never()).getOrDefault(any(Produto.class), anyInt());
	    }
	    } 
