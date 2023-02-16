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
        // Pattern.quote do the escape because the characters "+" e o "-" can be generate problems for being used for the system
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
    private final Locale localeBR = new Locale("pt", "br");

    private final NumberFormat numberFormat = NumberFormat.getNumberInstance(localeBR);
    private boolean btnPercentClicked = false;
    private boolean btnInvertSignalClicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Start the player
        media = MediaPlayer.create(this, R.raw.feedback_sound_click);
        StartButtons();
    }

    private void StartButtons() {
        // One method for insert values, one for insert the operations and one for make the functions
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
                    // Insert the operations
                    if (!(valuesInScreen.equals(""))) {
                        setInListValue();
                        if (!(inputOperation.equals(Pattern.quote("%")))) {
                            if (getSize_ListValue() >= 2) {
                                double total = returnTotalCalcule();
                                valuesInScreen = convert.IntToStr((int) (total));
                            }
                        }
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
                        case "C":// CLear all values in screen
                            Clear();
                            printInScreenOfOperations(returnExpression());
                            printInScreenOfResults(returnExpression());
                            break;

                        case "CE": // Cancel last entry data
                            CancelEntry();
                            printInScreenOfOperations(valuesInScreen.replace(".", ","));
                            printInScreenOfResults("");
                            break;
                        case ",":// Insert dot(In Brazil is Comma)
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
                        case "%":// This function will go two things: if the operations is + or - will insert the value * percent and if the operations is x or / print just percent
                            if (!(valuesInScreen.equals(""))) {
                                setInListValue();
                                String[] Values = splitValues();
                                double totalPercent = 0;
                                if (getSize_ListOperations() >= 1) {
                                    switch (getValue_ListOperations(getSize_ListOperations() - 1)) {
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
                                // TODO Gambiarra
                                operations.removeLastValue();
                                btnPercentClicked();
                                printInScreenOfOperations(returnExpression());
                                printInScreenOfResults(Values[Values.length - 1] + "%");
                                break;
                            }

                        case "+/-":// Invert the signal do make a validation for if the value in screen have operation
                            if (!(valuesInScreen.equals(""))) {
                                if (operations.verifyIfListValuesIsEmpty()) {
                                    setInListValue();
                                }
                                int lastValue = convert.StrToInt(operations.getValue(operations.returnSizeOfValue() - 1));
                                String valueInput;
                                int valueInverted = 0;
                                if (!(operations.verifyIfOperationIsEmpty())) {
                                    switch (witchIsTheLastOperator()) {
                                        case "-":
                                        case "+":
                                        case "x":
                                        case "/":
                                            valueInverted = InvertSignal(lastValue);
                                            valueInput = valuesInScreen.substring(0, idLastOperator() + 1);
                                            operations.updateValue(convert.IntToStr(valueInverted));
                                            printInScreenOfOperations(valueInput);
                                            break;
                                    }
                                } else {
                                    valueInverted = InvertSignal(lastValue);
                                    operations.updateValue(convert.IntToStr(valueInverted));
//                                    updateValueInScreen(convert.IntToStr(valueInverted));
                                    printInScreenOfOperations(convert.IntToStr(valueInverted));
                                }
                                btnInvertSignalWasClicked();
                                printInScreenOfResults(convert.IntToStr(valueInverted));
                            }
                            break;


                        case "=":
                            if (!(valuesInScreen.equals(""))) {
                                setInListLastValue();
                                Double total = returnTotalCalcule();
                                printInScreenOfResults(convert.DoubleToStr(total));
                                valuesInScreen = "";
                                operations.clearOperations();
                                operations.clearListOfValues();
                                break;
                            }
                    }
                });
    }

    private Double returnTotalCalcule() {
        Double total = convert.StrToDouble(getValue_ListValue(0));
        total = getTotal(total);
        return total;
    }

    private void btnInvertSignalWasClicked() {
        btnInvertSignalClicked = true;
    }

    private void btnPercentClicked() {
        btnPercentClicked = true;
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
        if (listOperations.contains(Pattern.quote(getValue_ListOperations(operations.returnSizeOfOperations()-1)))) {
            return operations.returnSizeOfOperations()-1;
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
        int j = 1;
        for (int i = 0; i < operations.returnSizeOfOperations(); i++) {
            label:
            for (int r = j; r <= getSize_ListValue(); r++) {
                switch (getValue_ListOperations(i)) {
                    case "+":
                        total = operations.sum(total, convert.StrToDouble(getValue_ListValue(r)));
                        j = r + 1;
                        break label;
                    case "-":
                        total = operations.subtraction(total, convert.StrToDouble(getValue_ListValue(r)));
                        j = r + 1;
                        break label;
                    case "x":
                        total = operations.multiplication(total, convert.StrToDouble(getValue_ListValue(r)));
                        j = r + 1;
                        break label;
                    case "/":
                        total = operations.division(total, convert.StrToDouble(getValue_ListValue(r)));
                        j = r + 1;
                        break label;
                }
            }
        }
        return total;
    }

    private double getPercent() {
        return convert.StrToDouble(getValue_ListValue(getSize_ListValue()-1)) / 100;
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
        return this.operations.returnSizeOfValue();
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

    private void setInListLastValue() {
        String[] values = splitValues(Pattern.quote(getValue_ListOperations( idLastOperator())));
        operations.setValue(values[values.length - 1]);
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

    public String returnExpression() {
        return valuesInScreen;
    }

    @NonNull
    private String retornLastCaracter_ListValue() {
        return Character.toString(valuesInScreen.charAt(valuesInScreen.length() - 1));
    }

    private void setValuesInScreen(String value) {
        String[] listValues = splitValues();
        playFeedbackSound();// Every moment that user press one button and insert then in screen play feedback sound
        clearScreenIfBtnPercentWasClicked();// If percent was clicked AND user insert more values clear screen
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

    private void clearScreenIfBtnPercentWasClicked() {
        if (btnPercentClicked) {
            Clear();
            btnPercentClicked = false;
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