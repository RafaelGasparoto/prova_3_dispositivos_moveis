package com.example.prova_3_dispositivos_moveis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.prova_3_dispositivos_moveis.dao.Banco;
import com.example.prova_3_dispositivos_moveis.dao.ListaComprasDAO;
import com.example.prova_3_dispositivos_moveis.dao.ProdutoDAO;
import com.example.prova_3_dispositivos_moveis.model.ListaCompras;
import com.example.prova_3_dispositivos_moveis.model.Produto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    final static int CRIAR_LISTA = 1;
    final static int CRIAR_PRODUTO = 2;
    final static int SETOR = 2;

     Banco bd;
     ProdutoDAO produtoDAO;
     ListaComprasDAO listaComprasDAO;
    ListView lista;
    ArrayAdapter<ListaCompras> adapter;
    LinkedList<ListaCompras> listaCompras;
    long idLista;

    public void iniciarListView() {
        listaCompras = new LinkedList<>();
        listaCompras.add(new ListaCompras("Lista"));
        listaCompras.add(new ListaCompras( "Lista"));
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice,
                listaCompras);
        lista = findViewById(R.id.listas_compras);
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                idLista = listaCompras.get(position).getId();
            }
        });
        lista.setItemChecked(0, true);
        idLista = listaCompras.get(0).getId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFloatingActionButton();
        iniciarListView();
        iniciarBotoes();
        iniciarBd();
    }

    private void iniciarBd() {
        bd = Room.databaseBuilder(getApplicationContext(), Banco.class, "ListaDeCompras").
                fallbackToDestructiveMigration().build();
        produtoDAO = bd.getProdutoDAO();
        listaComprasDAO = bd.getListaComprasDAO();
    }

    private void iniciarBotoes() {
        Button alterar = findViewById(R.id.alterar_lista_compras);
        alterar.setOnClickListener(v -> alterarListaCompra());
        Button realizarCompra = findViewById(R.id.realizar_compra);
        realizarCompra.setOnClickListener(v -> realizarCompra());
        Button excluir = findViewById(R.id.exluir_lista_compras);
        excluir.setOnClickListener(v -> excluirListaCompra());
    }

    public void realizarCompra() {
        if (Long.valueOf(idLista) != null) {

        }
    }

    public void alterarListaCompra() {
        if (Long.valueOf(idLista) != null) {
            irParaCriarLista(Math.toIntExact(Long.valueOf(idLista)));
        }
    }

    public void excluirListaCompra() {
        if (Long.valueOf(idLista) != null) {
            listaCompras.remove(listaCompras);
        }
    }

    private void initFloatingActionButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.irParaCriarLista);

        floatingActionButton.setOnClickListener(view -> {
            irParaCriarLista(-1);
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

    private void irParaSetor(long setor) {
        Intent intent = new Intent(this, EditarSetor.class);
        intent.putExtra("Setor", setor);
        startActivityForResult(intent, SETOR);
    }

    private void irParaCriarLista(int idLista) {
        Intent intent = new Intent(this, CriarLista.class);
        if (idLista != -1)
            intent.putExtra("IdLista", idLista);
        startActivityForResult(intent, CRIAR_LISTA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_overflow, menu);
        return true;
    }

    @Override
    public void onStop() {
        bd.close();
        super.onStop();
    }
}