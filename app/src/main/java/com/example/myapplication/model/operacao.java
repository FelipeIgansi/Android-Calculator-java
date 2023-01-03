package com.example.myapplication.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class operacao implements Serializable {
    @NonNull
    private String valor = "";
    private String operacao;


    public operacao(){}

    public int soma(int valor){
        int Valor = Integer.parseInt(this.valor);
        return Valor + valor;
    }
    public int subtracao(int valor){
        int Valor = Integer.parseInt(this.valor);
        return Valor - valor;
    }
    public int divisao(int valor){
        int Valor = Integer.parseInt(this.valor);
        return Valor / valor;
    }
    public int multiplicacao(int valor){
        int Valor = Integer.parseInt(this.valor);
        return Valor * valor;
    }

    public  void limpaTela(){
        this.valor ="";
    }


    public String getValor() {
        return valor;
    }

    public String getOperacao() {
        return operacao;
    }


    public void setValor(String valor) {
        this.valor = valor;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }
}
