package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Operation;
import com.example.myapplication.util.Convert;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final Set<String> listOperations;

    static {
        // Pattern.quote do the escape because the characters "+" and "-" can generate problems for being used for by the system
        listOperations = Set.of(
                Pattern.quote("+"),
                Pattern.quote("-"),
                Pattern.quote("/"),
                Pattern.quote("x"),
                Pattern.quote("%"),
                Pattern.quote("="));
    }

    private final Operation operations = new Operation();
    private final Convert convert = new Convert();
    private final Locale localeBR = new Locale("pt", "br");
    private final NumberFormat numberFormat = NumberFormat.getNumberInstance(localeBR);
    private final DecimalFormat decimalFormat = new DecimalFormat("0.##");
    public String valuesInScreen = "";
    //    private MediaPlayer media;
    private Button button;
    private boolean btnPercentClicked = false;
    private boolean btnInvertSignalClicked = false;
    private boolean value_isDouble = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButtons();
    }

    private void startButtons() {
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
                    // Insert the operations and calculate if have more than 2 values
                    if (!valueInScreen_IsEmpty()) {
/*                        if (getSize_ListOperations() >= 1) {
                            String[] values = splitValues(Pattern.quote(operations.getOperation(0)));
                            if (btnInvertSignalClicked) {
                                if (values.length >= 2) {
                                    setInListLastValue();
                                }
                            } else {
                                setInListValue();
                            }
                        }*/
                        insertValueIfBtnInvertSignalWasNotClicked();
                        calculateValue(inputOperation);
                        insertOperationIn_ListOperation(inputOperation);
                        setValuesInScreen(inputOperation);
                        printInScreenOfOperations(returnExpression());
                        if (btnInvertSignalClicked) {
                            setValueBtnInvertSignal(false);
                        }
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
                            clear();
                            break;

                        case "CE": // Cancel last entry data
                            if (getSize_ListOperations() < 1) {
                                clear();
                            } else {
                                cancelEntry();
                            }
                            printInScreenOfOperations(valuesInScreen);
                            printInScreenOfResults("");
                            break;
                        case ",":// Insert dot(In Brazil is Comma) becouse this it is made the replace
                            String[] values = splitValues();
                            if (!(haveComma(values[values.length - 1]))) {
                                insertDot();
                                value_isDouble = true;
                                printInScreenOfOperations(valuesInScreen);
                                printInScreenOfResults(valuesInScreen);
                            }
                            break;
                        case "%":// This function will do two things: if the operations is '+' or '-'
                            // will insert the value * percent and if the operations is 'x' or '/' print just percent
                            if (!valueInScreen_IsEmpty()) {
                                setInListValue();
                                String[] Values = splitValues();
                                double totalPercent = 0.0;
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
                                    cancelEntry();
                                    if (haveValueAfterDot(convert.DoubleToStr(totalPercent))) {
                                        incrementValueInScreen(convert.DoubleToStr(totalPercent));
                                    } else {
                                        incrementValueInScreen(convert.IntToStr((int) totalPercent));
                                    }
                                } else {
                                    updateValueInScreen("0");
                                }
                                operations.removeLastValue();
                                btnPercentClicked();
                                printInScreenOfOperations(replaceDotToComma(returnExpression()));// Print Expression with value of percent calculated
                                printInScreenOfResults(Values[Values.length - 1] + "%");//When is press '%' insert this caracter in screen
                                break;
                            }

                        case "+/-":// Invert the signal do make a validation for if the value in screen have operation
                            if (!valueInScreen_IsEmpty()) {
                                insertValueIfBtnInvertSignalWasNotClicked();
                                int lastValue = convert.StrToInt(operations.getValue(getSize_ListValue() - 1));
                                String valueInput;
                                int valueInverted = 0;
                                if (!(operations.verifyIfOperationIsEmpty())) {
                                    switch (witchIsTheLastOperator()) {
                                        case "-":
                                        case "+":
                                        case "x":
                                        case "/":
                                            valueInverted = invertSignal(lastValue);
                                            valueInput = valuesInScreen.substring(0, idFirstOperator() + 1);
                                            char lastCaracter = valueInput.charAt(valueInput.length() - 1);
                                            if ((lastCaracter == '+' && valueInverted < 0) || (lastCaracter == '+' && valueInverted > 0)) {
                                                setValueInScreenIfAreMoreThanTwoValuesWhenBtnInvertWasClicked(valueInverted);
                                            } else if ((lastCaracter == '-' && valueInverted < 0) || (lastCaracter == '-' && valueInverted > 0)) {
                                                setValueInScreenIfAreMoreThanTwoValuesWhenBtnInvertWasClicked(valueInverted);
                                            } else {
                                                if ((lastCaracter == 'x' && valueInverted < 0) || (lastCaracter == '/' && valueInverted < 0)) {
                                                    updateValueInScreen(valueInput + "(" + valueInverted + ")");
                                                } else {
                                                    updateValueInScreen(valueInput + valueInverted);
                                                }
                                            }
                                            operations.updateLastValue(convert.IntToStr(valueInverted));
                                            printInScreenOfOperations(returnExpression());
                                            break;
                                    }
                                } else {
                                    valueInverted = invertSignal(lastValue);
                                    operations.updateLastValue(convert.IntToStr(valueInverted));
                                    updateValueInScreen(convert.IntToStr(valueInverted));
                                    printInScreenOfOperations(convert.IntToStr(valueInverted));
                                }
                                btnInvertSignalWasClicked(); // TRUE
                                printInScreenOfResults(convert.IntToStr(valueInverted));
                            } else {
                                Toast.makeText(this, "Digite um valor", Toast.LENGTH_SHORT).show();
                            }

                            break;


                        case "=":
                            if (!valueInScreen_IsEmpty()) {
                                insertValueIfBtnInvertSignalWasNotClicked();
                                Double total = returnTotalCalcule();
                                if (!(haveValueAfterDot(convert.DoubleToStr(total)))) {
                                    printInScreenOfOperations(replaceDotToComma(valuesInScreen) + " = " + convert.IntToStr(total.intValue()));
                                    printInScreenOfResults(convert.IntToStr(total.intValue()));
                                } else {
                                    printInScreenOfOperations(replaceDotToComma(valuesInScreen) + " = " + replaceDotToComma(decimalFormat.format(total)));
                                    printInScreenOfResults(replaceDotToComma(decimalFormat.format(total)));
                                }
                                if (getSize_ListValue() > 1) {
                                    updateValueInScreen("");
                                    operations.clearOperations();
                                    operations.clearListOfValues();
                                    btnPercentClicked = false;
                                    setValueBtnInvertSignal(false);
                                } else {
                                    clear();
                                }
                                break;
                            }
                    }
                });
    }


    /**
     * Methods
     */


    private void btnInvertSignalWasClicked() {
        setValueBtnInvertSignal(true);
    }

    private void setValueBtnInvertSignal(boolean status) {
        btnInvertSignalClicked = status;
    }

    private void insertValueIfBtnInvertSignalWasNotClicked() {
        if (!btnInvertSignalClicked && !isLastElementAOperator()) {
            if (operations.verifyIfListValuesIsEmpty()) {
                setInListValue();
            } else {
                setInListLastValue();
            }
        }
    }

    private void setValueInScreenIfAreMoreThanTwoValuesWhenBtnInvertWasClicked(int valueInverted) {
        if (valueInverted < 0) {
            updateValueInScreen(String.format(valuesInScreen.substring(0, idFirstOperator() + 1) + "(" + valueInverted + ")"));
        } else {
            updateValueInScreen(valuesInScreen.substring(0, idFirstOperator() + 1) + valueInverted);
        }
    }

    private boolean valueInScreen_IsEmpty() {
        return valuesInScreen.equals("");
    }

    private void incrementValueInScreen(String value) {
        valuesInScreen += value;
    }


    private void insertOperationIn_ListOperation(String inputOperation) {
        if (listOperations.contains(Pattern.quote(retornLastCaracter_ListValue()))) {
            operations.updateOperation(inputOperation);
        } else {
            operations.setOperation(inputOperation);
        }
    }

    private void calculateValue(@NonNull String inputOperation) {
        if (!(inputOperation.equals(Pattern.quote("%")))) {
            if (getSize_ListValue() >= 2) {
                double total = returnTotalCalcule();
                clear();
                if (!value_isDouble) {
                    updateValueInScreen(convert.IntToStr((int) (total)));
                } else {
                    updateValueInScreen(convert.DoubleToStr(total));
                }
                operations.setValue(convert.DoubleToStr(total));
            }
        }
    }

    public boolean haveValueAfterDot(@NonNull String value) {
        String[] Values = value.split("\\.");
        if (Values.length > 1) {
            return !(Values[Values.length - 1].equals("0"));
        }
        return false;
    }

    @NonNull
    private String replaceDotToComma(@NonNull String value) {
        return value.replace(".", ",");
    }

    private Double returnTotalCalcule() {
        if (getSize_ListValue() == 2) {
            double val1 = convert.StrToDouble(operations.getValue(0));
            double val2 = convert.StrToDouble(operations.getValue(1));
            switch (operations.getOperation(0)) {
                case "+":
                    return operations.sum(val1, val2);
                case "-":
                    return operations.subtraction(val1, val2);
                case "x":
                    return operations.multiplication(val1, val2);
                case "/":
                    return operations.division(val1, val2);
            }
        } else {
            Toast.makeText(this, "Ocorreu um erro ao realizar o calculo!", Toast.LENGTH_SHORT).show();
        }
        return 0.0;
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

    private int idFirstOperator() {
        for (int i = 0; i < valuesInScreen.length() - 1; i++) {
            if (listOperations.contains(Pattern.quote(Character.toString(valuesInScreen.charAt(i))))) {
                return i;
            }
        }
        return 0;
    }

    private int idLastOperator() {
        for (int i = valuesInScreen.length() - 1; i >= 0; i--) {
            if (listOperations.contains(Pattern.quote(Character.toString(valuesInScreen.charAt(i))))) {
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
        return this.operations.returnSizeOfValue();
    }

    private void updateValueInScreen(String value) {
        valuesInScreen = value;
    }

    private void cancelEntry() {
        updateValueInScreen(valuesInScreen.substring(0, idLastOperator() + 1));
    }

    private void clear() {
        updateValueInScreen("");
        btnPercentClicked = false;
        operations.clearListOfValues();
        operations.clearOperations();
        printInScreenOfOperations(returnExpression());
        printInScreenOfResults(returnExpression());
    }

    private int invertSignal(int value) {
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
        String[] values = splitValues(Pattern.quote(Character.toString(valuesInScreen.charAt(idLastOperator()))));
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
        String[] listValues;
        clearScreenIfBtnPercentWasClicked();// If percent was clicked AND user insert more values clear screen
        if (!(valuesInScreen.equals("") && value.equals("0"))) {
            if (listOperations.contains(Pattern.quote(value)) && !valueInScreen_IsEmpty()) {
                if (isLastElementAOperator()) {
                    replaceLastValueInScreenIfIsAOperator(value);
                } else {
                    incrementValueInScreen(value);
                }
                printInScreenOfOperations(returnExpression());
            } else {
                if (!(listOperations.contains(Pattern.quote(value)))) {
                    incrementValueInScreen(value);
                    listValues = splitValues();
                    if (listValues[listValues.length - 1].length() == 11) {
                        Toast.makeText(this, "Valor maximo atingido!", Toast.LENGTH_SHORT).show();
                        clear();
                    } else {
                        printInScreenOfOperations(returnExpression());
                        printInScreenOfResults(formatValue(listValues[listValues.length - 1]));
                    }
                }
            }
        } else {
            clear();
        }
    }

    private void clearScreenIfBtnPercentWasClicked() {
        if (btnPercentClicked) {
            clear();
        }
    }

    public void replaceLastValueInScreenIfIsAOperator(String value) {
        updateValueInScreen(valuesInScreen.substring(0, valuesInScreen.length() - 1) + value);
    }

    private boolean isLastElementAOperator() {
        return listOperations.contains(Pattern.quote(Character.toString(valuesInScreen.charAt(valuesInScreen.length() - 1))));
    }

    public String formatValue(String value) {
        if (!value_isDouble) {
            return numberFormat.format(convert.StrToLong(value));
        } else {
            return numberFormat.format(convert.StrToDouble(value));
        }
    }

    private void insertDot() {
        if (valueInScreen_IsEmpty()) {
            updateValueInScreen("0.");
        } else {
            incrementValueInScreen(".");
        }
    }


    private void printInScreenOfOperations(String value) {
        TextView txtOperations = findViewById(R.id.txtPrintOperations);
        txtOperations.setText(value.replace(".", ","));
    }

    private void printInScreenOfResults(String value) {
        TextView txtResults = findViewById(R.id.txtPrintResult);
        txtResults.setText(value.replace(".", ","));
    }
}