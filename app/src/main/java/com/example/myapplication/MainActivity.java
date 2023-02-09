package com.example.myapplication;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Operation;
import com.example.myapplication.util.Convert;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final Set<String> listOperations;

    static {
        listOperations = Set.of(
                Pattern.quote("+"),
                Pattern.quote("-"),
                Pattern.quote("/"),
                Pattern.quote("x"),
                Pattern.quote("%"),
                Pattern.quote("="));
    }

    private MediaPlayer media;

    private final Operation operations = new Operation();
    private final Convert convert = new Convert();
    public String valuesInScreen = "";
    private Button button;
    private boolean btnResultClicked = false;
    private final Locale localeBR = new Locale("pt", "br");

    private final NumberFormat numberFormat = NumberFormat.getNumberInstance(localeBR);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        media = MediaPlayer.create(this, R.raw.feedback_sound_click);
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
        setFunction(R.id.btn_Comma, ",");
        setFunction(R.id.btn_Percent, "%");
        setFunction(R.id.btn_InvertSignal, "+/-");
        setFunction(R.id.btn_Result, "=");
        setFunction(R.id.btn_Clear, "C");
        setFunction(R.id.btn_CancelEntry, "CE");
    }

    private void setValue(int idButton, int value) {
        button = findViewById(idButton);
        button.setOnClickListener(view -> setValuesInScreen(convert.IntToStr(value)));
    }

    private void setOperation(int idButton, String inputOperation) {
        button = findViewById(idButton);
        button.setOnClickListener(view -> {
            switch (inputOperation) {
                case "+":
                case "-":
                case "x":
                case "/":
                    if (!(valuesInScreen.equals(""))) {
                        VerifyIfBtnResultWasClicked();
                        if (listOperations.contains(Pattern.quote(retornLastCaracter_ListValue()))) {
                            operations.updateOperation(inputOperation);
                        } else {
                            operations.setOperation(inputOperation);
                        }
                        setValuesInScreen(inputOperation);
                        printInScreenOfOperations(returnExpression());
                        break;
                    }
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
                            printInScreenOfOperations(valuesInScreen.replace(".", ","));
                            printInScreenOfResults("");
                            break;
                        case ",":
                            String[] values = splitValues();
                            if (!(haveComma(values[values.length - 1]))) {
                                if (valuesInScreen.equals("")) {
                                    valuesInScreen = "0.";
                                } else {
                                    valuesInScreen += ".";
                                }
                                printInScreenOfOperations(valuesInScreen.replace(".", ","));
                                printInScreenOfResults(valuesInScreen.replace(".", ","));
                            }
                            break;
                        case "%":
                            if (!(valuesInScreen.equals(""))) {
                                setInListValue();
                                String[] Values = splitValues();
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
                                    valuesInScreen += totalPercent;
                                } else {
                                    valuesInScreen = "0";
                                }
                                printInScreenOfOperations(returnExpression());
                                printInScreenOfResults(Values[Values.length - 1] + "%");
                                break;
                            }

                        case "+/-":
                            if (!(valuesInScreen.equals(""))) {
                                if (operations.verifyIfListValuesIsEmpty()) {
                                    setInListValue();
                                }
                                int lastValue = convert.StrToInt(operations.getValue(operations.returnSizeOfValue() - 1));
                                String valueInput;
                                int valueInverted = 0;
                                if (!(operations.verifyIfOperationIsEmpty())) {
                                    switch (witchIsTheLastOperator()) {
                                        case "-": case "+": case "x": case "/":
                                                valueInverted = InvertSignal(lastValue);
                                                valueInput = valuesInScreen.substring(0, idLastOperator() + 1);
                                                operations.updateValue(convert.IntToStr(valueInverted));
                                                printInScreenOfOperations(valueInput);
                                                break;
                                    }
                                } else {
                                    valueInverted = InvertSignal(lastValue);
                                    operations.updateValue(convert.IntToStr(valueInverted));
                                    updateValueInScreen(convert.IntToStr(valueInverted));
                                    printInScreenOfOperations(convert.IntToStr(valueInverted));
                                }
                                printInScreenOfResults(convert.IntToStr(valueInverted));
                            }
                            break;


                        case "=":
                            if (!(valuesInScreen.equals(""))) {
                                setInListValue();
                                Double total = convert.StrToDouble(getValue_ListValue(0));
                                total = getTotal(total);
                                if (haveComma(convert.DoubleToStr(total))) {
                                    printInScreenOfResults(convert.DoubleToStr(total));
                                } else {
                                    printInScreenOfResults(formatValue(convert.IntToStr(total.intValue())));
                                }
                                btnResultWasClicked(true);
                                valuesInScreen = "";
                                operations.clearOperations();
                                operations.clearListOfValues();
                                break;
                            }
                    }
                });
    }

    private String witchIsTheLastOperator() {
        String[] valuesInScreenDivided = splitValues("");
        for (int i = (valuesInScreenDivided.length - 1); i >= 0; i--) {
            if (listOperations.contains(Pattern.quote(valuesInScreenDivided[i]))) {
                return valuesInScreenDivided[i];
            }
        }
        return "";
    }


    private int idLastOperator() {
        String[] valuesInScreenDivided = splitValues("");
        for (int i = 0; i < valuesInScreenDivided.length; i++) {
            if (listOperations.contains(Pattern.quote(valuesInScreenDivided[i]))) {
                return i;
            }
        }
        return 0;
    }

    @NonNull
    private Boolean haveComma(@NonNull String value) {
        String[] valueInScreenDivided = value.split("");
        for (String caracter : valueInScreenDivided) {
            if (caracter.equals(".")) {
                return true;
            }
        }
        return false;
    }


    private Double getTotal(Double total) {
        String[] valuesDivided = splitValues();
        for (int i = 0; i < getSize_ListOperations(); i++) {
            label:
            for (String value:
                    valuesDivided) {
                switch (getValue_ListOperations(i)) {
                    case "+":
                        total = operations.sum(total, convert.StrToInt(value));
                        break label;
                    case "-":
                        total = operations.subtraction(total, convert.StrToInt(value));
                        break label;
                    case "x":
                        total = operations.multiplication(total, convert.StrToDouble(value));
                        break label;
                    case "/":
                        total = operations.division(total, convert.StrToDouble(value));
                        break label;
                }
            }

        }
        return total;
    }

    private double getPercent() {
        return convert.StrToDouble(getValue_ListValue(getSize_ListValue() - 1)) / 100;
    }

    private String getValue_ListOperations(int id) {
        return operations.getOperation(id);
    }

    private int getSize_ListOperations() {
        return this.operations.returnSizeOfOperations();
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
        valuesInScreen = value;
    }

    private void CancelEntry() {
        if (!valuesInScreen.isEmpty()) {
            for (int i = valuesInScreen.length() - 1; i > 0; i--) {
                char a = valuesInScreen.charAt(i);
                System.out.println(a);
                if ((listOperations.contains(Pattern.quote(Character.toString(valuesInScreen.charAt(i)))))) {
                    valuesInScreen = (valuesInScreen.substring(0, i + 1));
                    break;
                }
            }
        }
    }

    private void Clear() {
        valuesInScreen = "";
        operations.clearListOfValues();
        operations.clearOperations();
        printInScreenOfOperations("");
        printInScreenOfResults("");
    }

    private int InvertSignal(int value) {
        return -value;
    }

    private void setInListValue() {
        String[] values = splitValues();
        operations.clearListOfValues();
        for (String value : values) {
            if (!(value.equals("")))
                operations.setValue(value);
        }

    }

    @NonNull
    private String[] splitValues() {
        String[] values;
        values = valuesInScreen.split(String.valueOf(listOperations));
        return values;
    }

    @NonNull
    private String[] splitValues(String whereToCut) {
        String[] values;
        values = valuesInScreen.split(whereToCut);
        return values;
    }

    private void btnResultWasClicked(boolean value) {
        btnResultClicked = value;
    }

    public String returnExpression() {
        return valuesInScreen;
    }

    @NonNull
    private String retornLastCaracter_ListValue() {
        return Character.toString(valuesInScreen.charAt(valuesInScreen.length() - 1));
    }

    private void setValuesInScreen(String value) {
        String[] listValues = splitValues();
        playFeedbackSound();
        if (haveComma(listValues[listValues.length - 1])) {
            valuesInScreen += value;
            printInScreenOfOperations(valuesInScreen.replace(".", ","));
            printInScreenOfResults(valuesInScreen.replace(".", ","));
        } else {
            if (listOperations.contains(Pattern.quote(value)) && !(valuesInScreen.equals(""))) {
                if (isLastElementAOperator()) {
                    replaceLastValueInScreenIfIsAOperator(value);
                } else {
                    valuesInScreen += value;
                }
                printInScreenOfOperations(returnExpression());
            } else {
                if (listValues[listValues.length - 1].length() - 1 == 18) {
                    Clear();
                } else if (!(listOperations.contains(Pattern.quote(value)))) {
                    valuesInScreen += value;
                    listValues = splitValues();
                    printInScreenOfOperations(returnExpression());
                    printInScreenOfResults(formatValue(listValues[listValues.length - 1]));
                }
            }
        }
    }

    private void playFeedbackSound() {
        media.start();
    }

    public void replaceLastValueInScreenIfIsAOperator(String value) {
        valuesInScreen = valuesInScreen.substring(0, valuesInScreen.length() - 1) + value;
    }

    private boolean isLastElementAOperator() {
        return listOperations.contains(Pattern.quote(Character.toString(valuesInScreen.charAt(valuesInScreen.length() - 1))));
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