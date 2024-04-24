package org.sample.LojaA3;


public class Storage {

    private int sku; //identificador único do produto sku.
    private String nome; //nome do produto.
    private int quantidade; //quantidade em estoque do produto.
    private double valor; //preço unitário do produto.

    //construtor da classe:
  
    public Storage(int sku, String nome, int quantidade, double valor) {
        this.sku = sku;
        this.nome = nome;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    //metodos Getter:

    public int getSku() {
        return sku;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getValor() {
        return valor;
    }

    //metodos Setter:
  
    public void setSku(int sku) {
        this.sku = sku;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}