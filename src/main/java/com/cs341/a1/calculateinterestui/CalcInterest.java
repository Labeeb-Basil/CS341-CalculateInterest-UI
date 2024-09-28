package com.cs341.a1.calculateinterestui;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author test
 */
public class CalcInterest {

    private static final float ERROR_CASE = -1f; // Error condition

    private static final int ZERO = 0;

    // Loan Amount
    public static final int HUNDRED_THOUSAND = 100000;
    public static final int FIVE_HUNDRED_THOUSAND = 500000;

    // Interest Values
    private static final float FIVE_POINT_FIVE_PERCENT = 5.5f;
    private static final float SIX_POINT_FIVE_PERCENT = 6.5f;
    private static final float SEVEN_PERCENT = 7.0f;
    private static final float EIGHT_PERCENT = 8.0f;
    private static final float EIGHT_POINT_FIVE_PERCENT = 8.5f;
    private static final float TWELVE_PERCENT = 12.0f;

    // Loan Types
    public static final int LOAN_TYPE_HOME = 1;
    public static final int LOAN_TYPE_PROPERTY = 2;

    // Year Loans
    public static final int FIVE_YEARS = 5;
    public static final int TEN_YEARS = 10;
    public static final int ELEVEN_YEARS = 11;

    private static float interestValue;

    // Helper method to ensure input is not invalid.
    private static void validateInputs(double loanAmount, int yearLoan, int loanType) {
        if (yearLoan <= ZERO) {
            throw new IllegalArgumentException("Year Loan should be more than 0");
        }
        if (loanAmount <= ZERO) {
            throw new IllegalArgumentException("Loan Amount should be more than 0");
        }
        if (loanType != LOAN_TYPE_HOME && loanType != LOAN_TYPE_PROPERTY) {
            throw new IllegalArgumentException("Loan Type should be either 1 (Home) or 2 (Property)");
        }
    }

    // Method that computes simple interest based on provided params.
    // This section uses ternary operators to calculate based on loanType.
    // Format: (condition) ? doIfTrue : doIfFalse.
    // Explanation: If the condition is true, the expression right after '?' is executed (IF part). 
    // Otherwise, the expression after ':' is executed (ELSE part).
    // Using ternary operators reduces the amount of code and ensures all cases are covered.
    private static float computeLoanInterest(double loanAmount, int yearLoan, int loanType) {
        try {
            validateInputs(loanAmount, yearLoan, loanType);
        } catch (Exception e) {
            System.out.println(e);
            return ERROR_CASE;
        }
        String message = "";

        if (loanAmount < HUNDRED_THOUSAND) {
            message = "loanAmount < $100k";
            interestValue = (yearLoan <= FIVE_YEARS)
                    ? (loanType == LOAN_TYPE_HOME ? EIGHT_PERCENT : TWELVE_PERCENT)
                    : ((loanType == LOAN_TYPE_HOME) ? yearLoan <= TEN_YEARS : yearLoan < TEN_YEARS)
                            ? (loanType == LOAN_TYPE_HOME ? SIX_POINT_FIVE_PERCENT : EIGHT_POINT_FIVE_PERCENT)
                            : (yearLoan >= ELEVEN_YEARS)
                                    ? (loanType == LOAN_TYPE_HOME ? FIVE_POINT_FIVE_PERCENT : SEVEN_PERCENT)
                                    : ERROR_CASE;
        } else if (loanAmount < FIVE_HUNDRED_THOUSAND) {
            message = "loanAmount < $500k";
            interestValue = ((loanType == LOAN_TYPE_HOME) ? yearLoan <= TEN_YEARS : yearLoan < TEN_YEARS)
                    ? (loanType == LOAN_TYPE_HOME ? SIX_POINT_FIVE_PERCENT : EIGHT_POINT_FIVE_PERCENT)
                    : ERROR_CASE;
        } else {
            message = "loanAmount >= $500k";
            interestValue = (yearLoan >= ELEVEN_YEARS)
                    ? (loanType == LOAN_TYPE_HOME ? FIVE_POINT_FIVE_PERCENT : SEVEN_PERCENT)
                    : ERROR_CASE;
        }

        if (interestValue == ERROR_CASE) {
            System.out.println("Error Case in: " + message);
            return ERROR_CASE;
        }

        return (float) (loanAmount * yearLoan * interestValue) / 100;
    }

    private static boolean checkReturnError(float[] values) {
        for (float value : values) {
            if (value == ERROR_CASE) {
                System.out.println(value + " " + ERROR_CASE);
                return true;
            }
        }
        return false;
    }

    public static String[] computeLoanInterestValue(double loanAmount, int yearLoan, int loanType) {
        float returnValue = computeLoanInterest(loanAmount, yearLoan, loanType);

        if (checkReturnError(new float[]{returnValue})) {
            return new String[]{"Loan can't be given for entered values.", "red"};
        }

        return new String[]{"Loan Interest: $" + returnValue + " \n\nInterest Rate: " + interestValue + "%", "#66faee"};
    }

    public static String[] computeLoanAmountValue(double loanAmount, int yearLoan, int loanType) {

        float returnValue = (float) loanAmount + computeLoanInterest(loanAmount, yearLoan, loanType);

        if (checkReturnError(new float[]{returnValue})) {
            return new String[]{"Loan can't be given for entered values.", "red"};
        }

        return new String[]{"Loan Amount: $" + returnValue + " \n\nInterest Rate: " + interestValue + "%", "#66faee"};
    }

    // Compute loan balance for home and property loans
    public static String[] computeLoanBalance() {
        int LOAN_YEAR = 2020;
        int CURRENT_YEAR = 2024;
        float INITIAL_AMOUNT = 175000f;
        
        System.out.println(computeLoanInterest(INITIAL_AMOUNT, CURRENT_YEAR - LOAN_YEAR, LOAN_TYPE_HOME));

        float homeLoanBalance = INITIAL_AMOUNT - computeLoanInterest(INITIAL_AMOUNT, CURRENT_YEAR - LOAN_YEAR, LOAN_TYPE_HOME);
        float propertyLoanBalance = INITIAL_AMOUNT - computeLoanInterest(INITIAL_AMOUNT, CURRENT_YEAR - LOAN_YEAR, LOAN_TYPE_PROPERTY);

        if (checkReturnError(new float[]{homeLoanBalance, propertyLoanBalance})) {
            return new String[]{"Loan can't be given for entered values.", "red"};
        }

        return new String[]{"Loan Balance: \n\tHome:" + roundDecimal(homeLoanBalance, 2) + "\n\tProperty: " + roundDecimal(propertyLoanBalance, 2), "#66faee"};
    }

    // Helper method to round float value to required decimal places
    private static String roundDecimal(float value, int places) {
        return String.format("%." + places + "f", value);
    }

    public static String[] availableYearLoan(double loanAmount, int loanType) {
        if (loanAmount < HUNDRED_THOUSAND) {
            return generateArray(1, 100);
        } else if (loanAmount < FIVE_HUNDRED_THOUSAND) {
            if (loanType == LOAN_TYPE_HOME) {
                return generateArray(1, 10);
            } else {
                return generateArray(1, 9);
            }
        } else {
            return generateArray(11, 100);
        }
    }

// Utility method to generate an array from 'start' to 'end'
    private static String[] generateArray(int start, int end) {
        String[] array = new String[end - start + 1];
        for (int i = 0; i < array.length; i++) {
            int value = start + i;
            if (value == 1) {
                array[i] = value + " year";
            } else {
                array[i] = value + " years";
            }
        }
        return array;
    }

}
