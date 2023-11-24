package com.example.prova_3_dispositivos_moveis.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;
@Entity(tableName = "lista_compras")
public class ListaCompras implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String nome;
    private int prioridade;
    private double valorEstimado;

    @Ignore
    private
    List<Item> itens;

    public ListaCompras(String nome) {
        this.setNome(nome);
    }

    @NonNull
    @Override
    public String toString() {
        return getId() + "-" + getNome();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public double getValorEstimado() {
        return valorEstimado;
    }

    public void setValorEstimado(double valorEstimado) {
        this.valorEstimado = valorEstimado;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }
}
