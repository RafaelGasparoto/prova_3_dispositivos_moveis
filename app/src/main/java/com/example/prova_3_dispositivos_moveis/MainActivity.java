package com.example.prova_3_dispositivos_moveis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    final static int CRIAR_LISTA = 1;
    final static int SETOR = 2;
    ListView lista;
    ArrayAdapter<ListaCompras> adapter;
    LinkedList<ListaCompras> listaCompras;
    long idLista;
    public void makeListView() {
        listaCompras = new LinkedList<>();
        listaCompras.add(new ListaCompras(1, "Lista"));
        listaCompras.add(new ListaCompras(2, "Lista"));
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice,
                listaCompras);
        lista = findViewById(R.id.listas_compras);
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i)
            {
                idLista = listaCompras.get(position).id;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFloatingActionButton();
        makeListView();
    }

    private void initFloatingActionButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.irParaCriarLista);

        floatingActionButton.setOnClickListener(view -> {
            irParaCriarLista();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setor_1:
                irParaSetor(1);
                break;
            case R.id.setor_2:
                irParaSetor(2);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void irParaSetor(int setor) {
        Intent intent = new Intent(this, Setor.class);
        intent.putExtra("Setor", setor);
        startActivityForResult(intent, SETOR);
    }

    private void irParaCriarLista() {
        Intent intent = new Intent(this, CriarLista.class);
        startActivityForResult(intent, CRIAR_LISTA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_overflow, menu);
        return true;
    }

}