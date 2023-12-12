package com.example.prova_3_dispositivos_moveis.telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.prova_3_dispositivos_moveis.R;
import com.example.prova_3_dispositivos_moveis.dao.Banco;
import com.example.prova_3_dispositivos_moveis.dao.ItemDAO;
import com.example.prova_3_dispositivos_moveis.dao.ListaComprasDAO;
import com.example.prova_3_dispositivos_moveis.dao.ProdutoDAO;
import com.example.prova_3_dispositivos_moveis.model.Item;
import com.example.prova_3_dispositivos_moveis.model.ListaCompras;
import com.example.prova_3_dispositivos_moveis.model.Produto;
import com.example.prova_3_dispositivos_moveis.utils.ObservadorListaProdutos;

import java.util.LinkedList;
import java.util.List;

public class RealizarCompra extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    class ObservadorListaProdutos implements Observer<Produto> {
        @Override
        public void onChanged(Produto produto) {
            if (produto != null) {
                listaProdutosParaComprar.add(produto);
                adapter.notifyDataSetChanged();
                EditText quantidade = findViewById(R.id.quantidade_comprar);
                quantidade.setText("");
                listaProdutos.setItemChecked(0, true);
                setQuantidade();
            }
        }
    }

    void iniciarListaProdutosAComprar() {
//        listaProdutosParaComprar = new LinkedList<>();

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice,
                listaProdutosParaComprar);
        listaProdutos = findViewById(R.id.lista_produtos);
        listaProdutos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listaProdutos.setAdapter(adapter);

        listaProdutos.setOnItemClickListener((parent, view, position, i) -> {
            salvarQuantidade();
            pos = position;
            setQuantidade();
        });

        listaProdutos.setItemChecked(0, true);
    }

    class ObservadorListaCompras implements Observer<ListaCompras> {
        @Override
        public void onChanged(ListaCompras listaCompra) {
            listaCompras = listaCompra;
            EditText nomeLista = findViewById(R.id.nome_lista);
            nomeLista.setText(listaCompras.getNome());
        }
    }

    class ObservadorListaItens implements Observer<List<Item>> {
        @Override
        public void onChanged(List<Item> lista) {
            listaItens = lista;
            listaItens.forEach(item -> item.setQuantidadeFaltante(item.getQuantidade()));
            getProdutosDoSetor(0);
        }
    }

    long listaId;

    int pos = 0;
    Banco bd;
    ItemDAO itemDAO;
    ArrayAdapter<Produto> adapter;
    ProdutoDAO produtoDAO;
    ListaComprasDAO listaComprasDAO;
    ObservadorListaCompras listaComprasObs;
    ObservadorListaItens listaItensObs;
    ObservadorListaProdutos listaProdutosObs;
    ListaCompras listaCompras;
    long idSetor = 0;
    List<Item> listaItens;
    ListView listaProdutos;
    LinkedList<Produto> listaProdutosParaComprar = new LinkedList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_compra);
        listaId = (Long) getIntent().getSerializableExtra("IdLista");
        iniciarSpinnerSetores();
        iniciarBd();
        iniciarListaProdutosAComprar();
        iniciarListaCompras();
        iniciarListaItens();
        iniciarListenQuantidade();
    }

    private void iniciarSpinnerSetores() {
        Spinner spinner = findViewById(R.id.setores_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lista_setores, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void iniciarListenQuantidade() {
        EditText quantidade = findViewById(R.id.quantidade_comprar);
        quantidade.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                setQuantidadeRestante();
            }
        });
    }

    void iniciarListaItens() {
        listaItensObs = new ObservadorListaItens();
        itemDAO.listar(listaId).observe(this, listaItensObs);
    }

    void iniciarListaCompras() {
        listaComprasObs = new ObservadorListaCompras();
        listaComprasDAO.buscarPorListaCompras(listaId).observe(this, listaComprasObs);
    }

    private void iniciarBd() {
        bd = Room.databaseBuilder(getApplicationContext(), Banco.class, "ListaDeCompras").
                fallbackToDestructiveMigration().build();
        itemDAO = bd.getItemDAO();
        produtoDAO = bd.getProdutoDAO();
        listaComprasDAO = bd.getListaComprasDAO();
    }

    private void salvarQuantidade() {
        EditText quantidade = findViewById(R.id.quantidade_comprar);

        for (Item item : listaItens) {
            if (!listaProdutosParaComprar.isEmpty() && item.getProdutoId() == listaProdutosParaComprar.get(pos).getId()) {
                double q = Double.parseDouble(quantidade.getText().toString().isEmpty() ? "0" : quantidade.getText().toString());
                item.setQuantidade(q);
            }
        }

    }

    private void setQuantidadeRestante() {
        EditText quantidade = findViewById(R.id.quantidade_comprar);
        EditText quantidadeF = findViewById(R.id.quantidade_faltante);

        for (Item item : listaItens) {

            if (!listaProdutosParaComprar.isEmpty() && item.getProdutoId() == listaProdutosParaComprar.get(pos).getId()) {
                double quantidadecomprar = quantidade.getText().toString().isEmpty() ? 0 : Double.parseDouble(quantidade.getText().toString());
                quantidadecomprar = item.getQuantidadeFaltante() - quantidadecomprar;
                quantidadeF.setText(String.valueOf(quantidadecomprar > 0 ? quantidadecomprar : 0));
            }
        }
    }

    private void setQuantidade() {
        EditText quantidade = findViewById(R.id.quantidade_comprar);
        for (Item item : listaItens) {
            if (!listaProdutosParaComprar.isEmpty() && item.getProdutoId() == listaProdutosParaComprar.get(pos).getId()) {
                quantidade.setText(String.valueOf(item.getQuantidade()));
            }
        }
    }

    boolean first = true;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (first) {
            first = false;
            return;
        }
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
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getProdutosDoSetor(long setor) {
        idSetor = setor;
        listaProdutosObs = new ObservadorListaProdutos();
        listaProdutosParaComprar.clear();
        adapter.notifyDataSetChanged();
        for (Item item : listaItens) {
            produtoDAO.listarProdutosComprar(idSetor, item.getProdutoId()).observe(this, listaProdutosObs);
        }
    }

}