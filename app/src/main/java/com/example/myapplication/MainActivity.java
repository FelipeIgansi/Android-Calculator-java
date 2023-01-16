package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Operation;
import com.example.myapplication.util.Convert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final Set<String> listOperations = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("+", "-", "/", "x")));
    private final Operation operations = new Operation();
    private final Convert convert = new Convert();
    private final List<String> valueInScreen = new ArrayList<>();
    private Button button;
    private boolean btnResultClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StartBottons();

    }

    private void StartBottons() {
        setValue(R.id.btn_One, 1);
        setValue(R.id.btn_Two, 2);
        setValue(R.id.btn_Three, 3);
        setValue(R.id.btn_Four, 4);
        setValue(R.id.btn_Five, 5);
        setValue(R.id.btn_Six, 6);
        setValue(R.id.btn_Seven, 7);
        setValue(R.id.btn_Eight, 8);
        setValue(R.id.btn_Nine, 9);
        setValue(R.id.btn_Zero, 0);
        setOperation(R.id.btn_Sum, "+");
        setOperation(R.id.btn_Subtraction, "-");
        setOperation(R.id.btn_Multiplication, "x");
        setOperation(R.id.btn_Division, "/");
        setFunction(R.id.btn_InvertSignal, "+/-");
        setFunction(R.id.btn_Result, "=");
        setFunction(R.id.btn_Clear, "C");
        setFunction(R.id.btn_CancelEntry, "CE");
    }

    private void setValue(int idButton, int value) {
        button = findViewById(idButton);
        button.setOnClickListener(view -> setValueInScreen(convert.toStr(value)));
    }

    private void setOperation(int idButton, String inputOperation) {
        button = findViewById(idButton);
        button.setOnClickListener(view -> {
            switch (inputOperation) {
                case "+":
                case "-":
                case "x":
                case "/":
//                    if (!(getValueInScreen(retornSizeOfValueInScreen() - 1).equals(verifyIfExistsInOperations(inputOperation)))) {
                    setInListValue();
//                    }
                    VerifyIfBtnResultWasClicked();
                    operations.setOperation(inputOperation);
                    setValueInScreen(inputOperation);
                    printInScreenOfOperations(returnExpression());
                    break;
            }

        });
    }

    private String getValueInScreen(int i) {
        return valueInScreen.get(i);
    }

    private boolean verifyIfExistsInOperations(String inputOperation) {
        return listOperations.contains(inputOperation);
    }

    public int retornSizeOfValueInScreen() {
        return valueInScreen.size();
    }

    private void VerifyIfBtnResultWasClicked() {
        if (btnResultClicked) {
            printInScreenOfResults("");
            btnResultWasClicked(false);
        }
    }

    private void setFunction(int idButton, String function) {
        button = findViewById(idButton);
        button.setOnClickListener(
                view -> {
                    switch (function) {
                        case "C":
                            Clear();
                            printInScreenOfOperations(returnExpression());
                            printInScreenOfResults(returnExpression());
                            break;
                        case "CE":
                            // Por enquanto deixarei a função Cancel Entry Igual a Clear,
                            // para futuramente implementar a função corretamente
                            CancelEntry();
                            break;

                        case "+/-":
                            int value = getValueInScreen();
                            value = InvertSignal(value);
                            if (operations.verifyIfValueListIsEmpty()) {
                                operations.setValue(value);
                                updateValueInScreen(convert.toStr(value));
                            }else {
                                operations.updateValue(value);
                                updateValueInScreen(convert.toStr(value));
                            }

//                            Clear();
                            printInScreenOfOperations(convert.toStr(value));
                            break;

                        case "=":
                            setInListValue();
                            int total = operations.getValue(0);
                            int j = 1;
                            for (int i = 0; i < operations.returnSizeOfOperations(); i++)
                                label:for (int r = j; r < operations.returnSizeOfValue(); r++)
                                    switch (operations.getOperation(i)) {
                                        case "+":
                                            total = operations.sum(total, operations.getValue(r));
                                            btnResultWasClicked(true);
                                            j = r + 1;
                                            break label;
                                        case "-":
                                            total = operations.subtraction(total, operations.getValue(r));
                                            btnResultWasClicked(true);
                                            j = r + 1;
                                            break label;
                                        case "x":
                                            total = operations.multiplication(total, operations.getValue(r));
                                            btnResultWasClicked(true);
                                            j = r + 1;
                                            break label;
                                        case "/":
                                            total = operations.division(total, operations.getValue(r));
                                            btnResultWasClicked(true);
                                            j = r + 1;
                                            break label;
                                    }
                            printInScreenOfResults(convert.toStr(total));
                            Clear();
                            break;

                    }
                });
    }

    private void updateValueInScreen(String value) {
        if (valueInScreen.size() == 0) {
            valueInScreen.add(value);
        } else if (valueInScreen.size() > 0) {
            Clear();
            valueInScreen.add(value);

        }
    }

    private void CancelEntry() {
        label:
        for (int i = valueInScreen.size() - 1; i >= 0; i--) {
            if (verifyIfExistsInOperations(getValueInScreen(i))) {
                for (int j = valueInScreen.size() - 1; j >= i; j--) {
                    valueInScreen.remove(j);
                }
                break label;
            } else if (!verifyIfExistsInOperations(getValueInScreen(i)) && i == 0) {
                Clear();
            }
        }
        printInScreenOfOperations(returnExpression());
    }

    private int getValueInScreen() {
        if (valueInScreen.size() > 1) {
            String value = "";
            for (int i = 0; i < valueInScreen.size(); i++) {
                value += getValueInScreen(i);
            }
            return convert.toInt(value);
        } else {
            return convert.toInt(getValueInScreen(0));
        }
    }

    private int InvertSignal(int value) {
        return -value;
    }

    private int getSizeOfValueInScreen() {
        return valueInScreen.size();
    }

    private void setInListValue() {
        String Value = "";
        for (int i = 0; i < getSizeOfValueInScreen(); i++) {
            if (verifyIfExistsInOperations(getValueInScreen(i))) {
                Value = "";
            } else {
                Value += getValueInScreen(i);
            }
        }
        operations.setValue(convert.toInt(Value));
    }

    private void Clear() {
        valueInScreen.clear();
        operations.clearListOfValues();
        operations.clearOperations();
        printInScreenOfOperations("");
    }

    private void btnResultWasClicked(boolean value) {
        btnResultClicked = value;
    }

    public String returnExpression() {
        String expression = "";
        for (int i = 0; i < getSizeOfValueInScreen(); i++) {
            expression += getValueInScreen(i);
        }
        return expression;
    }

    private void setValueInScreen(String value) {
        valueInScreen.add(value);
        printInScreenOfOperations(returnExpression());
    }

    private void printInScreenOfOperations(String value) {
        TextView txtOperations = findViewById(R.id.txtPrintOperations);
        txtOperations.setText(value);
    }

    private void printInScreenOfResults(String value) {
        TextView txtResults = findViewById(R.id.txtPrintResult);
        txtResults.setText(value);
    }
}