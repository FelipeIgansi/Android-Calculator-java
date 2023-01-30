package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Operation;
import com.example.myapplication.util.Convert;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
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
                Pattern.quote("%"),
                Pattern.quote("="))));
    }

    private final Operation operations = new Operation();
    private final Convert convert = new Convert();
    public String valueInScreen = "";
    private Button button;
    private boolean btnResultClicked = false;
    private Locale localeBR = new Locale("pt", "br");

    private NumberFormat numberFormat = NumberFormat.getNumberInstance(localeBR);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StartButtons();
    }

    private void StartButtons() {
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
        button.setOnClickListener(view -> setValueInScreen(convert.IntToStr(value)));
    }

    private void setOperation(int idButton, String inputOperation) {
        button = findViewById(idButton);
        button.setOnClickListener(view -> {
            switch (inputOperation) {
                case "+":
                case "-":
                case "x":
                case "/":
                    VerifyIfBtnResultWasClicked();
                    setValueInScreen(inputOperation);
                    if (listOperations.contains(Pattern.quote(retornLastCaracter_ListValue()))) {
                        operations.updateOperation(inputOperation);
                    }else {
                        operations.setOperation(inputOperation);

                    }
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
                            if (getSize_ListOperations() >= 2) {
                                switch (getValue_ListOperations(0)) {
                                    case "+":
                                    case "-":
                                        totalPercent = convert.StrToInt(getValue_ListValue(getSize_ListValue() - 2)) * getPercent();
                                        break;
                                    case "x":
                                    case "/":
                                        totalPercent = getPercent();
                                        break;
                                }
                                CancelEntry();
                                valueInScreen += totalPercent;
                            } else {
                                valueInScreen = "0";
                            }
                            printInScreenOfOperations(returnExpression());
                            break;

                        case "+/-":
                            setInListValue();
                            int value = convert.StrToInt(getValue_ListValue(getSize_ListValue()));
                            value = InvertSignal(value);
                            if (operations.verifyIfValueListIsEmpty()) {
                                operations.setValue(convert.IntToStr(value));
                                updateValueInScreen(convert.IntToStr(value));
                            } else {
                                operations.updateValue(convert.IntToStr(value));
                                updateValueInScreen(convert.IntToStr(value));
                            }

                            printInScreenOfOperations(convert.IntToStr(value));
                            break;

                        case "=":
                            setInListValue();
                            Double total = convert.StrToDouble(getValue_ListValue(0));
                            int j = 1;
                            for (int i = 0; i < operations.returnSizeOfOperations(); i++)
                                label:for (int r = j; r < getSize_ListOperations(); r++)
                                    switch (getValue_ListOperations(i)) {
                                        case "+":
                                            total = operations.sum(total, convert.StrToDouble(getValue_ListValue(r)));
                                            btnResultWasClicked(true);
                                            j = r + 1;
                                            break label;
                                        case "-":
                                            total = operations.subtraction(total, convert.StrToDouble(getValue_ListValue(r)));
                                            btnResultWasClicked(true);
                                            j = r + 1;
                                            break label;
                                        case "x":
                                            total = operations.multiplication(total, convert.StrToDouble(getValue_ListValue(r)));
                                            btnResultWasClicked(true);
                                            j = r + 1;
                                            break label;
                                        case "/":
                                            total = operations.division(total, convert.StrToDouble(getValue_ListValue(r)));
                                            btnResultWasClicked(true);
                                            j = r + 1;
                                            break label;
                                    }
                            if (!(total < 0))
                                printInScreenOfResults(formatValue(convert.IntToStr(total.intValue())));
                            else
                                printInScreenOfResults(convert.DoubleToStr(total));
                            Clear();
                            break;

                    }
                });
    }

    private double getPercent() {
        return convert.StrToDouble(getValue_ListValue(getSize_ListValue() - 1)) / 100;
    }

    private String getValue_ListOperations(int id) {
        return operations.getOperation(id);
    }

    private int getSize_ListOperations() {
        return operations.returnSizeOfValue();
    }

    private String getValue_ListValue(int id) {
        return operations.getValue(id);
    }

    private int getSize_ListValue() {
        return getSize_ListOperations();
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
            for (int i = valueInScreen.length() - 1; i > 0; i--) {
                char a = valueInScreen.charAt(i);
                System.out.println(a);
                if ((listOperations.contains(Pattern.quote(Character.toString(valueInScreen.charAt(i)))))) {
                    valueInScreen = (valueInScreen.substring(0, i + 1));
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
            operations.setValue(value);
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

    private String retornLastCaracter_ListValue(){
        return Character.toString(valueInScreen.charAt(valueInScreen.length()-1));
    }
    private void setValueInScreen(String value) {
        if (listOperations.contains(Pattern.quote(value)) && !(valueInScreen.equals(""))) {
            if (isLastElementAOperator()) {
                replaceLastValueInScreenIfIsAOperator(value);
            } else {
                valueInScreen += value;
            }
            printInScreenOfOperations(returnExpression());
        } else {
            if (!(listOperations.contains(Pattern.quote(value)))) {
                valueInScreen += value;
                String[] listValues = splitValueInScreen();
                printInScreenOfOperations(formatValue(listValues[listValues.length-1]));
            }
        }

 /*       if (listValues.length - 1 > 18) {
            Clear();
        }*/

    }

    public void replaceLastValueInScreenIfIsAOperator(String value) {
        valueInScreen = valueInScreen.substring(0, valueInScreen.length() - 1) + value;
    }

    private boolean isLastElementAOperator() {
        return listOperations.contains(Pattern.quote(Character.toString(valueInScreen.charAt(valueInScreen.length() - 1))));
    }

    public String formatValue(String value) {
        return numberFormat.format(convert.StrToLong(value));
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