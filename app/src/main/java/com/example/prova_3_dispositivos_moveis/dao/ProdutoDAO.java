package com.example.prova_3_dispositivos_moveis.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prova_3_dispositivos_moveis.model.Produto;

import java.util.List;

@Dao
public interface ProdutoDAO {
    @Insert
    public void inserir(Produto m);

    @Update
    public void alterar(Produto m);

    @Delete
    public void remover(Produto m);

    @Query("select * from produto where idSetor = :idSetor order by id")
    public LiveData<List<Produto>> listar(long idSetor);

}
