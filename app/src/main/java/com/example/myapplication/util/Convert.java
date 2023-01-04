package com.example.myapplication.util;

public class Convert {
    public int toInt(String str){
        if(str ==""){
            return 0;
        }
        return Integer.parseInt(str);
    }
    public String toStr(int valor){
        return Integer.toString(valor);
    }

}
