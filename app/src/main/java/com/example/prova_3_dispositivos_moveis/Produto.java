package com.example.prova_3_dispositivos_moveis;

public class Produto {
    long id;
    String descricao;
    double preco;
    long idSetor;

    Produto(long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", idSetor=" + idSetor +
                '}';
    }
}
