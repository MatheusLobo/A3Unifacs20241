package org.sample.LojaA3;



import java.util.Map; 
public class InventoryManager {

    private final Map<Integer, Storage> estoque; //Mapa representando o estoque. Chave: SKU do produto, Valor: objeto Storage.
    private final PrecoService precoService;

  
    //criação de um objeto InventoryManager.

    public InventoryManager(Map<Integer, Storage> estoque, PrecoService precoService) {
        this.estoque = estoque; // Armazena a referência para o mapa do estoque.
        this.precoService = precoService; // Armazena a referência para o serviço de preços (opcional).
    }

    //metodo adicionarProduto:
    //adiciona um novo produto ao estoque.

    public Storage adicionarProduto(int sku, String nome, int quantidade, double valor) {
        if (sku <= 0 || nome == null || nome.isEmpty() || quantidade <= 0) {
            throw new IllegalArgumentException("Parâmetros inválidos");
        }

        Storage storage = new Storage(sku, nome, quantidade, valor); //cria um novo objeto Storage.
        return estoque.put(storage.getSku(), storage); //adiciona o Storage no mapa do estoque
    }

    //metodo removerProduto:
    //remove uma certa quantidade de um produto do estoque.

    public void removerProduto(int sku, int quantidade) {
        if (sku <= 0 || quantidade <= 0) {
            throw new IllegalArgumentException("SKU ou quantidade inválida");
        }

        if (estoque.containsKey(sku)) { //verifica se o SKU existe no estoque.
            Storage storage = estoque.get(sku); //recupera o objeto Storage associado ao SKU.
            int quantidadeAtual = storage.getQuantidade(); //obtem a quantidade atual do produto no estoque.

            if (quantidadeAtual < quantidade) {
                throw new IllegalArgumentException("Quantidade excede o estoque disponível");
            }

            storage.setQuantidade(quantidadeAtual - quantidade); // atualiza a quantidade produto no estoque.

            if (storage.getQuantidade() == 0) { //se zerar, remove o produto do estoque.
                estoque.remove(sku);
            }
        }
    }

    
    // recupera o objeto Storage associado a um determinado SKU (se existir).

    public Storage verificarEstoque(int sku) {
        if (sku <= 0) {
            throw new IllegalArgumentException("SKU inválido");
        }

        return estoque.get(sku); // retorna o objeto Storage associado ao SKU, ou null se não existir.
    }

    // interface PrecoService:
    // define uma interface para serviços de consulta de preços (não implementada aqui).

    interface PrecoService {
        double getPreco(int sku); // metodo para obter o preço de um produto.
    }
}