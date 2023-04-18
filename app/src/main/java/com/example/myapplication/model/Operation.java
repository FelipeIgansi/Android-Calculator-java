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

    public void updateLastValue(String value) {
        removeLastValue();
        setValue(value);
    }

    public void removeLastValue() {
        this.value.remove(returnSizeOfValue() - 1);
    }

    public void updateOperation(String value) {
        removeLastOperation();
        setOperation(value);
    }

    public void removeLastOperation() {
        this.operation.remove(returnSizeOfOperations() - 1);
    }

    public void setOperation(String operation) {
        this.operation.add(operation);
    }

    public boolean verifyIfOperationIsEmpty() {
        return this.operation.isEmpty();
    }
    public boolean verifyIfListValuesIsEmpty() {
        return this.value.isEmpty();
    }
}
