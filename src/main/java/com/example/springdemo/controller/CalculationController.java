package com.example.springdemo.controller;

import com.example.springdemo.controller.common.BaseController;
import com.example.springdemo.dto.CalculationDto;
import com.example.springdemo.dto.CalculationResponseDto;
import com.example.springdemo.model.common.ApiResponse;
import com.example.springdemo.service.CalculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculator")
@Tag(name = "Calculator API", description = "Mathematical operations API for performing calculations on two numbers")
public class CalculationController extends BaseController {

    private final CalculationService calculationService;

    @Autowired
    public CalculationController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @PostMapping("/add")
    @Operation(
        summary = "Add two numbers",
        description = "Performs addition of two numbers and returns the result with operation details"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Addition completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Success Example",
                    value = "{\"status\": \"success\", \"code\": 200, \"message\": \"Addition completed successfully\", \"data\": {\"number1\": 10.0, \"number2\": 5.0, \"result\": 15.0, \"operation\": \"addition\", \"message\": \"Calculation completed successfully\"}, \"timestamp\": \"2025-07-13T11:30:00\", \"path\": \"/api/calculator/add\"}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - Invalid input data"
        )
    })
    public ResponseEntity<ApiResponse<CalculationResponseDto>> add(
            @Parameter(
                description = "Calculation request containing two numbers",
                required = true,
                content = @Content(
                    examples = @ExampleObject(
                        name = "Addition Request",
                        value = "{\"number1\": 10, \"number2\": 5}"
                    )
                )
            )
            @Valid @RequestBody CalculationDto dto) {
        
        logRequest("Addition", dto);
        
        try {
            CalculationResponseDto response = calculationService.add(dto);
            ApiResponse<CalculationResponseDto> apiResponse = successResponse(response, "Addition completed successfully");
            logResponse("Addition", apiResponse);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            logError("Addition", e.getMessage(), e);
            return ResponseEntity.badRequest().body(badRequestResponse(e.getMessage()));
        }
    }

    @PostMapping("/subtract")
    @Operation(
        summary = "Subtract two numbers",
        description = "Performs subtraction of two numbers and returns the result with operation details"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Subtraction completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Success Example",
                    value = "{\"status\": \"success\", \"code\": 200, \"message\": \"Subtraction completed successfully\", \"data\": {\"number1\": 10.0, \"number2\": 5.0, \"result\": 5.0, \"operation\": \"subtraction\", \"message\": \"Calculation completed successfully\"}, \"timestamp\": \"2025-07-13T11:30:00\", \"path\": \"/api/calculator/subtract\"}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - Invalid input data"
        )
    })
    public ResponseEntity<ApiResponse<CalculationResponseDto>> subtract(
            @Parameter(
                description = "Calculation request containing two numbers",
                required = true,
                content = @Content(
                    examples = @ExampleObject(
                        name = "Subtraction Request",
                        value = "{\"number1\": 10, \"number2\": 5}"
                    )
                )
            )
            @Valid @RequestBody CalculationDto dto) {
        
        logRequest("Subtraction", dto);
        
        try {
            CalculationResponseDto response = calculationService.subtract(dto);
            ApiResponse<CalculationResponseDto> apiResponse = successResponse(response, "Subtraction completed successfully");
            logResponse("Subtraction", apiResponse);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            logError("Subtraction", e.getMessage(), e);
            return ResponseEntity.badRequest().body(badRequestResponse(e.getMessage()));
        }
    }

    @PostMapping("/multiply")
    @Operation(
        summary = "Multiply two numbers",
        description = "Performs multiplication of two numbers and returns the result with operation details"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Multiplication completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Success Example",
                    value = "{\"status\": \"success\", \"code\": 200, \"message\": \"Multiplication completed successfully\", \"data\": {\"number1\": 10.0, \"number2\": 5.0, \"result\": 50.0, \"operation\": \"multiplication\", \"message\": \"Calculation completed successfully\"}, \"timestamp\": \"2025-07-13T11:30:00\", \"path\": \"/api/calculator/multiply\"}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - Invalid input data"
        )
    })
    public ResponseEntity<ApiResponse<CalculationResponseDto>> multiply(
            @Parameter(
                description = "Calculation request containing two numbers",
                required = true,
                content = @Content(
                    examples = @ExampleObject(
                        name = "Multiplication Request",
                        value = "{\"number1\": 10, \"number2\": 5}"
                    )
                )
            )
            @Valid @RequestBody CalculationDto dto) {
        
        logRequest("Multiplication", dto);
        
        try {
            CalculationResponseDto response = calculationService.multiply(dto);
            ApiResponse<CalculationResponseDto> apiResponse = successResponse(response, "Multiplication completed successfully");
            logResponse("Multiplication", apiResponse);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            logError("Multiplication", e.getMessage(), e);
            return ResponseEntity.badRequest().body(badRequestResponse(e.getMessage()));
        }
    }

    @PostMapping("/divide")
    @Operation(
        summary = "Divide two numbers",
        description = "Performs division of two numbers and returns the result with operation details. Division by zero is not allowed."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Division completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Success Example",
                    value = "{\"status\": \"success\", \"code\": 200, \"message\": \"Division completed successfully\", \"data\": {\"number1\": 10.0, \"number2\": 5.0, \"result\": 2.0, \"operation\": \"division\", \"message\": \"Calculation completed successfully\"}, \"timestamp\": \"2025-07-13T11:30:00\", \"path\": \"/api/calculator/divide\"}"
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - Division by zero or invalid input data"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Internal server error"
        )
    })
    public ResponseEntity<ApiResponse<CalculationResponseDto>> divide(
            @Parameter(
                description = "Calculation request containing two numbers (number2 cannot be zero)",
                required = true,
                content = @Content(
                    examples = @ExampleObject(
                        name = "Division Request",
                        value = "{\"number1\": 10, \"number2\": 5}"
                    )
                )
            )
            @Valid @RequestBody CalculationDto dto) {
        
        logRequest("Division", dto);
        
        try {
            CalculationResponseDto response = calculationService.divide(dto);
            ApiResponse<CalculationResponseDto> apiResponse = successResponse(response, "Division completed successfully");
            logResponse("Division", apiResponse);
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            logError("Division", e.getMessage(), e);
            return ResponseEntity.badRequest().body(badRequestResponse(e.getMessage()));
        } catch (Exception e) {
            logError("Division", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(internalErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/add")
    @Operation(
        summary = "Add two numbers (Simple GET)",
        description = "Performs addition using query parameters for simple testing"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Addition completed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Bad request - Invalid parameters"
        )
    })
    public ResponseEntity<ApiResponse<CalculationResponseDto>> addSimple(
            @Parameter(description = "First number", example = "10.0", required = true)
            @RequestParam Double number1,
            @Parameter(description = "Second number", example = "5.0", required = true)
            @RequestParam Double number2) {
        
        logRequest("Simple Addition", "number1=" + number1 + ", number2=" + number2);
        
        CalculationDto dto = new CalculationDto(number1, number2);
        return add(dto);
    }

    @GetMapping("/health")
    @Operation(
        summary = "Health check",
        description = "Returns the health status of the calculator API"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "API is healthy",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class),
                examples = @ExampleObject(
                    name = "Health Response",
                    value = "{\"status\": \"success\", \"code\": 200, \"message\": \"Calculator API is running!\", \"data\": null, \"timestamp\": \"2025-07-13T11:30:00\", \"path\": \"/api/calculator/health\"}"
                )
            )
        )
    })
    public ResponseEntity<ApiResponse<String>> health() {
        logRequest("Health Check", "Health check requested");
        ApiResponse<String> apiResponse = successResponse("Calculator API is running!");
        logResponse("Health Check", apiResponse);
        return ResponseEntity.ok(apiResponse);
    }
} 