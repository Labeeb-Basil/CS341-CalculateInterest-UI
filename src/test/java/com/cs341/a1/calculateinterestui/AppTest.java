package com.cs341.a1.calculateinterestui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.testfx.util.WaitForAsyncUtils;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.ApplicationExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class AppTest extends ApplicationTest {

    @Start
    public void start(Stage stage) throws Exception {
        new App().start(stage);

        // Set up key listener to terminate the application on spacebar press
        Scene scene = stage.getScene();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE) {
                System.out.println("Terminating the application...");
                Platform.exit(); 
                System.exit(0); 
            }
        });

        stage.show();
    }

    @BeforeEach
    public void setup() {
    }

    // Test Loan Amount
    @ParameterizedTest
    @CsvSource({
        "50000.00, 2 years, Home, Loan Amount: $58,000",
        "50000.00, 7 years, Home, Loan Amount: $72,750",
        "50000.00, 13 years, Home, Loan Amount: $85,750",
        "150000.00, 5 years, Home, Loan Amount: $198,750",
        "550000.00, 20 years, Home, Loan Amount: $1,155,000",
        "150000.00, 15 years, Home, Loan can't be given for entered values, check your relevant manual",
        "600000.00, 5 years, Home, Loan can't be given for entered values, check your relevant manual",
        "50000.00, 2 years, Property, Loan Amount: $62,000",
        "50000.00, 7 years, Property, Loan Amount: $79,750",
        "50000.00, 13 years, Property, Loan Amount: $95,500",
        "150000.00, 5 years, Property, Loan Amount: $213,750",
        "550000.00, 20 years, Property, Loan Amount: $1,320,000",
        "50000.00, 10 years, Property, Loan can't be given for entered values, check your relevant manual",
        "150000.00, 15 years, Property, Loan can't be given for entered values, check your relevant manual",
        "600000.00, 5 years, Property, Loan can't be given for entered values, check your relevant manual"
    })
    public void testLoanAmountCalculation(String loanAmount, String loanYear, String loanType, String expectedResult) {
        FxRobot robot = new FxRobot();

        robot.clickOn("#calculateLoanAmountButton");
        robot.clickOn("#loanAmountField").write(loanAmount);

        selectLoanYear(robot, loanYear);

        robot.clickOn("#loanTypeField").clickOn(loanType);
        robot.clickOn("#submitButton");

        // Wait for the result to be visible
        String actualResult = WaitForAsyncUtils.waitForAsync(20, ()
                -> robot.lookup("#resultLabel").queryAs(Label.class).getText());

        if (expectedResult.contains("Amount:")) {
            assertTrue(actualResult.contains(expectedResult), "Loan Interest result mismatch");
        } else {
            assertTrue(actualResult.contains(expectedResult), "Error message mismatch");
        }
    }

    // Test Loan Interest
    @ParameterizedTest
    @CsvSource({
        "50000.00, 2 years, Home, Loan Interest: $8,000",
        "50000.00, 7 years, Home, Loan Interest: $22,750",
        "50000.00, 13 years, Home, Loan Interest: $35,750",
        "150000.00, 5 years, Home, Loan Interest: $48,750",
        "550000.00, 20 years, Home, Loan Interest: $605,000",
        "150000.00, 15 years, Home, Loan can't be given for entered values, check your relevant manual",
        "600000.00, 5 years, Home, Loan can't be given for entered values, check your relevant manual",
        "50000.00, 2 years, Property, Loan Interest: $12,000",
        "50000.00, 7 years, Property, Loan Interest: $29,750",
        "50000.00, 13 years, Property, Loan Interest: $45,500",
        "150000.00, 5 years, Property, Loan Interest: $63,750",
        "550000.00, 20 years, Property, Loan Interest: $770,000",
        "50000.00, 10 years, Property, Loan can't be given for entered values, check your relevant manual",
        "150000.00, 15 years, Property, Loan can't be given for entered values, check your relevant manual",
        "600000.00, 5 years, Property, Loan can't be given for entered values, check your relevant manual"
    })
    public void testLoanInterestCalculation(String loanAmount, String loanYear, String loanType, String expectedResult) {
        FxRobot robot = new FxRobot();

        robot.clickOn("#calculateLoanInterestButton");
        robot.clickOn("#loanAmountField").write(loanAmount);

        selectLoanYear(robot, loanYear);

        robot.clickOn("#loanTypeField").clickOn(loanType);
        robot.clickOn("#submitButton");

        // Wait for the result to be visible
        String actualResult = WaitForAsyncUtils.waitForAsync(20, ()
                -> robot.lookup("#resultLabel").queryAs(Label.class).getText());

        if (expectedResult.contains("Interest:")) {
            assertTrue(actualResult.contains(expectedResult), "Loan Interest result mismatch");
        } else {
            assertTrue(actualResult.contains(expectedResult), "Error message mismatch");
        }
    }

    // Test Loan Balance
    @ParameterizedTest
    @CsvSource({
        "Balance: $54055.56"
    })
    public void testLoanBalanceCalculation(String expectedResult) {
        FxRobot robot = new FxRobot(); // Use a new instance of FxRobot if needed.

        robot.clickOn("#loanBalanceButton");
        robot.clickOn("#loanBalanceButton");

        robot.clickOn("#submitButton");

        // Wait for the result to be visible
        String actualResult = WaitForAsyncUtils.waitForAsync(20, ()
                -> robot.lookup("#resultLabel").queryAs(Label.class).getText()
        );

        if (expectedResult.contains("Balance:")) {
            assertTrue(actualResult.contains(expectedResult), "Loan Interest result mismatch");
        } else {
            assertTrue(actualResult.contains(expectedResult), "Error message mismatch");
        }

    }

    private String getAlertDialogMessage() {
        return lookup(".dialog-pane .content").queryLabeled().getText();
    }

    @Test
    public void testLoanTypeNotSelected() {
        clickOn("#calculateLoanInterestButton");

        clickOn("#submitButton");

        String alertMessage = getAlertDialogMessage();
        assertEquals("Select a Loan Type from the options", alertMessage);
    }

    @Test
    public void testLoanAmountFieldEmpty() {
        clickOn("#calculateLoanInterestButton");

        clickOn("#loanTypeField").clickOn("Home");

        clickOn("#submitButton");

        String alertMessage = getAlertDialogMessage();
        assertEquals("Enter an amount", alertMessage);
    }

    @Test
    public void testLoanAmountNotANumber() {
        clickOn("#calculateLoanInterestButton");

        clickOn("#loanTypeField").clickOn("Home");

        clickOn("#loanAmountField").write("test");

        clickOn("#submitButton");

        String alertMessage = getAlertDialogMessage();
        assertEquals("Invalid input. Please enter a valid amount.", alertMessage);
    }

    // Test Case 36: Amount less than $5000
    @Test
    public void testLoanAmountLessThan5000() {
        clickOn("#calculateLoanInterestButton");

        clickOn("#loanTypeField").clickOn("Home");

        clickOn("#loanAmountField").write("1000");

        clickOn("#submitButton");

        String alertMessage = getAlertDialogMessage();
        assertEquals("Unable to process. Amount should be more than $5,000.", alertMessage);
    }

    @Test
    public void testLoanYearNotSelected() {
        clickOn("#calculateLoanInterestButton");

        clickOn("#loanTypeField").clickOn("Home");

        clickOn("#loanAmountField").write("6000");

        clickOn("#submitButton");

        String alertMessage = getAlertDialogMessage();
        assertEquals("Select an Year from the options", alertMessage);
    }

    public void selectLoanYear(FxRobot robot, String loanYear) {
        robot.clickOn("#loanYearField");

        ComboBox<String> loanYearComboBox = robot.lookup("#loanYearField").queryComboBox();
        WaitForAsyncUtils.waitForAsync(2, () -> loanYearComboBox.isShowing());

        for (String year : loanYearComboBox.getItems()) {
            if (year.equals(loanYear)) {
                robot.press(KeyCode.DOWN).release(KeyCode.DOWN);

                robot.clickOn(year); // Click on the item directly

                return; 

            }

            // If the item is not visible, scroll down
            robot.press(KeyCode.DOWN).release(KeyCode.DOWN);
        }

        throw new RuntimeException("Loan year " + loanYear + " not found in ComboBox.");
    }

}
