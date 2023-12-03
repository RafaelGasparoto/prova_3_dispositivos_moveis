package com.example.prova_3_dispositivos_moveis.telas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prova_3_dispositivos_moveis.R;
import com.example.prova_3_dispositivos_moveis.dao.Banco;
import com.example.prova_3_dispositivos_moveis.dao.ItemDAO;
import com.example.prova_3_dispositivos_moveis.dao.ListaComprasDAO;
import com.example.prova_3_dispositivos_moveis.dao.ProdutoDAO;
import com.example.prova_3_dispositivos_moveis.model.Item;
import com.example.prova_3_dispositivos_moveis.model.ListaCompras;
import com.example.prova_3_dispositivos_moveis.model.Produto;
import com.example.prova_3_dispositivos_moveis.utils.ObservadorListaProdutos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;

public class CriarLista extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ListView lista;
    int pos;
    ArrayAdapter<Produto> adapter;
    ArrayList<Item> listaItens = new ArrayList<Item>();
    LinkedList<Produto> listaProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_lista);
        listaId = (Long) getIntent().getSerializableExtra("IdLista");
        iniciarBd();
        if (listaId != null)
            getListaDeCompras(listaId.intValue());
        iniciarSpinnerSetores();
        iniciarListaProdutos();
        iniciarListenQuantidade();
        iniciarFloatingActionButton();
        getIdLista();
    }

    private void iniciarListenQuantidade() {
        EditText quantidade = findViewById(R.id.quantidade_produto);
        quantidade.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (quantidade.getText().toString().isEmpty()) {
                    salvarQuantidade(0);
                } else {
                    salvarQuantidade(Double.parseDouble(quantidade.getText().toString()));
                }
            }
        });
    }

    private void getIdLista() {

    }

    private void getListaDeCompras(int id) {

    }

    Banco bd;
    ItemDAO itemDAO;
    ProdutoDAO produtoDAO;
    ListaComprasDAO listaDAO;
    ObservadorListaProdutos observadorListaProdutos;
    Long listaId;
    Long idSetor;

    private void iniciarBd() {
        bd = Room.databaseBuilder(getApplicationContext(), Banco.class, "ListaDeCompras").
                fallbackToDestructiveMigration().build();
        itemDAO = bd.getItemDAO();
        produtoDAO = bd.getProdutoDAO();
        listaDAO = bd.getListaComprasDAO();
    }


    private void getProdutosDoSetor(long setor) {
        idSetor = setor;
        observadorListaProdutos = new ObservadorListaProdutos(listaProdutos, adapter);
        produtoDAO.listar(idSetor).observe(this, observadorListaProdutos);
    }

    private void iniciarFloatingActionButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.criarLista);

        floatingActionButton.setOnClickListener(view -> {
            if (checkCriarLista()) {
                confirmar();
            }
        });
    }

    private boolean checkCriarLista() {
        EditText nomeLista = findViewById(R.id.nome_lista);

        if (nomeLista.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Insira um nome para a lista", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else if (listaItens.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Nenhum item inserido na lista", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        return true;
    }

    private void confirmar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_confirmar, null);
        builder.setView(dialogView);

        Button botaoSim = dialogView.findViewById(R.id.botão_sim);
        Button botaoNao = dialogView.findViewById(R.id.botão_não);
        final AlertDialog dialog = builder.create();

        botaoSim.setOnClickListener(v -> {
            criarLista();
            dialog.dismiss();
        });

        botaoNao.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void criarLista() {
        Thread t = new Thread(() -> {
            EditText nomeLista = findViewById(R.id.nome_lista);
            ListaCompras listaCompras = new ListaCompras(nomeLista.getText().toString());
            double valorEstimado = 0;
            for (Item item : listaItens) {
                valorEstimado += item.getQuantidade() * item.getProduto().getPreco();
            }
            listaCompras.setValorEstimado(valorEstimado);
            listaCompras.setPrioridade(0);
            listaId = listaDAO.inserir(listaCompras);
        });
        tryThread(t);
        inserirItens();
    }

    private void inserirItens() {
        Thread t = new Thread(() -> {
            for (Item item : listaItens) {
                item.setListaId(listaId);
                itemDAO.inserir(item);
            }
        });
        tryThread(t);
        finish();
    }

    private void tryThread(Thread t) {
        try {
            t.start();
            t.join();
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void iniciarSpinnerSetores() {
        Spinner spinner = (Spinner) findViewById(R.id.setores_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lista_setores, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
//        spinner.setSelection(0);
    }

    private void iniciarListaProdutos() {
        listaProdutos = new LinkedList<>();

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice,
                listaProdutos);
        lista = findViewById(R.id.lista_produtos);
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener((parent, view, position, i) -> {
            pos = position;
            EditText quantidade = findViewById(R.id.quantidade_produto);
            quantidade.setText("");
        });
        lista.setItemChecked(0, true);
    }

    private void salvarQuantidade(double quantidade) {
        boolean itemInserido = false;
        for (Item item : listaItens) {
            if (item.getProdutoId() == listaProdutos.get(pos).getId()) {
                itemInserido = true;
                item.setQuantidade(quantidade);
            }
        }

        if (itemInserido == false) {
            inserirItem(quantidade);
        }
    }

    private void inserirItem(double quantidade) {
        Item item = new Item();
        item.setComprado(false);
        item.setQuantidade(quantidade);
        item.setProdutoId(listaProdutos.get(pos).getId());
        item.setProduto(listaProdutos.get(pos));
        listaItens.add(item);
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
}