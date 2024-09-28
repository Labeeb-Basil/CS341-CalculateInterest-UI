package com.cs341.a1.calculateinterestui;

import java.util.Arrays;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    private Label operationTile;
    private ToggleGroup operationToggleGroup;
    private ToggleButton calculateLoanAmountButton;
    private ToggleButton calculateLoanInterestButton;
    private ToggleButton loanBalanceButton;

    private TextField loanAmountField;
    private ComboBox loanYearField;
    private ComboBox loanTypeField;

    private Label resultLabel;

    private String gAvailableYears[];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Loan Calculator");

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createTopSection());
        borderPane.setCenter(createCenterSection());
        borderPane.setPadding(new Insets(10));
        borderPane.getStyleClass().add("border-pane");

        Scene scene = new Scene(borderPane, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/Styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private HBox createTopSection() {
        HBox topSection = new HBox();
        topSection.getStyleClass().add("hbox-top");

        Label headingLabel = new Label("Loan Calculator");
        headingLabel.getStyleClass().add("heading");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topSection.setAlignment(Pos.CENTER_LEFT);
        topSection.setSpacing(20);

        topSection.getChildren().add(headingLabel);
        topSection.getChildren().add(spacer);
        topSection.getChildren().add(createToggleButtons());

        return topSection;
    }

    private VBox createCenterSection() {
        VBox center = new VBox();
        center.getStyleClass().add("center-container");

        // Create the heading
        operationTile = new Label("Select an operation");
        operationTile.getStyleClass().add("operation-tile");

        VBox.setVgrow(operationTile, Priority.ALWAYS);

        HBox contentBox = new HBox(30);
        contentBox.getStyleClass().add("hbox");
        contentBox.setPadding(new Insets(20));
        contentBox.setAlignment(Pos.CENTER);

        VBox leftBox = createInputBox();
        leftBox.getStyleClass().add("left-box");
        leftBox.setMaxWidth(Double.MAX_VALUE);

        VBox rightBox = createOutputBox();
        rightBox.getStyleClass().add("right-box");
        HBox.setHgrow(rightBox, Priority.SOMETIMES);

        leftBox.setPrefWidth(400);
        rightBox.setPrefWidth(300);

        contentBox.getChildren().addAll(leftBox, rightBox);

        VBox contentContainer = new VBox();
        contentContainer.getStyleClass().add("vbox");
        contentContainer.setSpacing(10);
        contentContainer.setPadding(new Insets(10));

        contentContainer.getChildren().addAll(operationTile, contentBox);

        center.getChildren().add(contentContainer);

        return center;
    }

    private HBox createToggleButtons() {
        operationToggleGroup = new ToggleGroup();
        HBox toggleButtons = new HBox();
        toggleButtons.getStyleClass().add("tb-container");
        toggleButtons.setSpacing(15);

        calculateLoanAmountButton = new ToggleButton("Calculate Loan Amount");
        calculateLoanAmountButton.setToggleGroup(operationToggleGroup);
        calculateLoanAmountButton.getStyleClass().add("toggle-button");

        calculateLoanInterestButton = new ToggleButton("Calculate Loan Interest");
        calculateLoanInterestButton.setToggleGroup(operationToggleGroup);
        calculateLoanInterestButton.getStyleClass().add("toggle-button");

        loanBalanceButton = new ToggleButton("Loan Balance");
        loanBalanceButton.setToggleGroup(operationToggleGroup);
        loanBalanceButton.getStyleClass().add("toggle-button");

        toggleButtons.getChildren().addAll(calculateLoanAmountButton, calculateLoanInterestButton, loanBalanceButton);

        operationToggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle == null) {
                operationToggleGroup.selectToggle(oldToggle);
            } else {
                if (newToggle == loanBalanceButton) {
                    setInputFieldsDisabled(3);
                    operationTile.setText("Loan Balance");
                } else if (newToggle == calculateLoanAmountButton) {
                    setInputFieldsDisabled(1);
                    operationTile.setText("Calculate Loan Amount");
                } else if (newToggle == calculateLoanInterestButton) {
                    setInputFieldsDisabled(2);
                    operationTile.setText("Calculate Loan Interest");
                }
            }
        });

        return toggleButtons;
    }

    // container for input fields.
    private VBox createInputBox() {
        VBox inputBox = new VBox();
        inputBox.setSpacing(15);
        inputBox.setAlignment(Pos.CENTER);

        // Loan Type Label and ComboBox
        Label loanTypeLabel = new Label("Loan Type:");
        loanTypeLabel.getStyleClass().add("input-label");
        loanTypeField = new ComboBox<>();
        loanTypeField.getItems().addAll("Home", "Property");
        loanTypeField.setDisable(true);
        loanTypeField.setPromptText("Select Loan Type");

        // Add Loan Type components to VBox
        VBox loanTypeBox = new VBox(loanTypeLabel, loanTypeField);
        loanTypeBox.setAlignment(Pos.CENTER_LEFT);
        inputBox.getChildren().add(loanTypeBox);

        // Loan Amount Label and TextField
        Label loanAmountLabel = new Label("Amount:");
        loanAmountLabel.getStyleClass().add("input-label");
        loanAmountField = new TextField();
        loanAmountField.setDisable(true);
        loanAmountField.setPromptText("Enter Loan Amount");

        // Add Loan Amount components to VBox
        VBox loanAmountBox = new VBox(loanAmountLabel, loanAmountField);
        loanAmountBox.setAlignment(Pos.CENTER_LEFT);
        inputBox.getChildren().add(loanAmountBox);

        // Loan Year Label and ComboBox
        Label loanYearLabel = new Label("Year:");
        loanYearLabel.getStyleClass().add("input-label");
        loanYearField = new ComboBox<>();
        loanYearField.getItems().addAll("1 Year", "2 Years", "3 Years", "5 Years", "10 Years");
        loanYearField.setDisable(true);
        loanYearField.setPromptText("Select Loan Year");

        // Add Loan Year components to VBox
        VBox loanYearBox = new VBox(loanYearLabel, loanYearField);
        loanYearBox.setAlignment(Pos.CENTER_LEFT);
        inputBox.getChildren().add(loanYearBox);

        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("toggle-button");
        submitButton.setOnAction(event -> {
            if (loanBalanceButton.isSelected()) {
                String[] result = CalcInterest.computeLoanBalance();
                resultLabel.setText(result[0]);
                return;
            }
            if (validateAmount() && validateFields()) {
                String loanAmountValue = loanAmountField.getText();
                String loanTypeValue = (String) loanTypeField.getValue();
                String loanYearValue = (String) loanYearField.getValue();

                // Process loan type: if "Home" then 1, else 2
                int loanType = loanTypeValue.equals("Home") ? 1 : 2;

                loanYearValue = loanYearValue.replaceAll("[^\\d]", "");

                double loanAmount = 0;
                try {
                    loanAmount = Double.parseDouble(loanAmountValue);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid loan amount: " + loanAmountValue);
                    return;
                }
                if (calculateLoanAmountButton.isSelected()) {
                    String[] result = CalcInterest.computeLoanAmountValue(loanAmount, Integer.parseInt(loanYearValue), loanType);
                    resultLabel.setText(result[0]);
                    resultLabel.setTextFill(Color.web(result[1]));
                } else if (calculateLoanInterestButton.isSelected()) {
                    String[] result = CalcInterest.computeLoanInterestValue(loanAmount, Integer.parseInt(loanYearValue), loanType);
                    resultLabel.setText(result[0]);
                    resultLabel.setTextFill(Color.web(result[1]));
                }
            }

        });
        inputBox.getChildren().add(submitButton);

        loanTypeField.setOnAction(event -> {
            if (loanTypeField.getValue() != null) {
                loanAmountField.setDisable(false);
            }
            updateLoanYearField(loanAmountField.getText());
        });

        loanAmountField.textProperty().addListener((observable, oldValue, newValue) -> updateLoanYearField(newValue));

        return inputBox;
    }

    // container for the results of calculation.
    private VBox createOutputBox() {
        VBox outputBox = new VBox();
        outputBox.setSpacing(10);
        outputBox.setAlignment(Pos.TOP_LEFT); // Align to top left

        Label outputLabel = new Label("Output:");
        outputLabel.getStyleClass().add("heading-io");

        // No need to set Vgrow for outputLabel as it should not grow
        outputBox.getChildren().add(outputLabel);

        resultLabel = new Label("Do a calculation");
        resultLabel.getStyleClass().add("output-label");
        resultLabel.setTextFill(Color.web("#326569"));
        outputBox.getChildren().add(resultLabel);

        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("toggle-button");
        submitButton.setOnAction(event -> resultLabel.setText(""));

        return outputBox;
    }

    // function that disables/enables the input fields based on selected operation and clears them.
    private void setInputFieldsDisabled(int option) {
        boolean disabled = true;
        if (option < 3) { // disable only year and amount fields
            loanTypeField.setDisable(!disabled);
            loanAmountField.setDisable(disabled);
            loanYearField.setDisable(disabled);
        } else { // disable all fields
            loanTypeField.setDisable(disabled);
            loanAmountField.setDisable(disabled);
            loanYearField.setDisable(disabled);
        }
        if (disabled) { // clear fields
            loanAmountField.clear();
            loanYearField.getSelectionModel().clearSelection();
            loanTypeField.getSelectionModel().clearSelection();
        }
    }

    // Function to provide correct year values for dropdown based on type and amount.
    // Based on requirements.
    private void updateLoanYearField(String value) {
        if (value != null && !value.trim().isEmpty()) {
            try {
                double loanAmount = Double.parseDouble(loanAmountField.getText());
                int loanType = loanTypeField.getValue() != null ? (loanTypeField.getValue().equals("Home") ? 1 : 2) : 0;

                loanYearField.setDisable(false);
                updateLoanYearOptionsBasedOnAmount(loanAmount, loanType); // call the method from the CalcInterest class

            } catch (NumberFormatException e) { // if the amount field does not contain a valid double value
                loanYearField.getSelectionModel().clearSelection();
                loanYearField.setPromptText("Select Loan Year");
                loanYearField.setDisable(true);
            }
        } else { // if input field is set to null or empty.
            loanYearField.getSelectionModel().clearSelection();
            loanYearField.setPromptText("Select Loan Year");
            loanYearField.setDisable(true);
        }

    }

    // helper function to get loan year values based on input
    private void updateLoanYearOptionsBasedOnAmount(double loanAmount, int loanType) {
        String[] availableYears = CalcInterest.availableYearLoan(loanAmount, loanType);

        if (gAvailableYears != null && Arrays.equals(gAvailableYears, availableYears)) {
            return;
        }

        gAvailableYears = Arrays.copyOf(availableYears, availableYears.length);

        loanYearField.getItems().clear();
        loanYearField.getItems().addAll(Arrays.asList(gAvailableYears));
        loanYearField.setPromptText("Select Loan Year");
    }

    // helper method that validates the free form amount input.
    private boolean validateAmount() {
        String amountText = loanAmountField.getText();

        try {
            double amount = Double.parseDouble(amountText);

            if (amount < 5000) {
                showErrorMessage("Unable to process. Amount should be more than $5,000.");
                return false;
            }

            return true;

        } catch (NumberFormatException e) { // if a non double value is entered.
            showErrorMessage("Invalid input. Please enter a valid amount.");
            return false;
        }
    }

    // helper method that checks if all fields are not null and if a valid operation was selected.
    private boolean validateFields() {
        String loanAmountText = loanAmountField.getText();
        String loanType = (String) loanTypeField.getValue();
        String loanYear = (String) loanYearField.getValue();

        if (operationToggleGroup.getSelectedToggle() == null) {
            showErrorMessage("Select an operation from the top section");
        }
        if (loanType == null) {
            showErrorMessage("Select an option from the Type options");
            return false;
        }

        if (loanAmountText.isEmpty()) {
            showErrorMessage("Fill in the amount field");
            return false;
        }

        if (loanYear == null) {
            showErrorMessage("Select an option from the Year options");
            return false;
        }

        return true;
    }

    // helper method that shows an alert dialog box and can be reused.
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Calculation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
