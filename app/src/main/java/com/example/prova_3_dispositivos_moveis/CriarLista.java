package com.example.prova_3_dispositivos_moveis;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.prova_3_dispositivos_moveis.model.ListaCompras;
import com.example.prova_3_dispositivos_moveis.model.Produto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;

public class CriarLista extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ListView lista;
    int pos;
    ArrayAdapter<Produto> adapter;
    LinkedList<Produto> listaProdutos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_lista);
        Integer idLista = (Integer) getIntent().getSerializableExtra("IdLista");
        if (idLista != null)
            getListaDeCompras(idLista.intValue());
        iniciarSpinnerSetores();
        iniciarListaProdutos();
        iniciarFloatingActionButton();
    }

    private void getListaDeCompras(int id) {

    }

    private void iniciarFloatingActionButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.criarLista);

        floatingActionButton.setOnClickListener(view -> {
            confirmar();
        });
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

    }


    private void iniciarSpinnerSetores() {
        Spinner spinner = (Spinner) findViewById(R.id.setores_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lista_setores, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void iniciarListaProdutos() {
        listaProdutos = new LinkedList<>();
        listaProdutos.add(new Produto());
        listaProdutos.add(new Produto());
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listaProdutos);
        lista = findViewById(R.id.lista_produtos);
        lista.setAdapter(adapter);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice,
                listaProdutos);
        lista = findViewById(R.id.listas_compras);
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long i) {
                pos = position;
            }
        });
        lista.setItemChecked(0, true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        switch (text) {
            case "Setor 1":
                break;
            case "Setor 2":
                break;
            case "Setor 3":
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}