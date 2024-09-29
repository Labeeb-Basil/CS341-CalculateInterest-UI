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
import javafx.scene.text.Font;

public class App extends Application {

    private Label operationTile;
    private ToggleGroup operationToggleGroup;
    private ToggleButton calculateLoanAmountButton;
    private ToggleButton calculateLoanInterestButton;
    private ToggleButton loanBalanceButton;

    private TextField loanAmountField;
    private ComboBox loanYearField;
    private ComboBox loanTypeField;
    private Button submitButton;

    private Label resultLabel;

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
        calculateLoanAmountButton.setId("calculateLoanAmountButton");

        calculateLoanInterestButton = new ToggleButton("Calculate Loan Interest");
        calculateLoanInterestButton.setToggleGroup(operationToggleGroup);
        calculateLoanInterestButton.getStyleClass().add("toggle-button");
        calculateLoanInterestButton.setId("calculateLoanInterestButton"); // Set ID

        loanBalanceButton = new ToggleButton("Loan Balance");
        loanBalanceButton.setToggleGroup(operationToggleGroup);
        loanBalanceButton.getStyleClass().add("toggle-button");
        loanBalanceButton.setId("loanBalanceButton"); // Set ID

        toggleButtons.getChildren().addAll(calculateLoanAmountButton, calculateLoanInterestButton, loanBalanceButton);

        operationToggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle == null) {
                operationToggleGroup.selectToggle(oldToggle);
            } else {
                if (newToggle == loanBalanceButton) {
                    setInputFieldsDisabled(true);
                    operationTile.setText("Loan Balance");
                } else if (newToggle == calculateLoanAmountButton) {
                    setInputFieldsDisabled(false);
                    operationTile.setText("Calculate Loan Amount");
                } else if (newToggle == calculateLoanInterestButton) {
                    setInputFieldsDisabled(false);
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
        loanTypeField.setId("loanTypeField");

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
        loanAmountField.setId("loanAmountField");

        // Add Loan Amount components to VBox
        VBox loanAmountBox = new VBox(loanAmountLabel, loanAmountField);
        loanAmountBox.setAlignment(Pos.CENTER_LEFT);
        inputBox.getChildren().add(loanAmountBox);

        // Loan Year Label and ComboBox
        Label loanYearLabel = new Label("Year:");
        loanYearLabel.getStyleClass().add("input-label");
        loanYearField = new ComboBox<>();
        loanYearField.getItems().addAll(Arrays.asList(generateArray(1, 100)));
        loanYearField.setDisable(true);
        loanYearField.setPromptText("Select Loan Year");
        loanYearField.setId("loanYearField");

        // Add Loan Year components to VBox
        VBox loanYearBox = new VBox(loanYearLabel, loanYearField);
        loanYearBox.setAlignment(Pos.CENTER_LEFT);
        inputBox.getChildren().add(loanYearBox);

        submitButton = new Button("Submit");
        submitButton.getStyleClass().add("toggle-button");
        submitButton.setId("submitButton");
        submitButton.setDisable(true);
        submitButton.setOnAction(event -> {
            if (loanBalanceButton.isSelected()) {
                String[] result = CalculateInterestUI.computeLoanBalance();
                resultLabel.setText(result[0]);
                resultLabel.setTextFill(Color.web(result[1]));
                resultLabel.setId("resultLabel");
                return;
            }
            if (validateFields()) {
                String loanAmountValue = loanAmountField.getText();
                String loanTypeValue = (String) loanTypeField.getValue();
                String loanYearValue = (String) loanYearField.getValue();

                int loanType = loanTypeValue.equals("Home") ? 1 : 2;
                double loanAmount = Double.parseDouble(loanAmountValue);
                int loanYear = Integer.parseInt(loanYearValue.replaceAll("[^\\d]", ""));

                String[] result;
                if (calculateLoanAmountButton.isSelected()) {
                    result = CalculateInterestUI.computeLoanAmountValue(loanAmount, loanYear, loanType);
                } else { // calculateLoanInterestButton is selected
                    result = CalculateInterestUI.computeLoanInterestValue(loanAmount, loanYear, loanType);
                }

                resultLabel.setText(result[0]);
                resultLabel.setTextFill(Color.web(result[1]));
                resultLabel.setFont(new Font(30.0));
                resultLabel.setId("resultLabel");
            }

        });
        inputBox.getChildren().add(submitButton);

        return inputBox;
    }

    // container for the results of calculation.
    private VBox createOutputBox() {
        VBox outputBox = new VBox();
        outputBox.setSpacing(5);
        outputBox.setAlignment(Pos.TOP_LEFT); // Align to top left

        Label outputLabel = new Label("Output:");
        outputLabel.getStyleClass().add("heading-output");

        // No need to set Vgrow for outputLabel as it should not grow
        outputBox.getChildren().add(outputLabel);

        resultLabel = new Label("Do a calculation");
        resultLabel.getStyleClass().add("result-label");
        resultLabel.setId("resultLabel");
        outputBox.getChildren().add(resultLabel);

        return outputBox;
    }

    // function that disables/enables the input fields based on selected operation and clears them.
    private void setInputFieldsDisabled(boolean disabled) {
        loanTypeField.setDisable(disabled);
        loanAmountField.setDisable(disabled);
        loanYearField.setDisable(disabled);
        submitButton.setDisable(false);

        loanAmountField.clear();
        loanYearField.getSelectionModel().clearSelection();
        loanTypeField.getSelectionModel().clearSelection();
    }

    // helper function to get loan year values based on input
    public static String[] generateArray(int start, int end) {
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
            showErrorMessage("Select a Loan Type from the options");
            return false;
        }

        if (loanAmountText.isEmpty()) {
            showErrorMessage("Enter an amount");
            return false;
        } else if (!validateAmount()) {
            return false;
        }

        if (loanYear == null) {
            showErrorMessage("Select an Year from the options");
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
