package com.example.myapplication.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Operation implements Serializable {
    @NonNull
    private List<Integer> value = new ArrayList<>();
    private List<String> operation = new ArrayList<>();

    public Operation() {}

    public int sum(int total, int value) {
        return total + value;
    }

    public int subtraction(int total, int value) {
        return total - value;
    }

    public int division(int total, int value) {
        return total / value;
    }

    public int multiplication(int total, int value) {
        return total * value;
    }

    public void clearListOfValues() {
        this.value.clear();
    }

    public void clearOperations() {
        this.operation.clear();
    }

    public int getValue(int id) {
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

    public void setValue(int number) {
        this.value.add(number);
    }

    public void setOperation(String operation) {
        this.operation.add(operation);
    }
}