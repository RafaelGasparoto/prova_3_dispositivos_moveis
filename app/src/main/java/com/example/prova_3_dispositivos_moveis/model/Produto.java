package com.example.prova_3_dispositivos_moveis.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "produto")
public class Produto implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String descricao;
    private double preco;
    private long idSetor;

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + getId() +
                ", descricao='" + getDescricao() + '\'' +
                ", preco=" + getPreco() +
                ", idSetor=" + getIdSetor() +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public long getIdSetor() {
        return idSetor;
    }

    public void setIdSetor(long idSetor) {
        this.idSetor = idSetor;
    }
}
