package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Operation;
import com.example.myapplication.util.Convert;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final Set<String> listOperations;

    static {
        listOperations = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                Pattern.quote("+"),
                Pattern.quote("-"),
                Pattern.quote("/"),
                Pattern.quote("x"),
                "%")));
    }

    private final Operation operations = new Operation();
    private final Convert convert = new Convert();
    public String valueInScreen = "";
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
        setFunction(R.id.btn_Percent, "%");
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
                    setValueInScreen(inputOperation);
                    VerifyIfBtnResultWasClicked();
                    operations.setOperation(inputOperation);
                    printInScreenOfOperations(returnExpression());
                    break;
            }

        });
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
                            CancelEntry();
                            printInScreenOfOperations(returnExpression());
                            break;

                        case "%":
                            setInListValue();
                            double totalPercent = 0;
                            if (operations.returnSizeOfValue() >= 2) {
                                switch (operations.getOperation(0)){
                                    case "+":
                                    case "-":
                                        double a1 = operations.getValue(1);
                                        double a2 = (a1 /100);
                                        totalPercent = (operations.getValue(0) * (a2));
                                        totalPercent = (int) totalPercent;
                                        break;
                                    case "x":
                                    case "/":
                                        a1 = operations.getValue(1);
                                        a2 = (a1 /100);
                                        totalPercent = (a2);
                                        break;
                                }
                                CancelEntry();
                                valueInScreen += totalPercent;
                            }
                            else {
                                valueInScreen = "0";
                            }
                            printInScreenOfOperations(returnExpression());
                            break;

                        case "+/-":
                            setInListValue();
                            int value = operations.getValue(operations.returnSizeOfValue() - 1);
                            value = InvertSignal(value);
                            if (operations.verifyIfValueListIsEmpty()) {
                                operations.setValue(value);
                                updateValueInScreen(convert.toStr(value));
                            } else {
                                operations.updateValue(value);
                                updateValueInScreen(convert.toStr(value));
                            }

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
                            valueInScreen = "0";
//                            Clear();
                            break;

                    }
                });
    }

    private void VerifyIfBtnResultWasClicked() {
        if (btnResultClicked) {
            printInScreenOfResults("");
            btnResultWasClicked(false);
        }
    }

    private void updateValueInScreen(String value) {
        String[] Value = splitValueInScreen();
        if (Value.length != 0) {
            Clear();
        }
        valueInScreen += value;
    }

    private void CancelEntry() {
        if (!valueInScreen.isEmpty()) {
            for (int i = valueInScreen.length()-1; i > 0; i--) {
                char a = valueInScreen.charAt(i);
                System.out.println(a);
                if ((listOperations.contains(Pattern.quote(Character.toString(valueInScreen.charAt(i)))))){
                    valueInScreen = (valueInScreen.substring(0, i+1));
                    break;
                }
            }
        }
    }

    private void Clear() {
        valueInScreen = "";
        operations.clearListOfValues();
        operations.clearOperations();
        printInScreenOfOperations("");
    }

    private int InvertSignal(int value) {
        return -value;
    }

    private void setInListValue() {
        String[] values = splitValueInScreen();
        operations.clearListOfValues();
        for (String value : values) {
            operations.setValue(convert.toInt(value));
        }

    }

    @NonNull
    private String[] splitValueInScreen() {
        String[] values;
        values = valueInScreen.split(String.valueOf(listOperations));
        return values;
    }


    private void btnResultWasClicked(boolean value) {
        btnResultClicked = value;
    }

    public String returnExpression() {
        return valueInScreen;
    }

    private void setValueInScreen(String value) {
        valueInScreen += value;
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