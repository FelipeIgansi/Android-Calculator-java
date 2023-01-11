package com.example.myapplication.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Operacoes implements Serializable {
    @NonNull
    private List<Integer> valor = new ArrayList<>();
    private List<String> operacao = new ArrayList<>();


    public Operacoes() {
    }


    public int soma(int totalAtual, int valorAtual) {
        return totalAtual + valorAtual;
    }

    public int subtracao(int totalAtual, int valorAtual) {
        return totalAtual - valorAtual;
    }

    public int divisao(int totalAtual, int valorAtual) {
        return totalAtual / valorAtual;
    }

    public int multiplicacao(int totalAtual, int valorAtual) {
        return totalAtual * valorAtual;
    }

    public void limpaLista() {
        this.valor.clear();
    }

    public int getValor(int id) {
        return valor.get(id);
    }

    public int returnSizeOfValue() {
        return this.valor.size();
    }

    public int returnSizeOfOperations() {
        return this.operacao.size();
    }

    public String getOperacao(int id) {
        return operacao.get(id);
    }


    public void setValor(int numero) {
        valor.add(numero);
    }

    public void setOperacao(String operacao) {
        this.operacao.add(operacao);
    }

    public int buscaIdPelovalor(String valor) {
        int id = 0;
        for (int i = 0; i < operacao.size(); i++) {
            if (operacao.get(i).equals(valor)) {
                id = i;
            }
        }
        return id;
    }

    public void limpaOperacao() {
        operacao.clear();
    }
}
