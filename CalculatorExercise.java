package com.example.androidprojectcollection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CalculatorExercise extends AppCompatActivity {
    StringBuilder number;
    StringBuilder equation;
    List<String> listEquation;
    String SequentialResult;

    // Buttons
    List<Button> buttonNumbers;
    List<Button> buttonOperations;
    Button btnClear;
    Button btnNumber9;
    Button btnNumber8;
    Button btnNumber7;
    Button btnNumber6;
    Button btnNumber5;
    Button btnNumber4;
    Button btnNumber3;
    Button btnNumber2;
    Button btnNumber1;
    Button btnNumber0;

    Button btnOpDiv;
    Button btnOpMul;
    Button btnOpSub;
    Button btnOpAdd;
    Button btnOpEquals;
    Button btnOpPeriod;

    // TextView
    TextView tv_equation;
    TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_calculator_exercise);

        number = new StringBuilder();
        equation = new StringBuilder();

        assignButtonsAndTextView();
        assignOnClickListeners();

        listEquation = new ArrayList<>();
    }

    private void assignOnClickListeners() {
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCalculator();
            }
        });

        for (Button num: buttonNumbers){
            num.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StringBuilder curNum = new StringBuilder(num.getText().toString());

                    if (number.length() == 1 && number.charAt(0) == '0'){
                        number.setCharAt(0, curNum.charAt(0));
                    } else {
                        number.append(curNum);
                    }

                    /* show number on result textview */
//                    if (number.length() == 1){
//                        tv_result.setText(curNum);
//                    } else {
//                        tv_result.append(curNum);
//                    }

                    tv_equation.setText(equation.toString());
                    tv_equation.append(number.toString());

                    if (!listEquation.isEmpty()){
                        tv_result.setText(calculateSequential());
                    }
                }
            });
        }

        for (Button op: buttonOperations){
            op.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if (!listEquation.isEmpty() || number.length() != 0){
                        char ope = op.getText().charAt(0);

                        switch (ope){
                            case '+':
                            case '-':
                            case '÷':
                            case '×':
                                if (number.length() == 0){
                                    listEquation.remove(listEquation.size()-1);
                                    equation.setCharAt(equation.length()-1, ope);
                                } else {
                                    if (number.charAt(number.length()-1) == '.') {
                                        number.setLength(number.length()-1);
                                    }

                                    listEquation.add(number.toString());
                                    equation.append(number.toString());
                                    equation.append(ope);
                                    number.setLength(0);
                                }

                                listEquation.add(String.valueOf(ope));
                                tv_equation.setText(equation);
                                tv_result.setText(calculateSequential());
                                break;
                            case '.':
                               if (number.indexOf(op.getText().toString()) == -1){
                                   number.append(ope);
                                   tv_equation.append(op.getText().toString());
                                } else if (number.charAt(number.length()-1) == '.'){
                                   number.setLength(number.length()-1);
                                   tv_equation.setText(equation.toString());
                                   tv_equation.append(number.toString());
                                }

                                break;
                            case '=':
                                if (number.length() != 0){
                                    if (number.charAt(number.length()-1) == '.'){
                                        number.setLength(number.length()-1);
                                    }

                                    listEquation.add(number.toString());
                                    equation.append(number.toString());
                                }

                                if (!Character.isDigit(equation.charAt(equation.length()-1))){
                                    equation.setLength(equation.length()-1);
                                    listEquation.remove(listEquation.size()-1);
                                }

                                System.out.println("\nEQUATION: " + equation);
                                System.out.print("BEFORE MDAS: ");
                                for (String s: listEquation){
                                    System.out.print(s + " ");
                                }
                                System.out.println("list size: " + listEquation.size());

                                String finalEquation = equation.toString() + "=";
                                String result = calculateMDAS();
                                clearCalculator();
                                tv_equation.setText(finalEquation);
                                tv_result.setText(result);
                                break;
                        }
                    }
                }
            });
        }
    }

    private String calculateSequential(){
        if (listEquation.size() == 2 && number.length() == 0){
            SequentialResult = listEquation.get(0);
            return SequentialResult;
        }

        int opIndexFrLast;
        BigDecimal right;
        char op;
        if (number.length() == 0){
            right = new BigDecimal(listEquation.get(listEquation.size()-2));
            opIndexFrLast = 3;
        } else {
            right = new BigDecimal(number.toString());
            opIndexFrLast = 1;
        }

        op = listEquation.get(listEquation.size()-opIndexFrLast).charAt(0);

        if (!SequentialResult.equals("ERROR")){
            BigDecimal left = new BigDecimal(SequentialResult);

            BigDecimal tempRes = new BigDecimal(0);
            switch (op){
                case '+':
                    tempRes = left.add(right);
                    break;
                case '-':
                    tempRes = left.subtract(right);
                    break;
                case '×':
                    tempRes = left.multiply(right);
                    break;
                case '÷':
                    try {
                        tempRes = left.divide(right);
                    } catch (ArithmeticException a){
                        if (opIndexFrLast == 3){
                            SequentialResult = "ERROR";
                        }

                        return "ERROR";
                    }

                    break;
            }

            if (opIndexFrLast == 3){
                SequentialResult = tempRes.toString();
            }

            return tempRes.toString();
        }

        return "ERROR";
    }
    private String calculateMDAS(){
        System.out.print("INFIX: ");
        for (String s: listEquation){
            System.out.print(s + " ");
        }

        getPostfixExpression();

        System.out.print("\nPOSTFIX: ");
        for (String s: listEquation){
            System.out.print(s + " ");
        }

        Stack<String> calculations = new Stack<>();

        for (String s: listEquation){
            BigDecimal left;
            BigDecimal right;
            if (s.contains(".") || Character.isDigit(s.charAt(0))) {
                calculations.push(s);
            } else {
                right = new BigDecimal(calculations.pop());
                left = new BigDecimal(calculations.pop());

                switch (s.charAt(0)){
                    case '+':
                        calculations.push(left.add(right).toString());
                        break;
                    case '-':
                        calculations.push(left.subtract(right).toString());
                        break;
                    case '×':
                        calculations.push(left.multiply(right).toString());
                        break;
                    case '÷':
                        try {
                            calculations.push(left.divide(right).toString());
                        } catch (ArithmeticException a){
                            return "ERROR Cannot divide by 0";
                        }

                        break;
                }
            }
        }

        return calculations.pop();
    }

    private void getPostfixExpression(){
        // arraylist for the postfix expression
        List<String> pf = new ArrayList<>();

        // stack for operators
        Stack<String> ops = new Stack<>();

        for (String s: listEquation){

            /* Operands will be added directly to the pf (postfix expression) */
            if (s.contains(".") || Character.isDigit(s.charAt(0))){
                pf.add(s);
            } else {
                /* If the ops stack for operators is empty, current operator can be added directly */
                if (ops.empty()){
                    ops.add(s);
                } else {
                    /* If the current accessed string is an operator

                    /* STEP 1: Compare the current operator with the last added operator (AKA TOP of the ops stack) if is less in precedence
                    /* STEP 2.1: If it is less in precedence, POP the last added operator -> ADD it into the postfix expression
                    /* STEP 3.1: Repeat STEP 1 & 2.1 (if applicable) until the current operator is less in precedence than the one on TOP of the stack
                    /* or until the stack is empty

                    /* STEP 2.2: If it is greater in precedence, add it directly to the ops stack */
                    while (!ops.empty() && getPrecedence(s.charAt(0)) < getPrecedence(ops.peek().charAt(0))){
                        pf.add(ops.pop());
                    }

                    ops.push(s);
                }
            }
        }

        while (!ops.empty()){
            pf.add(ops.pop());
        }

        listEquation = pf;
    }

    // getting the precedence priority?? sort of
    // 3 - highest, 1 - lowest
    // M > D > AS
    private int getPrecedence(char op){
        if (op == '×'){
            return 3;
        } else if (op == '÷'){
            return 2;
        } else {
            return 1;
        }
    }

    // resetting calculator
    private void clearCalculator(){
        tv_result.setText("");
        tv_equation.setText("");
        number.setLength(0);
        equation.setLength(0);
        listEquation.clear();
        SequentialResult = null;
    }
    private void assignButtonsAndTextView(){
        btnClear = findViewById(R.id.btnClear);

        btnNumber0 = findViewById(R.id.btnNumber0);
        btnNumber1 = findViewById(R.id.btnNumber1);
        btnNumber2 = findViewById(R.id.btnNumber2);
        btnNumber3 = findViewById(R.id.btnNumber3);
        btnNumber4 = findViewById(R.id.btnNumber4);
        btnNumber5 = findViewById(R.id.btnNumber5);
        btnNumber6 = findViewById(R.id.btnNumber6);
        btnNumber7 = findViewById(R.id.btnNumber7);
        btnNumber8 = findViewById(R.id.btnNumber8);
        btnNumber9 = findViewById(R.id.btnNumber9);

        btnOpAdd = findViewById(R.id.btnOpAdd);
        btnOpSub = findViewById(R.id.btnOpSub);
        btnOpMul = findViewById(R.id.btnOpMul);
        btnOpDiv = findViewById(R.id.btnOpDiv);
        btnOpEquals = findViewById(R.id.btnOpEquals);
        btnOpPeriod = findViewById(R.id.btnOpPeriod);

        tv_equation = findViewById(R.id.textview_partialRes);
        tv_result = findViewById(R.id.textview_Res);

        buttonNumbers = new ArrayList<>();
        buttonOperations = new ArrayList<>();

        buttonNumbers.add(btnNumber0);
        buttonNumbers.add(btnNumber1);
        buttonNumbers.add(btnNumber2);
        buttonNumbers.add(btnNumber3);
        buttonNumbers.add(btnNumber4);
        buttonNumbers.add(btnNumber5);
        buttonNumbers.add(btnNumber6);
        buttonNumbers.add(btnNumber7);
        buttonNumbers.add(btnNumber8);
        buttonNumbers.add(btnNumber9);

        buttonOperations.add(btnOpAdd);
        buttonOperations.add(btnOpMul);
        buttonOperations.add(btnOpDiv);
        buttonOperations.add(btnOpSub);
        buttonOperations.add(btnOpEquals);
        buttonOperations.add(btnOpPeriod);
    }
}

/* USING STACK FOR SEQUENTIAL CALCULATION */

//public class CalculatorExercise extends AppCompatActivity {
//    StringBuilder number;
//    StringBuilder equation;
//    List<String> listEquation;
//    Stack<String> stackCalculation;
//
//    // Buttons
//    List<Button> buttonNumbers;
//    List<Button> buttonOperations;
//    Button btnClear;
//    Button btnNumber9;
//    Button btnNumber8;
//    Button btnNumber7;
//    Button btnNumber6;
//    Button btnNumber5;
//    Button btnNumber4;
//    Button btnNumber3;
//    Button btnNumber2;
//    Button btnNumber1;
//    Button btnNumber0;
//
//    Button btnOpDiv;
//    Button btnOpMul;
//    Button btnOpSub;
//    Button btnOpAdd;
//    Button btnOpEquals;
//    Button btnOpPeriod;
//
//    // TextView
//    TextView tv_equation;
//    TextView tv_result;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Objects.requireNonNull(getSupportActionBar()).hide();
//        setContentView(R.layout.activity_calculator_exercise);
//
//        number = new StringBuilder();
//        equation = new StringBuilder();
//
//        assignButtonsAndTextView();
//        assignOnClickListeners();
//
//        listEquation = new ArrayList<>();
//        stackCalculation = new Stack<>();
//    }

//    private void assignOnClickListeners() {
//        btnClear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clearCalculator();
//            }
//        });
//
//        for (Button num : buttonNumbers) {
//            num.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    StringBuilder curNum = new StringBuilder(num.getText().toString());
//
//                    if (number.length() == 1 && number.charAt(0) == '0') {
//                        number.setCharAt(0, curNum.charAt(0));
//                    } else {
//                        number.append(curNum);
//                    }
//
//                    tv_equation.setText(equation.toString());
//                    tv_equation.append(number.toString());
//                }
//            });
//        }
//
//        for (Button op : buttonOperations) {
//            op.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (!listEquation.isEmpty() || number.length() != 0) {
//                        char ope = op.getText().charAt(0);
//
//                        switch (ope) {
//                            case '+':
//                            case '-':
//                            case '÷':
//                            case '×':
//                                if (number.length() == 0) {
//                                    stackCalculation.pop();
//                                    listEquation.remove(listEquation.size() - 1);
//                                    equation.setCharAt(equation.length() - 1, ope);
//                                } else {
//                                    if (number.charAt(number.length() - 1) == '.') {
//                                        number.setLength(number.length() - 1);
//                                    }
//
//                                    stackCalculation.add(number.toString());
//                                    listEquation.add(number.toString());
//
//                                    if (listEquation.size() >= 3) {
//                                        calculateSequential();
//                                    }
//
//                                    equation.append(number.toString());
//                                    equation.append(ope);
//                                    number.setLength(0);
//
//                                }
//
//                                tv_result.setText(stackCalculation.peek());
//                                stackCalculation.add(String.valueOf(ope));
//                                listEquation.add(String.valueOf(ope));
//                                tv_equation.setText(equation);
//                                break;
//                            case '.':
//                                if (number.indexOf(op.getText().toString()) == -1) {
//                                    number.append(ope);
//                                    tv_equation.append(op.getText().toString());
//                                } else if (number.charAt(number.length() - 1) == '.') {
//                                    number.setLength(number.length() - 1);
//                                    tv_equation.setText(equation.toString());
//                                    tv_equation.append(number.toString());
//                                }
//                                break;
//                            case '=':
//                                if (number.length() != 0) {
//                                    if (number.charAt(number.length() - 1) == '.') {
//                                        number.setLength(number.length() - 1);
//                                    }
//
//                                    listEquation.add(number.toString());
//                                    equation.append(number.toString());
//                                }
//
//                                if (!Character.isDigit(equation.charAt(equation.length() - 1))) {
//                                    equation.setLength(equation.length() - 1);
//                                    listEquation.remove(listEquation.size() - 1);
//                                }
//
//                                String finalEquation = equation.toString() + "=";
//                                String result = calculateMDAS();
//                                clearCalculator();
//                                tv_equation.setText(finalEquation);
//                                tv_result.setText(result);
//                                break;
//                        }
//                    }
//                }
//            });
//        }
//    }

//    private void calculateSequential(){
//         /* CALCULATING SEQUENTIAL RESULT after typing another operator
//         /* if using stackCalculation
//         /* there's at least 2 operators in the listEquation */
//        BigDecimal right = new BigDecimal(stackCalculation.pop());
//        char op = stackCalculation.pop().charAt(0);
//
////        if (!stackCalculation.peek().equals("ERROR")){
////            BigDecimal left = new BigDecimal(stackCalculation.pop());
////
////            BigDecimal tempRes = new BigDecimal(0);
////            switch (op){
////                case '+':
////                    tempRes = left.add(right);
////                    break;
////                case '-':
//                    tempRes = left.subtract(right);
//                    break;
//                case '×':
//                    tempRes = left.multiply(right);
//                    break;
//                case '÷':
//                    try {
//                        tempRes = left.divide(right);
//                    } catch (ArithmeticException a){
//                        stackCalculation.push("ERROR");
//                        return;
//                    }
//
//                    break;
//            }
//
//            stackCalculation.push(tempRes.toString());
//        }


//    // resetting calculator
//    private void clearCalculator(){
//        tv_result.setText("");
//        tv_equation.setText("");
//        number.setLength(0);
//        equation.setLength(0);
//        listEquation.clear();
//        stackCalculation.clear();
//    }