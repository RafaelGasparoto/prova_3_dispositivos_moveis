package com.example.prova_3_dispositivos_moveis.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.prova_3_dispositivos_moveis.model.ListaCompras;
import com.example.prova_3_dispositivos_moveis.model.Produto;


@Database(entities = {ListaCompras.class, Produto.class}, version = 2)
public abstract class Banco extends RoomDatabase {
    public abstract ListaComprasDAO getListaComprasDAO();

    public abstract ProdutoDAO getProdutoDAO();
}
