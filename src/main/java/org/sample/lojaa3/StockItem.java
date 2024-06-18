package org.sample.lojaa3;

public class StockItem {
    private int sku;
    private String nome;
    private int quantidade;
    private double valor;

    public StockItem(int sku, String nome, int quantidade, double valor) {
        this.sku = sku;
        this.nome = nome;
        this.quantidade = quantidade;
        this.valor = valor;
    }

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

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
