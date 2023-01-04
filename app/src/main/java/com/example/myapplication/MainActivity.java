package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
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
        botao.setOnClickListener(view -> {
            if (!valorImpresso.equals(""))
                operacoes.setValor(convert.toInt(valorImpresso));
            switch (operacaoRealizada) {
                case "C":
                    operacoes.limpaLista();
                    valorImpresso = "";
                    exibeValorEmOperacoes(valorImpresso);
                    exibeValorEmResultados(valorImpresso);
                    break;
                case "+":
                case "-":
                case "x":
                case "/":
                    operacoes.setOperacao(operacaoRealizada);
                    valorImpresso = "";
                    exibeValorEmOperacoes(valorImpresso);
                    break;
                case "=":
                    switch (operacoes.getOperacao()) {
                        case "+":
                            total = operacoes.soma();
                            break;
                        case "-":
                            total = operacoes.subtracao();
                            break;
                        case "x":
                            total = operacoes.multiplicacao();
                            break;
                        case "/":
                            total = operacoes.divisao();
                            break;
                    }

                    exibeValorEmResultados(convert.toStr(total));
                    operacoes.limpaLista();
                    valorImpresso = "";
                    exibeValorEmOperacoes(valorImpresso);
                    break;
            }

        });
    }

    private void setarValor(int idBotao, int valor) {
        botao = findViewById(idBotao);
        botao.setOnClickListener(view -> exibeValorEmOperacoes(convert.toStr(valor)));
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