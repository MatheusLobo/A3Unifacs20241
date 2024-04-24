package org.sample.LojaA3;

import java.util.List;
import java.util.Objects;
import static org.mockito.Mockito.*;

public class Produto {

    private final String nome;
    private final double preco;

    public Produto(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return nome.equals(produto.nome) && preco == produto.preco;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, preco);
    }
}