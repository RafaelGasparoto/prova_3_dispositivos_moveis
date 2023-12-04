package com.example.prova_3_dispositivos_moveis.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prova_3_dispositivos_moveis.model.Item;
import com.example.prova_3_dispositivos_moveis.model.Produto;

import java.util.List;

@Dao
public interface ItemDAO {
    @Insert
    public void inserir(Item item);

    @Update
    public void alterar(Item item);

    @Delete
    public void remover(Item item);

    @Query("delete from item_lista where listaId = :listaId")
    public void removerPorListaId(long listaId);

    @Query("select * from item_lista where listaId = :listaId order by id")
    public LiveData<List<Item>> listar(long listaId);

    @Query("select * from item_lista where id = :id")
    public LiveData<Item> item_por_id(long id);


}
