package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Operacoes;
import com.example.myapplication.util.Convert;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Operacoes operacoes = new Operacoes();
    private Convert convert = new Convert();
    private List<String> valorImpresso = new ArrayList<>();
    private Button botao;
    private int total = 0;
    private boolean btnResultFoiClicado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IniciaBotoes();

    }

    private void IniciaBotoes() {
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
        setarOperacao(R.id.btn_Multiplicacao, "X");
        setarOperacao(R.id.btn_Divisao, "/");
        realizaFuncao(R.id.btn_InverteSinal, "+/-");
        realizaFuncao(R.id.btn_Resultado, "=");
        realizaFuncao(R.id.btn_Clear, "C");
        realizaFuncao(R.id.btn_ClearEntry, "CE");
    }

    private void setarValor(int idBotao, int valor) {
        botao = findViewById(idBotao);
        botao.setOnClickListener(view -> setaValorImpresso(convert.toStr(valor)));
    }

    private void setarOperacao(int idBotaoOperacao, String operacaoRealizada) {
        botao = findViewById(idBotaoOperacao);
        botao.setOnClickListener(view -> {
            switch (operacaoRealizada) {
                case "+":
                case "-":
                case "X":
                case "/":
                    setaValorAtualNaLista();
                    if (btnResultFoiClicado) {
                        exibeValorEmResultados("");
                        btnResultadoFoiClicado(false);
                    }
                    operacoes.setOperacao(operacaoRealizada);
                    setaValorImpresso(operacaoRealizada);
                    exibeValorEmOperacoes(retornaExpressao());
//                    LimpaTela();
                    break;
            }

        });
    }

    private void realizaFuncao(int idBotaoFuncao, String funcaoRealizada) {
        botao = findViewById(idBotaoFuncao);
        botao.setOnClickListener(
                view -> {
                    switch (funcaoRealizada) {
                        case "C":
                        case "CE":
                            // Por enquanto deixarei a função Clear Entry Igual a Clear,
                            // para futuramente implementar a função corretamente
                            operacoes.limpaLista();
                            LimpaTela();
                            exibeValorEmOperacoes("");
                            exibeValorEmResultados("");
                            break;

                        case "+/-":
                            int valor = operacoes.getValor(operacoes.returnListSize() - 1);
                            if (valor > 0)
                                valor = -valor;
                            else
                                valor = -valor;

//                    valorImpresso = convert.toStr(valor);
                            setaValorAtualNaLista();
                            LimpaTela();
                            exibeValorEmOperacoes(convert.toStr(valor));
                            break;

                        case "=":
                            setaValorAtualNaLista();
                            switch (operacoes.getOperacao()) {
                                case "+":
                                    total = operacoes.soma();
                                    btnResultadoFoiClicado(true);
                                    break;
                                case "-":
                                    total = operacoes.subtracao();
                                    btnResultadoFoiClicado(true);
                                    break;
                                case "X":
                                    total = operacoes.multiplicacao();
                                    btnResultadoFoiClicado(true);
                                    break;
                                case "/":
                                    total = operacoes.divisao();
                                    btnResultadoFoiClicado(true);
                                    break;
                            }

                            exibeValorEmResultados(convert.toStr(total));
                            operacoes.limpaLista();
                            LimpaTela();
                            break;

                    }
                });
    }

    private void setaValorAtualNaLista() {
        String ValorAtual = "";
        for (int i = 0; i < valorImpresso.size(); i++) {
            if (valorImpresso.get(i).equals("+") || // Gambiarra para consertar depois
                    valorImpresso.get(i).equals("-") ||
                    valorImpresso.get(i).equals("/") ||
                    valorImpresso.get(i).equals("X"))
            {
                ValorAtual = "";
            }else{
                ValorAtual += valorImpresso.get(i);

            }
        }
        operacoes.setValor(convert.toInt(ValorAtual));
    }

    private void LimpaTela() {
        valorImpresso.clear();
        exibeValorEmOperacoes("");
    }

    private void btnResultadoFoiClicado(boolean valor) {
        btnResultFoiClicado = valor;
    }

    public String retornaExpressao() {
        String expressao = "";
        for (int i = 0; i < valorImpresso.size(); i++) {
            expressao += valorImpresso.get(i);
        }

        return expressao;
    }

    private void setaValorImpresso(String valor) {
        valorImpresso.add(valor);
        exibeValorEmOperacoes(retornaExpressao());
    }

    private void exibeValorEmOperacoes(String valor) {
        TextView campoMostraOperacoes = findViewById(R.id.txtMostraOperacoes);
        campoMostraOperacoes.setText(valor);
    }


    private void exibeValorEmResultados(String valor) {
        TextView campoMostraResultado = findViewById(R.id.txtMostraResultado);
        campoMostraResultado.setText(valor);
        valorImpresso.clear();

    }
}