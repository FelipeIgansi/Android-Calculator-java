package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exibeNumero(R.id.btn_Um, "1");
        exibeNumero(R.id.btn_Dois, "2");
        exibeNumero(R.id.btn_Tres, "3");
        exibeNumero(R.id.btn_Quatro, "4");
        exibeNumero(R.id.btn_Cinco, "5");
        exibeNumero(R.id.btn_Seis, "6");
        exibeNumero(R.id.btn_Sete, "7");
        exibeNumero(R.id.btn_Oito, "8");
        exibeNumero(R.id.btn_Nove, "9");
        exibeNumero(R.id.btn_Zero, "0");
    }

    private void exibeNumero(int btn_NumUm, String text) {
        Button btnUm = findViewById(btn_NumUm);
        btnUm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView campoMostraOperacoes = findViewById(R.id.txtMostraOperacoes);
                campoMostraOperacoes.setText(text);
            }
        });
    }
}