package com.example.prova_3_dispositivos_moveis.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prova_3_dispositivos_moveis.model.ListaCompras;

import java.util.List;

@Dao
public interface ListaComprasDAO {
    @Insert
    public void inserir(ListaCompras m);

    @Update
    public void alterar(ListaCompras m);

    @Delete
    public void remover(ListaCompras m);

    @Query("select * from lista_compras where id = :id order by nome")
    public LiveData<List<ListaCompras>> buscarPorListaCompras(long id);

}
