package com.example.prova_3_dispositivos_moveis.utils;

import android.widget.ArrayAdapter;

import androidx.lifecycle.Observer;

import com.example.prova_3_dispositivos_moveis.model.ListaCompras;

import java.util.LinkedList;
import java.util.List;

public class ObservadorListasCompras implements Observer<List<ListaCompras>> {
    LinkedList<ListaCompras> listaCompras;
    ArrayAdapter<ListaCompras> adapter;

    public ObservadorListasCompras(LinkedList<ListaCompras> listaCompras, ArrayAdapter<ListaCompras> adapter){
        this.listaCompras = listaCompras;
        this.adapter = adapter;
    }
    @Override
    public void onChanged(List<ListaCompras> listaCompra) {
        listaCompras.clear();
        listaCompras.addAll(listaCompra);
        adapter.notifyDataSetChanged();
    }
}