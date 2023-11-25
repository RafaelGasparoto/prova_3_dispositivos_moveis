package com.example.prova_3_dispositivos_moveis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.prova_3_dispositivos_moveis.dao.Banco;
import com.example.prova_3_dispositivos_moveis.dao.ListaComprasDAO;
import com.example.prova_3_dispositivos_moveis.dao.ProdutoDAO;
import com.example.prova_3_dispositivos_moveis.model.Produto;

import java.util.List;

public class CriarProduto extends AppCompatActivity {

    Banco bd;
    ProdutoDAO produtoDAO;
    ListaComprasDAO listaComprasDAO;
    Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_produto);
        id = (Long) getIntent().getSerializableExtra("IdProduto");
        iniciarBotao();
        iniciarBd();
        if (id != null)
            getProduto();
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
        produto.setIdSetor(1);

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
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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