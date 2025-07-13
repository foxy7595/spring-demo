package com.example.springdemo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;

@Schema(description = "Calculation Data Transfer Object")
public class CalculationDto {

    @Schema(description = "First number for calculation", example = "10.0", required = true)
    @NotNull(message = "First number is required")
    @DecimalMin(value = "-999999999.0", message = "First number must be greater than or equal to -999999999")
    @DecimalMax(value = "999999999.0", message = "First number must be less than or equal to 999999999")
    private Double number1;

    @Schema(description = "Second number for calculation", example = "5.0", required = true)
    @NotNull(message = "Second number is required")
    @DecimalMin(value = "-999999999.0", message = "Second number must be greater than or equal to -999999999")
    @DecimalMax(value = "999999999.0", message = "Second number must be less than or equal to 999999999")
    private Double number2;

    // Default constructor
    public CalculationDto() {}

    // Constructor with parameters
    public CalculationDto(Double number1, Double number2) {
        this.number1 = number1;
        this.number2 = number2;
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

    @Override
    public String toString() {
        return "CalculationDto{" +
                "number1=" + number1 +
                ", number2=" + number2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalculationDto that = (CalculationDto) o;

        if (number1 != null ? !number1.equals(that.number1) : that.number1 != null) return false;
        return number2 != null ? number2.equals(that.number2) : that.number2 == null;
    }

    @Override
    public int hashCode() {
        int result = number1 != null ? number1.hashCode() : 0;
        result = 31 * result + (number2 != null ? number2.hashCode() : 0);
        return result;
    }
} 