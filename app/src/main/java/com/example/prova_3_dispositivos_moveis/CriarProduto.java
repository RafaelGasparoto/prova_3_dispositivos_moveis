package com.example.prova_3_dispositivos_moveis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;

import com.example.prova_3_dispositivos_moveis.dao.Banco;
import com.example.prova_3_dispositivos_moveis.dao.ListaComprasDAO;
import com.example.prova_3_dispositivos_moveis.dao.ProdutoDAO;
import com.example.prova_3_dispositivos_moveis.model.Produto;

public class CriarProduto extends AppCompatActivity {

    Banco bd;
    ProdutoDAO produtoDAO;
    ListaComprasDAO listaComprasDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_produto);
        iniciarBotao();
    }

    private void iniciarBotao() {
        Button button = findViewById(R.id.criar_produto);
        button.setOnClickListener(view -> ConstruirProduto());
    }

    private void ConstruirProduto() {
        EditText decricao = findViewById(R.id.descricao_produto);
        EditText preco = findViewById(R.id.preco_produto);
        Produto produto = new Produto();
        produto.setDescricao(decricao.getText().toString());
        produto.setPreco(Double.parseDouble(preco.getText().toString()));
        produto.setIdSetor(1);
        iniciarBd();
        inserirProduto(produto);
    }

    public void inserirProduto(Produto produto) {
        Thread t = new Thread(() -> produtoDAO.inserir(produto));
        t.start();
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


    @Override
    public void onStop() {
        bd.close();
        super.onStop();
    }
}