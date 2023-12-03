package com.example.prova_3_dispositivos_moveis.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "item_lista")
public class Item implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private double quantidade;
    private boolean comprado;
    private long listaId;
    private long produtoId;
    @Ignore
    private Produto produto;

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isComprado() {
        return comprado;
    }

    public void setComprado(boolean comprado) {
        this.comprado = comprado;
    }

    public long getListaId() {
        return listaId;
    }

    public void setListaId(long listaId) {
        this.listaId = listaId;
    }

    public long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(long produtoId) {
        this.produtoId = produtoId;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", quantidade=" + quantidade +
                ", comprado=" + comprado +
                ", listaId=" + listaId +
                ", produtoId=" + produtoId +
                '}';
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
