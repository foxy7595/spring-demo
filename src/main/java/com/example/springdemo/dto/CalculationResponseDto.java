package com.example.springdemo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Calculation Response Data Transfer Object")
public class CalculationResponseDto {

    @Schema(description = "First number used in calculation", example = "10.0")
    private Double number1;

    @Schema(description = "Second number used in calculation", example = "5.0")
    private Double number2;

    @Schema(description = "Result of the calculation", example = "15.0")
    private Double result;

    @Schema(description = "Type of operation performed", example = "addition", allowableValues = {"addition", "subtraction", "multiplication", "division"})
    private String operation;

    @Schema(description = "Success message", example = "Calculation completed successfully")
    private String message;

    // Default constructor
    public CalculationResponseDto() {}

    // Constructor with parameters
    public CalculationResponseDto(Double number1, Double number2, Double result, String operation) {
        this.number1 = number1;
        this.number2 = number2;
        this.result = result;
        this.operation = operation;
        this.message = "Calculation completed successfully";
    }

    // Constructor with custom message
    public CalculationResponseDto(Double number1, Double number2, Double result, String operation, String message) {
        this.number1 = number1;
        this.number2 = number2;
        this.result = result;
        this.operation = operation;
        this.message = message;
    }

    // Getters and Setters
    public Double getNumber1() {
        return number1;
    }

    public void setNumber1(Double number1) {
        this.number1 = number1;
    }

    public Double getNumber2() {
        return number2;
    }

    public void setNumber2(Double number2) {
        this.number2 = number2;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CalculationResponseDto{" +
                "number1=" + number1 +
                ", number2=" + number2 +
                ", result=" + result +
                ", operation='" + operation + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalculationResponseDto that = (CalculationResponseDto) o;

        if (number1 != null ? !number1.equals(that.number1) : that.number1 != null) return false;
        if (number2 != null ? !number2.equals(that.number2) : that.number2 != null) return false;
        if (result != null ? !result.equals(that.result) : that.result != null) return false;
        if (operation != null ? !operation.equals(that.operation) : that.operation != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;
    }

    @Override
    public int hashCode() {
        int result1 = number1 != null ? number1.hashCode() : 0;
        result1 = 31 * result1 + (number2 != null ? number2.hashCode() : 0);
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        result1 = 31 * result1 + (operation != null ? operation.hashCode() : 0);
        result1 = 31 * result1 + (message != null ? message.hashCode() : 0);
        return result1;
    }
} 