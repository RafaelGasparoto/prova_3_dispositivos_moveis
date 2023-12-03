package com.example.prova_3_dispositivos_moveis.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.prova_3_dispositivos_moveis.model.ListaCompras;

import java.util.List;
@Dao
public interface ListaComprasDAO {
    @Insert
    public long inserir(ListaCompras m);

    @Update
    public void alterar(ListaCompras m);

    @Delete
    public void remover(ListaCompras m);

    @Query("select * from lista_compras where id = :id order by nome")
    public LiveData<List<ListaCompras>> buscarPorListaCompras(long id);

    @Query("select * from lista_compras order by id")
    public LiveData<List<ListaCompras>> recuperarTodasListas();
}
