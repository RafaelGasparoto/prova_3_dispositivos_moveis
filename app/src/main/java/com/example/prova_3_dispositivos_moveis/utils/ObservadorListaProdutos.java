package com.example.prova_3_dispositivos_moveis.utils;

import android.widget.ArrayAdapter;

import androidx.lifecycle.Observer;

import com.example.prova_3_dispositivos_moveis.model.Produto;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import javax.security.auth.callback.Callback;

public class ObservadorListaProdutos implements Observer<List<Produto>> {
    LinkedList<Produto> listaProdutos;
    ArrayAdapter<Produto> adapter;

    Runnable runnable;
    public ObservadorListaProdutos(LinkedList<Produto> listaCompras, ArrayAdapter<Produto> adapter) {
        this.listaProdutos = listaCompras;
        this.adapter = adapter;
    }

    public ObservadorListaProdutos(LinkedList<Produto> listaCompras, ArrayAdapter<Produto> adapter, Runnable runnable)

    {
        this.runnable = runnable;
        this.listaProdutos = listaCompras;
        this.adapter = adapter;
    }

    @Override
    public void onChanged(List<Produto> produtos) {
        listaProdutos.clear();
        listaProdutos.addAll(produtos);
        adapter.notifyDataSetChanged();
        if(runnable != null) {
            runnable.run();
        }
    }
}