package com.example.prova_3_dispositivos_moveis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;

public class Setor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setor);
        int setor = (int) getIntent().getSerializableExtra("Setor");
        getProdutosDoSetor(setor);
    }

    private void getProdutosDoSetor(int setor) {

    }
}