package com.example.prova_3_dispositivos_moveis.utils;

import android.widget.ArrayAdapter;

import androidx.lifecycle.Observer;

import com.example.prova_3_dispositivos_moveis.model.Produto;

import java.util.LinkedList;
import java.util.List;

public class ObservadorListaProdutos implements Observer<List<Produto>> {
    LinkedList<Produto> listaProdutos;
    ArrayAdapter<Produto> adapter;

    public ObservadorListaProdutos(LinkedList<Produto> listaProdutos, ArrayAdapter<Produto> adapter){
        this.listaProdutos = listaProdutos;
        this.adapter = adapter;
    }
    @Override
    public void onChanged(List<Produto> produtos) {
        listaProdutos.clear();
        listaProdutos.addAll(produtos);
        adapter.notifyDataSetChanged();
    }
}