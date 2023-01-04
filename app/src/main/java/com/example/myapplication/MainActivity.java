package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.myapplication.model.Operacoes;
import com.example.myapplication.util.Convert;

public class MainActivity extends AppCompatActivity {

    Operacoes operacoes = new Operacoes();
    Convert convert = new Convert();
    TextView campoMostraOperacoes;
    TextView campoMostraResultado;
    String valorImpresso = "";
    Button botao;
    int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setarValor(R.id.btn_Um, 1);
        setarValor(R.id.btn_Dois, 2);
        setarValor(R.id.btn_Tres, 3);
        setarValor(R.id.btn_Quatro, 4);
        setarValor(R.id.btn_Cinco, 5);
        setarValor(R.id.btn_Seis, 6);
        setarValor(R.id.btn_Sete, 7);
        setarValor(R.id.btn_Oito, 8);
        setarValor(R.id.btn_Nove, 9);
        setarValor(R.id.btn_Zero, 0);
        setarOperacao(R.id.btn_Soma, "+");
        setarOperacao(R.id.btn_Subtracao, "-");
        setarOperacao(R.id.btn_Multiplicacao, "x");
        setarOperacao(R.id.btn_Divisao, "/");
        setarOperacao(R.id.btn_Resultado, "=");
        setarOperacao(R.id.btn_Clear, "C");

    }

    private void setarOperacao(int idBotaoOperacao, String operacaoRealizada) {
        botao = findViewById(idBotaoOperacao);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (valorImpresso != "")
                    operacoes.setValor(convert.toInt(valorImpresso));
                if (operacaoRealizada == "C"){
                    operacoes.limpaLista();
                    valorImpresso = "";
                    exibeValorEmOperacoes(valorImpresso);
                    exibeValorEmResultados(valorImpresso);
                }
                else if (operacaoRealizada == "+"){
                    operacoes.setOperacao(operacaoRealizada);
                    valorImpresso = "";
                    exibeValorEmOperacoes(valorImpresso);

                }
                else if (operacaoRealizada == "-") {
                    operacoes.setOperacao(operacaoRealizada);
                    valorImpresso = "";
                    exibeValorEmOperacoes(valorImpresso);
                }
                else if (operacaoRealizada == "x") {
                    operacoes.setOperacao(operacaoRealizada);
                    valorImpresso = "";
                    exibeValorEmOperacoes(valorImpresso);
                }
                else if (operacaoRealizada == "/") {
                    operacoes.setOperacao(operacaoRealizada);
                    valorImpresso = "";
                    exibeValorEmOperacoes(valorImpresso);
                }
                else  if(operacaoRealizada == "="){
                    if (operacoes.getOperacao() == "+")
                        total = operacoes.soma();
                    else if (operacoes.getOperacao() == "-")
                        total = operacoes.subtracao();
                    else if (operacoes.getOperacao() == "x")
                        total = operacoes.multiplicacao();
                    else if (operacoes.getOperacao() == "/")
                        total = operacoes.divisao();

                    exibeValorEmResultados(convert.toStr(total));
                    operacoes.limpaLista();
                    valorImpresso = "";
                    exibeValorEmOperacoes(valorImpresso);
                }

            }
        });
    }

    private void setarValor(int idBotao, int valor) {
        botao = findViewById(idBotao);
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exibeValorEmOperacoes(convert.toStr(valor));
            }
        });
    }

    private void exibeValorEmOperacoes(String valor){
        valorImpresso += valor;
        campoMostraOperacoes = findViewById(R.id.txtMostraOperacoes);
        campoMostraOperacoes.setText(valorImpresso);
    }
    private void exibeValorEmResultados(String valor){
        campoMostraResultado = findViewById(R.id.txtMostraResultado);
        campoMostraResultado.setText(valor);
        valorImpresso = "";

    }
}