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
import android.widget.Spinner;

import com.example.prova_3_dispositivos_moveis.dao.Banco;
import com.example.prova_3_dispositivos_moveis.dao.ProdutoDAO;
import com.example.prova_3_dispositivos_moveis.model.Produto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;
import java.util.List;

public class EditarSetor extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
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
    Long idSetor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setor);
        idSetor = (Long) getIntent().getSerializableExtra("Setor");
        iniciarBd();
        iniciarListaProdutos();
        iniciarBotoes();
        iniciarSpinnerSetores();
    }

    @Override
    public void onActivityResult(int code,
                                 int result, Intent data) {
        super.onActivityResult(code, result, data);
        if(result == RESULT_OK){
            Boolean resetar_lista = (Boolean) data.getSerializableExtra("produto_alterado");
            if(resetar_lista) {
                idSetor = spinner.getSelectedItemId();
                getProdutosDoSetor(idSetor);
            }
        }
    }
    Spinner spinner;
    private void iniciarSpinnerSetores() {
        spinner = (Spinner) findViewById(R.id.spinner_setores);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lista_setores, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(Math.toIntExact(idSetor));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        switch (text) {
            case "Setor 1":
                getProdutosDoSetor(0);
                break;
            case "Setor 2":
                getProdutosDoSetor(1);
                break;
            case "Setor 3":
                getProdutosDoSetor(2);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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