package com.example.prova_3_dispositivos_moveis;

import static com.example.prova_3_dispositivos_moveis.MainActivity.CRIAR_PRODUTO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.prova_3_dispositivos_moveis.dao.Banco;
import com.example.prova_3_dispositivos_moveis.dao.ProdutoDAO;
import com.example.prova_3_dispositivos_moveis.model.Produto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;
import java.util.List;

public class EditarSetor extends AppCompatActivity {
    ListView lista;
    int pos;
    ArrayAdapter<Produto> adapter;
    ObservadorProduto produtoObs;
    LinkedList<Produto> listaProdutos;

    Banco bd;
    ProdutoDAO produtoDAO;
    class ObservadorProduto implements Observer<List<Produto>> {
        @Override
        public void onChanged(List<Produto> produtos) {
            listaProdutos.clear();
            listaProdutos.addAll(produtos);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int code,
                                 int result, Intent data) {
        super.onActivityResult(code, result, data);
        getProdutosDoSetor(1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setor);
        long setor = (long) getIntent().getSerializableExtra("Setor");
        iniciarBd();
        iniciarListaProdutos();
        getProdutosDoSetor(setor);
        iniciarBotoes();
    }

    private void getProdutosDoSetor(long setor) {
        produtoObs = new ObservadorProduto();
        produtoDAO.listar(setor).observe(this, produtoObs);
    }

    private void iniciarBotoes() {
        FloatingActionButton floatingActionButton = findViewById(R.id.ir_para_criar_produto);
        Button excluir = findViewById(R.id.exluir_produto);
        Button alterar = findViewById(R.id.alterar_produto);

        floatingActionButton.setOnClickListener(view -> {
            irParaCriarProduto(-1);
        });

        excluir.setOnClickListener(view -> {
            excluirProduto();
        });

        alterar.setOnClickListener(view -> {
            alterarProduto();
        });

    }

    private void irParaCriarProduto(long idProduto) {
        Intent intent = new Intent(this, CriarProduto.class);
        if (idProduto != -1)
            intent.putExtra("IdProduto", idProduto);
        startActivityForResult(intent, CRIAR_PRODUTO);
    }

    private void iniciarListaProdutos() {
        listaProdutos = new LinkedList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice,
                listaProdutos);
        lista = findViewById(R.id.lista_produtos_do_setor);
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener((parent, view, position, i) -> pos = position);
        lista.setItemChecked(0, true);
    }

    private void iniciarBd() {
        bd = Room.databaseBuilder(getApplicationContext(), Banco.class, "ListaDeCompras").
                fallbackToDestructiveMigration().build();
        produtoDAO = bd.getProdutoDAO();
    }

    private void excluirProduto() {
        int pos = lista.getCheckedItemPosition();

        Produto p = listaProdutos.get(pos);
        new Thread(() -> {
            produtoDAO.remover( p );
            listaProdutos.remove( p );
            lista.clearChoices();
            runOnUiThread(new Thread(() -> {
                adapter.notifyDataSetChanged();
                lista.setItemChecked(0, true);
            }));
        }).start();
    }

    private void alterarProduto() {
        int pos = lista.getCheckedItemPosition();
        irParaCriarProduto(listaProdutos.get(pos).getId());
    }
}