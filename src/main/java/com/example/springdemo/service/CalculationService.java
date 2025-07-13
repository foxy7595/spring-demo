package com.example.springdemo.service;

import com.example.springdemo.dto.CalculationDto;
import com.example.springdemo.dto.CalculationResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    private static final Logger logger = LoggerFactory.getLogger(CalculationService.class);

    @Cacheable("calculation")
    public CalculationResponseDto add(CalculationDto dto) {
        logger.info("Adding numbers: {} + {}", dto.getNumber1(), dto.getNumber2());
        
        Double result = dto.getNumber1() + dto.getNumber2();
        
        logger.info("Addition result: {}", result);
        
        return new CalculationResponseDto(
            dto.getNumber1(),
            dto.getNumber2(),
            result,
            "addition"
        );
    }

    @Cacheable("calculation")
    public CalculationResponseDto subtract(CalculationDto dto) {
        logger.info("Subtracting numbers: {} - {}", dto.getNumber1(), dto.getNumber2());
        
        Double result = dto.getNumber1() - dto.getNumber2();
        
        logger.info("Subtraction result: {}", result);
        
        return new CalculationResponseDto(
            dto.getNumber1(),
            dto.getNumber2(),
            result,
            "subtraction"
        );
    }

    @Cacheable("calculation")
    public CalculationResponseDto multiply(CalculationDto dto) {
        logger.info("Multiplying numbers: {} * {}", dto.getNumber1(), dto.getNumber2());
        
        Double result = dto.getNumber1() * dto.getNumber2();
        
        logger.info("Multiplication result: {}", result);
        
        return new CalculationResponseDto(
            dto.getNumber1(),
            dto.getNumber2(),
            result,
            "multiplication"
        );
    }

    @Cacheable("calculation")
    public CalculationResponseDto divide(CalculationDto dto) {
        logger.info("Dividing numbers: {} / {}", dto.getNumber1(), dto.getNumber2());
        
        if (dto.getNumber2() == 0) {
            logger.error("Division by zero attempted: {} / {}", dto.getNumber1(), dto.getNumber2());
            throw new IllegalArgumentException("Division by zero is not allowed");
        }
        
        Double result = dto.getNumber1() / dto.getNumber2();
        
        logger.info("Division result: {}", result);
        
        return new CalculationResponseDto(
            dto.getNumber1(),
            dto.getNumber2(),
            result,
            "division"
        );
    }
} 