package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.myapplication.model.operacao;

public class MainActivity extends AppCompatActivity {

    operacao operacao = new operacao();
    TextView campoMostraOperacoes;
    String valorImpresso = "";
    Button botao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setaValor(R.id.btn_Um, "1");
        setaValor(R.id.btn_Dois, "2");
        setaValor(R.id.btn_Tres, "3");
        setaValor(R.id.btn_Quatro, "4");
        setaValor(R.id.btn_Cinco, "5");
        setaValor(R.id.btn_Seis, "6");
        setaValor(R.id.btn_Sete, "7");
        setaValor(R.id.btn_Oito, "8");
        setaValor(R.id.btn_Nove, "9");
        setaOperacao(R.id.btn_Soma, "+");
        setaFuncao(R.id.btn_Clear, "C");

    }

    private void setaFuncao(int idBotaoFuncao, String funcaoRealizada) {
        botao = findViewById(idBotaoFuncao);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (funcaoRealizada == "C"){
                    operacao.setValor("");
                    valorImpresso = "";
                    exibeValorEmOperacoes(valorImpresso);
                }
            }
        });
    }

    private void exibeValorEmOperacoes(String valor){
        valorImpresso += valor;
        campoMostraOperacoes = findViewById(R.id.txtMostraOperacoes);
        campoMostraOperacoes.setText(valorImpresso);
    }
    private void exibeValorEmResultados(int valor){
        TextView campoMostraEmResultados = findViewById(R.id.txtMostraOperacoes);
        campoMostraEmResultados.setText(Integer.toString(valor));
    }


    private void setaOperacao(int idBotaoOperacao, String operacaoRealizada) {
        botao = findViewById(idBotaoOperacao);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exibeValorEmOperacoes(operacaoRealizada);
                operacao.setOperacao(operacaoRealizada);
            }
        });
    }
    private void setaValor(int idBotao, String valor) {
        botao = findViewById(idBotao);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exibeValorEmOperacoes(valor);
                operacao.setValor(valor);
            }
        });
    }
}