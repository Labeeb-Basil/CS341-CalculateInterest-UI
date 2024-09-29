package com.cs341.a1.calculateinterestui;

/**
 *
 * @author test
 */
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CalculateInterestUI {

    private static final float ERROR_CASE = -1.0f; // Error condition

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

    // Method that computes simple interest based on provided params.
    // This section uses ternary operators to calculate based on loanType.
    // Format: (condition) ? doIfTrue : doIfFalse.
    // Explanation: If the condition is true, the expression right after '?' is executed (IF part). 
    // Otherwise, the expression after ':' is executed (ELSE part).
    // Using ternary operators reduces the amount of code and ensures all cases are covered.
    public static float computeLoanInterest(double loanAmount, int yearLoan, int loanType) {
        if (yearLoan <= ZERO) {
            return ERROR_CASE;
        }
        if (loanAmount <= ZERO) {
            return ERROR_CASE;
        }
        if (loanType != LOAN_TYPE_HOME && loanType != LOAN_TYPE_PROPERTY) {
            return ERROR_CASE;
        }
        String message = "";
        interestValue = -1.0f;

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
            System.out.println("Error Case in: " + message + " Result: " + ERROR_CASE);
            return ERROR_CASE;
        }

        double result = Math.round(((loanAmount * yearLoan * interestValue) / 100) * 100) / 100d;

        System.out.println(message + " Result: " + result);

        return (float) result;
    }

    private static boolean checkReturnError(float value) {

        if (value == ERROR_CASE) {
            System.out.println(value + " " + ERROR_CASE);
            return true;
        }

        return false;
    }

    public static String[] computeLoanInterestValue(double loanAmount, int yearLoan, int loanType) {
        float returnValue = computeLoanInterest(loanAmount, yearLoan, loanType);

        if (checkReturnError(returnValue)) {
            return new String[]{"Loan can't be given for entered values\nCheck your relevant manual", "#FF0000"};
        }

        return new String[]{"\nLoan Interest: $" + returnValue + " \n\nInterest Rate: " + interestValue + "%", "#66faee"};
    }

    public static String[] computeLoanAmountValue(double loanAmount, int yearLoan, int loanType) {
        
        float returnValue = computeLoanInterest(loanAmount, yearLoan, loanType);

        if (checkReturnError(returnValue)) {
            return new String[]{"Loan can't be given for entered values\nCheck your relevant manual", "#FF0000"};
        }

        float returnLoanAmount = (float) loanAmount + returnValue;

        return new String[]{"\nLoan Amount: $" + returnLoanAmount + " \n\nInterest Rate: " + interestValue + "%", "#66faee"};
    }

    // Compute loan balance for home and property loans
    // Assuming started on Jan 1st, first payment made on Feb 1st and then 1st of every following month
    // Currently at september, payments made = 56
    // Assuming Loan Type is Home (1)
    public static String[] computeLoanBalance() {
        float INITIAL_AMOUNT = 175000f;
        int ASSUMED_LOAN_TERM = 6; // years
        LocalDate loanStartDate = LocalDate.of(2020, 1, 1);
        LocalDate currentDate = LocalDate.now();

        int totalMonths = (int) ChronoUnit.MONTHS.between(loanStartDate, ChronoUnit.YEARS.addTo(loanStartDate, ASSUMED_LOAN_TERM));
        int monthsTillNow = (int) ChronoUnit.MONTHS.between(loanStartDate, currentDate);

        float homeLoanInterest = computeLoanInterest(INITIAL_AMOUNT, ASSUMED_LOAN_TERM, LOAN_TYPE_HOME);

        if (checkReturnError(homeLoanInterest)) {
            return new String[]{"Error while calculating Loan Balance", "#FF0000"};
        }

        float monthlyPaymentHome = computeMonthlyPayment(INITIAL_AMOUNT, homeLoanInterest, totalMonths);
        float homeLoanPaidAmount = monthlyPaymentHome * monthsTillNow;
        float homeLoanBalance = (INITIAL_AMOUNT + homeLoanInterest) - homeLoanPaidAmount;

        // Return the formatted loan balance
        return new String[]{
            "Assumed Loan Term: " + totalMonths + " months"
            + "\nAssumed Start Date: " + loanStartDate
            + "\nMonths Paid: " + monthsTillNow + " months"
            + "\n\nIf Type - Home: "
            + "\n\tPaid Amount: $" + homeLoanPaidAmount
            + "\n\tRemaining Balance: $" + roundDecimal(homeLoanBalance, 2), "#66faee"
        };
    }

    private static float computeMonthlyPayment(float principal, float interestAmount, int durationMonths) {
        return (principal + interestAmount) / durationMonths;
    }

    // Helper method to round float value to required decimal places
    private static String roundDecimal(float value, int places) {
        return String.format("%." + places + "f", value);
    }
}
