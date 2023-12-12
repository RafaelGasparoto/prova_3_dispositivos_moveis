package com.example.prova_3_dispositivos_moveis.telas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CriarLista extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ListView lista;
    int pos;
    ArrayAdapter<Produto> adapter;
    List<Item> listaItens;
    LinkedList<Produto> listaProdutos;
    Banco bd;
    ItemDAO itemDAO;
    ProdutoDAO produtoDAO;
    ListaComprasDAO listaDAO;
    ObservadorListaProdutos observadorListaProdutos;
    ObservadorItens observadorListaItens;
    ListaCompras listaCompras;
    ObservadorListaCompras listaCompraObs;
    Long listaId;
    Long idSetor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_lista);
        listaItens = new ArrayList<>();
        listaId = (Long) getIntent().getSerializableExtra("IdLista");
        iniciarBd();
        iniciarSpinnerSetores();
        iniciarListaProdutos();
        iniciarListenQuantidade();
        iniciarFloatingActionButton();
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

    private void iniciarSpinnerSetores() {
        Spinner spinner = findViewById(R.id.setores_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lista_setores, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void iniciarFloatingActionButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.criarLista);

        floatingActionButton.setOnClickListener(view -> {
            if (checkCriarLista()) {
                confirmar();
            }
        });
    }

    private void iniciarBd() {
        bd = Room.databaseBuilder(getApplicationContext(), Banco.class, "ListaDeCompras").
                fallbackToDestructiveMigration().build();
        itemDAO = bd.getItemDAO();
        produtoDAO = bd.getProdutoDAO();
        listaDAO = bd.getListaComprasDAO();
    }


    private void getProdutosDoSetor(long setor) {
        idSetor = setor;
        observadorListaProdutos = new ObservadorListaProdutos(listaProdutos, adapter, () -> getListaDeCompras());
        produtoDAO.listar(idSetor).observe(this, observadorListaProdutos);
    }

    boolean first = true;

    private void getListaDeCompras() {
        if (listaId != null && first) {
            listaCompraObs = new ObservadorListaCompras();
            listaDAO.buscarPorListaCompras(listaId).observe(this, listaCompraObs);
            observadorListaItens = new ObservadorItens();
            itemDAO.listar(listaId).observe(this, observadorListaItens);
            first = false;
        }
        lista.setItemChecked(0, true);
        setQuantidadeEditText();
    }

    public void setProdutosItens() {
        for (int pos = 0; listaItens.size() > pos; pos++) {
            produtoDAO.produto_por_id(listaItens.get(pos).getProdutoId()).observe(this, new ObservadorProduto(pos));
        }
    }

    List<Item> removerListaItens = new ArrayList<>();

    private void setQuantidadeEditText() {
        EditText quantidade = findViewById(R.id.quantidade_produto);
        boolean inserido = false;

        for (Item item : listaItens) {

            if (!listaProdutos.isEmpty() && item.getProdutoId() == listaProdutos.get(pos).getId()) {
                quantidade.setText(String.valueOf(item.getQuantidade()));
                inserido = true;
            }
        }

        if (!inserido) {
            quantidade.setText("");
        }
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
            double valorEstimado = 0;
            for (Item item : listaItens) {
                Produto produto = item.getProduto();
                valorEstimado += item.getQuantidade() * produto.getPreco();
            }

            if (listaId == null) {
                ListaCompras listaCompras = new ListaCompras(nomeLista.getText().toString());
                listaCompras.setValorEstimado(valorEstimado);
                listaCompras.setPrioridade(0);
                listaId = listaDAO.inserir(listaCompras);
            } else {
                this.listaCompras.setNome(nomeLista.getText().toString());
                this.listaCompras.setValorEstimado(valorEstimado);
                listaDAO.alterar(this.listaCompras);
            }
        });
        tryThread(t);
        inserirItens();
    }

    private void inserirItens() {
        Thread t = new Thread(() -> {
            for (Item item : listaItens) {
                if (Long.valueOf(item.getListaId()) == -1) {
                    item.setListaId(listaId);
                    itemDAO.inserir(item);
                } else {
                    itemDAO.alterar(item);
                }
            }
        });

        Thread tr = new Thread(() -> {
            for (Item item : removerListaItens) {
                if (item.getListaId() == listaId)
                    itemDAO.remover(item);
            }
        });

        tryThread(t);
        tryThread(tr);
        Intent intent = new Intent();
        intent.putExtra("lista_produtos_alterado", true);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void tryThread(Thread t) {
        try {
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
            setQuantidadeEditText();
        });
        lista.setItemChecked(0, true);
    }

    private void salvarQuantidade(double quantidade) {
        if (listaProdutos.isEmpty())
            return;
        Item itemAlterado = null;
        for (Item item : listaItens) {
            if (item.getProdutoId() == listaProdutos.get(pos).getId()) {
                item.setQuantidade(quantidade);
                if (quantidade == 0) {
                    removerListaItens.add(item);
                }
                itemAlterado = item;
            }
        }

        if (quantidade == 0 && itemAlterado != null) {
            int pos = removerListaItens.indexOf(itemAlterado);
            if (pos != -1) {
                listaItens.remove(removerListaItens.get(pos));
            }
        } else if (quantidade != 0 && itemAlterado != null) {
            int pos = removerListaItens.indexOf(itemAlterado);
            if (pos != -1) {
                listaItens.add(removerListaItens.get(pos));
                removerListaItens.remove(itemAlterado);
            }
        } else if (quantidade != 0) {
            for (Item item : removerListaItens) {
                if (listaProdutos.get(pos).getId() == item.getProdutoId()) {
                    item.setQuantidade(quantidade);
                    itemAlterado = item;
                    listaItens.add(item);
                }
            }
            if (itemAlterado != null) {
                removerListaItens.remove(itemAlterado);
            } else {
                inserirItem(quantidade);
            }
        }
    }

    private void inserirItem(double quantidade) {
        Item item = new Item();
        item.setComprado(false);
        item.setQuantidade(quantidade);
        item.setProdutoId(listaProdutos.get(pos).getId());
        item.setProduto(listaProdutos.get(pos));
        item.setListaId(-1);
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

    class ObservadorItens implements Observer<List<Item>> {
        @Override
        public void onChanged(List<Item> itens) {
            listaItens = itens;
            setQuantidadeEditText();
            setProdutosItens();
        }
    }

    class ObservadorListaCompras implements Observer<ListaCompras> {
        @Override
        public void onChanged(ListaCompras listaCompra) {
            listaCompras = listaCompra;
            EditText nomeLista = findViewById(R.id.nome_lista);
            nomeLista.setText(listaCompras.getNome());
        }
    }

    class ObservadorProduto implements Observer<Produto> {
        int pos;

        public ObservadorProduto(int pos) {
            this.pos = pos;
        }

        @Override
        public void onChanged(Produto produto) {
            listaItens.get(pos).setProduto(produto);
        }
    }
}
