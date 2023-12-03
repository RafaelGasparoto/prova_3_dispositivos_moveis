package com.example.prova_3_dispositivos_moveis.telas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteProgram;
import androidx.sqlite.db.SupportSQLiteQuery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.prova_3_dispositivos_moveis.R;
import com.example.prova_3_dispositivos_moveis.dao.Banco;
import com.example.prova_3_dispositivos_moveis.dao.ListaComprasDAO;
import com.example.prova_3_dispositivos_moveis.dao.ProdutoDAO;
import com.example.prova_3_dispositivos_moveis.model.Produto;

import java.util.List;

public class CriarProduto extends AppCompatActivity {

    Banco bd;
    ProdutoDAO produtoDAO;
    ListaComprasDAO listaComprasDAO;
    Long id, idSetor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_produto);
        id = (Long) getIntent().getSerializableExtra("IdProduto");
        idSetor = (Long) getIntent().getSerializableExtra("IdSetor");
        iniciarBotao();
        iniciarBd();
        if (id != null)
            getProduto();
        getIdLista();
    }

    class ObservadorProduto implements Observer<Produto> {
        @Override
        public void onChanged(Produto produto) {
            iniciarInformacoesDoProduto(produto);
        }
    }

    ObservadorProduto produtoObs;

    private void getProduto() {
        produtoObs = new ObservadorProduto();
        produtoDAO.produto_por_id(id).observe(this, produtoObs);
    }

    private void iniciarInformacoesDoProduto(Produto produto) {
        EditText descricao = findViewById(R.id.descricao_produto);
        EditText preco = findViewById(R.id.preco_produto);
        TextView id = findViewById(R.id.id_produto);

        descricao.setText(produto.getDescricao());
        preco.setText(String.valueOf(produto.getPreco()));
        id.setText(String.valueOf(produto.getId()));
    }

    private void iniciarBotao() {
        Button button = findViewById(R.id.confirmar_produto);
        button.setOnClickListener(view -> ConstruirProduto());
    }

    private void ConstruirProduto() {
        EditText decricao = findViewById(R.id.descricao_produto);
        EditText preco = findViewById(R.id.preco_produto);
        EditText idProduto = findViewById(R.id.id_produto);

        Produto produto = new Produto();
        produto.setDescricao(decricao.getText().toString());
        produto.setPreco(Double.parseDouble(preco.getText().toString()));
        produto.setIdSetor(idSetor);

        if (id != null) {
            produto.setId(Long.parseLong(idProduto.getText().toString()));
            alterarProduto(produto);
        } else
            inserirProduto(produto);

    }

    private void inserirProduto(Produto produto) {
        Thread t = new Thread(() -> produtoDAO.inserir(produto));
        t.start();
        esperarThread(t);
    }

    private void alterarProduto(Produto produto) {
        Thread t = new Thread(() -> produtoDAO.alterar(produto));
        t.start();
        esperarThread(t);
    }

    private void esperarThread(Thread t) {
        try {
            t.join();
            Intent data = new Intent();
            data.putExtra("produto_alterado", true);
            setResult(RESULT_OK, data);
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getIdLista() {
        SupportSQLiteQuery s = new SupportSQLiteQuery() {
            @Override
            public String getSql() {
                return "select last_insert_rowid()";
            }

            @Override
            public void bindTo(SupportSQLiteProgram statement) {

            }

            @Override
            public int getArgCount() {
                return 0;
            }
        };

       LiveData id = produtoDAO.busrcarIdUltimaLista(s);
        System.out.println(id.getValue());
    }


    private void iniciarBd() {
        bd = Room.databaseBuilder(getApplicationContext(), Banco.class, "ListaDeCompras").
                fallbackToDestructiveMigration().build();
        produtoDAO = bd.getProdutoDAO();
        listaComprasDAO = bd.getListaComprasDAO();
    }


//    @Override
//    public void onStop() {
//        bd.close();
//        super.onStop();
//    }


}