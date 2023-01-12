package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Operacoes;
import com.example.myapplication.util.Convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final Set<String> listaOperacoes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("+", "-", "/", "X")));
    private final Operacoes operacoes = new Operacoes();
    private final Convert convert = new Convert();
    private final List<String> valorImpresso = new ArrayList<>();
    private Button botao;
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
        realizaFuncao(R.id.btn_CancelEntry, "CE");
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
                    VerificaSeBtnResultadoFoiClicado();
                    operacoes.setOperacao(operacaoRealizada);
                    setaValorImpresso(operacaoRealizada);
                    exibeValorEmOperacoes(retornaExpressao());
                    break;
            }

        });
    }

    private void VerificaSeBtnResultadoFoiClicado() {
        if (btnResultFoiClicado) {
            exibeValorEmResultados("");
            btnResultadoFoiClicado(false);
        }
    }

    private void realizaFuncao(int idBotaoFuncao, String funcaoRealizada) {
        botao = findViewById(idBotaoFuncao);
        botao.setOnClickListener(
                view -> {
                    switch (funcaoRealizada) {
                        case "C":
                            LimpaTela();
                            exibeValorEmOperacoes(retornaExpressao());
                            exibeValorEmResultados(retornaExpressao());
                            break;
                        case "CE":
                            // Por enquanto deixarei a função Cancel Entry Igual a Clear,
                            // para futuramente implementar a função corretamente
                            CancelarEntrada();
                            break;

                        case "+/-":
                            int valor = buscaValorImpresso();
                            valor = InverteSinal(valor);
                            setaValorAtualNaLista();
                            LimpaTela();
                            exibeValorEmOperacoes(convert.toStr(valor));
                            break;

                        case "=":
                            setaValorAtualNaLista();
                            int total = operacoes.getValor(0);
                            int j = 1;
                            for (int i = 0; i < operacoes.returnSizeOfOperations(); i++)
                                label:for (int r = j; r < operacoes.returnSizeOfValue(); r++)
                                    switch (operacoes.getOperacao(i)) {
                                        case "+":
                                            total = operacoes.soma(total, operacoes.getValor(r));
                                            btnResultadoFoiClicado(true);
                                            j = r + 1;
                                            break label;
                                        case "-":
                                            total = operacoes.subtracao(total, operacoes.getValor(r));
                                            btnResultadoFoiClicado(true);
                                            j = r + 1;
                                            break label;
                                        case "X":
                                            total = operacoes.multiplicacao(total, operacoes.getValor(r));
                                            btnResultadoFoiClicado(true);
                                            j = r + 1;
                                            break label;
                                        case "/":
                                            total = operacoes.divisao(total, operacoes.getValor(r));
                                            btnResultadoFoiClicado(true);
                                            j = r + 1;
                                            break label;
                                    }
                            exibeValorEmResultados(convert.toStr(total));
                            LimpaTela();
                            break;

                    }
                });
    }

    private void CancelarEntrada() {
        label: for (int i = valorImpresso.size() - 1; i >= 0; i--) {
            if (listaOperacoes.contains(valorImpresso.get(i))) {
                for (int j = valorImpresso.size() - 1; j >= i; j--) {
                    valorImpresso.remove(j);
//                        operacoes.removePorIDListaValores(j);
                }
                break label;
            } else if (!(listaOperacoes.contains(valorImpresso.get(i))) && i == 0){
                LimpaTela();

            }
        }

        exibeValorEmOperacoes(retornaExpressao());
    }

    private int buscaValorImpresso() {
        if (valorImpresso.size() > 1) {
            String valor = "";
            for (int i = 0; i < valorImpresso.size(); i++) {
                valor += valorImpresso.get(i);
            }
            return convert.toInt(valor);
        } else {
            return convert.toInt(valorImpresso.get(0));
        }
    }

    private int InverteSinal(int valor) {
        return -valor;
    }

    private int getSizeOfValorImpresso() {
        return valorImpresso.size();
    }

    private void setaValorAtualNaLista() {
        String ValorAtual = "";
        for (int i = 0; i < getSizeOfValorImpresso(); i++) {
            if (listaOperacoes.contains(valorImpresso.get(i))) {
                ValorAtual = "";
            } else {
                ValorAtual += valorImpresso.get(i);

            }
        }
        operacoes.setValor(convert.toInt(ValorAtual));
    }

    private void LimpaTela() {
        valorImpresso.clear();
        operacoes.limpaLista();
        operacoes.limpaOperacao();
        exibeValorEmOperacoes("");
    }

    private void btnResultadoFoiClicado(boolean valor) {
        btnResultFoiClicado = valor;
    }

    public String retornaExpressao() {
        String expressao = "";
        for (int i = 0; i < getSizeOfValorImpresso(); i++) {
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
    }
}