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
        // Validação dos parâmetros de entrada (sku, nome, quantidade)
        if (sku <= 0) {
            throw new IllegalArgumentException("SKU inválido: " + sku);
        }

        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Nome inválido: " + nome);
        }

        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade inválida: " + quantidade);
        }

        // Busca o preço do produto no serviço externo se nenhum valor for fornecido
        if (valor <= 0) {
            if (precoService == null) {
                throw new IllegalStateException("Serviço de preço não configurado");
            }

            valor = precoService.getPreco(sku);
        }

        // Cria um novo objeto Storage com os valores informados
        Storage storage = new Storage(sku, nome, quantidade, valor);

        // Adiciona o Storage no mapa do estoque
        estoque.put(storage.getSku(), storage);

        // Retorna o Storage criado
        return storage;
    }
    //metodo removerProduto:
    //remove uma certa quantidade de um produto do estoque.

    public void removerProduto(int sku, int quantidade) {
        if (sku <= 0 || quantidade <= 0) {
            throw new IllegalArgumentException("SKU ou quantidade inválida");
        }

        if (estoque.containsKey(sku)) { //verifica se o sku existe no estoque.
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

    
    // verifica o estoque usando o sku

    public Storage verificarEstoque(int sku) {
        if (sku <= 0) {
            throw new IllegalArgumentException("SKU inválido");
        }

        return estoque.get(sku); // retorna o objeto Storage associado ao SKU, ou null se não existir.
    }

    // interface PrecoService;
    // define uma interface para serviços de consulta de preços (não implementada aqui).

    interface PrecoService {
        double getPreco(int sku); // metodo para obter o preço de um produto.
    }
}