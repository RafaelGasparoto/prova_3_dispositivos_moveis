package com.example.prova_3_dispositivos_moveis;

import androidx.annotation.NonNull;

import java.util.List;

public class ListaCompras {
    long id;
    String nome;
    int prioridade;
    double valorEstimado;
    List<Item> itens;

    ListaCompras(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    @NonNull
    @Override
    public String toString() {
        return id + "-" + nome;
    }
}
