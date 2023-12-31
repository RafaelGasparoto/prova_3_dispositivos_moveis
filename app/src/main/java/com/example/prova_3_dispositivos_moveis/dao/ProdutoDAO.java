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
import com.example.prova_3_dispositivos_moveis.model.Produto;

import java.util.List;

@Dao
public interface ProdutoDAO {
    @Insert
    public void inserir(Produto produto);

    @Update
    public void alterar(Produto produto);

    @Delete
    public void remover(Produto produto);

    @Query("select * from produto where idSetor = :idSetor order by id")
    public LiveData<List<Produto>> listar(long idSetor);

    @Query("select * from produto where idSetor = :idSetor and id = :listaId order by id")
    public LiveData<Produto> listarProdutosComprar(long idSetor, long listaId);

    @Query("select * from produto where id = :id")
    public LiveData<Produto> produto_por_id(long id);

    @RawQuery(observedEntities = Produto.class)
    LiveData<Long> busrcarIdUltimaLista(SupportSQLiteQuery query);


}
