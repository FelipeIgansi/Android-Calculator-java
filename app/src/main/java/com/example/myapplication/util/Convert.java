package com.example.myapplication.util;

public class Convert {
    public int StrToInt(String str){
        if(str.equals("")){
            return 0;
        }
        return Integer.parseInt(str);
    }
    public double StrToDouble(String valor){
        return Double.parseDouble(valor);
    }
    public String DoubleToStr(double valor){
        return Double.toString(valor);
    }
    public String IntToStr(int valor){
        return Integer.toString(valor);
    }

}
