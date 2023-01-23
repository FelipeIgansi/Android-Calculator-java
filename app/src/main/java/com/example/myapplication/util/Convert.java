package com.example.myapplication.util;

public class Convert {
    public int toInt(String str){
        if(str.equals("")){
            return 0;
        }
        return Integer.parseInt(str);
    }
    public int DoubleToInt(Double valor)  {
        return (int) Math.abs(valor);
    }
    public String toStr(int valor){
        return Integer.toString(valor);
    }

}
