package com.example.myapplication.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Operation implements Serializable {
    @NonNull
    private final List<String> value = new ArrayList<>();
    private final List<String> operation = new ArrayList<>();

    public Operation() {
    }

    public double sum(double total, double value) {
        return total + value;
    }

    public double subtraction(double total, double value) {
        return total - value;
    }

    public double division(double total, double value) {
        return total / value;
    }

    public double multiplication(double total, double value) {
        return total * value;
    }

    public void clearListOfValues() {
        this.value.clear();
    }

    public void clearOperations() {
        this.operation.clear();
    }

    public String getValue(int id) {
        return this.value.get(id);
    }

    public int returnSizeOfValue() {
        return this.value.size();
    }

    public int returnSizeOfOperations() {
        return this.operation.size();
    }

    public String getOperation(int id) {
        return this.operation.get(id);
    }

    public void setValue(String number) {
        this.value.add(number);
    }

    public void updateValue(String value) {
        this.value.remove(returnSizeOfValue() - 1);
        this.value.add(value);
    }

    public void updateOperation(String value) {
        this.operation.remove(returnSizeOfOperations() - 1);
        this.operation.add(value);

    }


    public void setOperation(String operation) {
        this.operation.add(operation);
    }

    public boolean isListValueEmpty() {
        return this.value.isEmpty();
    }
}
