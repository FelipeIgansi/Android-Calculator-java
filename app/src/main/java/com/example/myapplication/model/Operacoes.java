package com.example.myapplication.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Operacoes implements Serializable {
    @NonNull
    private List<Integer> valor = new ArrayList<>();
    private String operacao;


    public Operacoes(){}


    public int soma(){
        int total= 0;
        for (int i = 0; i < this.valor.size(); i++) {
            total += this.valor.get(i);
        }
        return  total;
    }
    public int subtracao(){
        int total= 0;
        for (int i = 0; i < (this.valor.size()-1); i++) {
            if(this.valor.get(i) < this.valor.get(i+1)) {
                total = this.valor.get(i+1) - this.valor.get(i);
            }
            total = this.valor.get(i) - this.valor.get(i + 1);

        }
        return  total;
    }
    public int divisao(){
        int total= 0;
        for (int i = 0; i < (this.valor.size() - 1); i++) {
            total = this.valor.get(i)/this.valor.get(i+1);
        }
        return  total;
    }
    public int multiplicacao(){
        int total= 0;
        for (int i = 0; i < (this.valor.size() - 1); i++) {
            total = this.valor.get(i)*this.valor.get(i+1);
        }
        return  total;
    }

    public  void limpaLista(){
        this.valor.clear();
    }

    public int getValor(int id) {
        return valor.get(id);
    }

    public String getOperacao() {
        return operacao;
    }


    public void setValor(int numero) {
        valor.add(numero);
    }
    public int getListSize(){
        return this.valor.size();
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }
}
