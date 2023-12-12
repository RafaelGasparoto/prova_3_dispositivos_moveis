package com.example.prova_3_dispositivos_moveis.dao;

import androidx.room.Database;
import androidx.room.RawQuery;
import androidx.room.RoomDatabase;

import com.example.prova_3_dispositivos_moveis.model.Item;
import com.example.prova_3_dispositivos_moveis.model.ListaCompras;
import com.example.prova_3_dispositivos_moveis.model.Produto;

@Database(entities = {ListaCompras.class, Produto.class, Item.class}, version = 5)
public abstract class Banco extends RoomDatabase {
    public abstract ListaComprasDAO getListaComprasDAO();

    public abstract ProdutoDAO getProdutoDAO();
    public abstract ItemDAO getItemDAO();
}
