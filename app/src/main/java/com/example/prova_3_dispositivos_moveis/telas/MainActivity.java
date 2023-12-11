package com.example.prova_3_dispositivos_moveis.telas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.prova_3_dispositivos_moveis.R;
import com.example.prova_3_dispositivos_moveis.dao.Banco;
import com.example.prova_3_dispositivos_moveis.dao.ItemDAO;
import com.example.prova_3_dispositivos_moveis.dao.ListaComprasDAO;
import com.example.prova_3_dispositivos_moveis.dao.ProdutoDAO;
import com.example.prova_3_dispositivos_moveis.model.Item;
import com.example.prova_3_dispositivos_moveis.model.ListaCompras;
import com.example.prova_3_dispositivos_moveis.utils.ObservadorListasCompras;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    final static int CRIAR_LISTA = 1;
    final static int CRIAR_PRODUTO = 2;
    final static int SETOR = 2;

    Banco bd;
    ProdutoDAO produtoDAO;
    ItemDAO itemDAO;
    ListaComprasDAO listaComprasDAO;
    ListView listView;
    ArrayAdapter<ListaCompras> adapter;
    LinkedList<ListaCompras> listaCompras;
    long idLista;

    public void iniciarListView() {
        listaCompras = new LinkedList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice,
                listaCompras);
        listView = findViewById(R.id.listas_compras);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, i) -> {
            idLista = listaCompras.get(position).getId();
            pos = position;
        });
        recupararListasDoBd();
        listView.setItemChecked(0, true);
    }

    int pos;
    ObservadorListasCompras listaComprasObs;

    private void recupararListasDoBd() {
        listaComprasObs = new ObservadorListasCompras(listaCompras, adapter);
        listaComprasDAO.listar().observe(this, listaComprasObs);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciarBd();
        initFloatingActionButton();
        iniciarListView();
        iniciarBotoes();
    }
    @Override
    public void onActivityResult(int code,
                                 int result, Intent data) {
        super.onActivityResult(code, result, data);
        if (result == RESULT_OK) {
            Boolean resetar_lista = (Boolean) data.getSerializableExtra("lista_produtos_alterado");
            if (resetar_lista) {
                recupararListasDoBd();
            }
        }
    }
    private void iniciarBd() {
        bd = Room.databaseBuilder(getApplicationContext(), Banco.class, "ListaDeCompras").
                fallbackToDestructiveMigration().build();
        produtoDAO = bd.getProdutoDAO();
        itemDAO = bd.getItemDAO();
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
        idLista = listView.getCheckedItemPosition();
        if (Long.valueOf(idLista) != null) {

        }
    }

    public void alterarListaCompra() {
        idLista = listaCompras.get(listView.getCheckedItemPosition()).getId();
        if (Long.valueOf(idLista) != null) {
            irParaCriarLista(idLista);
        }
    }

    public void excluirListaCompra() {
        idLista = listView.getCheckedItemPosition();
        if (Long.valueOf(idLista) != null) {
            Thread t = new Thread(() -> {
                itemDAO.removerPorListaId(listaCompras.get(pos).getId());
            });
            t.start();
            Thread tr = new Thread(() -> {
                listaComprasDAO.remover(listaCompras.get(pos));
            });
            tr.start();
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
                irParaSetor(0);
                break;
            case R.id.setor_2:
                irParaSetor(1);
                break;
            case R.id.setor_3:
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

    private void irParaCriarLista(long idLista) {
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