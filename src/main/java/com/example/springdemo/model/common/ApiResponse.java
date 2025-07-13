package com.example.springdemo.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API Response Wrapper")
public class ApiResponse<T> {

    @Schema(description = "Response status", example = "success")
    private String status;

    @Schema(description = "HTTP status code", example = "200")
    private int code;

    @Schema(description = "Response message", example = "Operation completed successfully")
    private String message;

    @Schema(description = "Response data payload")
    private T data;

    @Schema(description = "Response timestamp")
    private LocalDateTime timestamp;

    @Schema(description = "Error details (if any)")
    private List<String> errors;

    @Schema(description = "Request path", example = "/api/calculator/add")
    private String path;

    @Schema(description = "Response metadata")
    private Metadata metadata;

    // Default constructor
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // Success constructor
    public ApiResponse(String status, int code, String message, T data, String path) {
        this();
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
        this.path = path;
    }

    // Error constructor
    public ApiResponse(String status, int code, String message, List<String> errors, String path) {
        this();
        this.status = status;
        this.code = code;
        this.message = message;
        this.errors = errors;
        this.path = path;
    }

    // Static factory methods for success responses
    public static <T> ApiResponse<T> success(T data, String message, String path) {
        return new ApiResponse<>("success", 200, message, data, path);
    }

    public static <T> ApiResponse<T> success(T data, String path) {
        return success(data, "Operation completed successfully", path);
    }

    public static <T> ApiResponse<T> success(String message, String path) {
        return new ApiResponse<>("success", 200, message, null, path);
    }

    // Static factory methods for error responses
    public static <T> ApiResponse<T> error(int code, String message, List<String> errors, String path) {
        return new ApiResponse<>("error", code, message, errors, path);
    }

    public static <T> ApiResponse<T> error(int code, String message, String path) {
        return error(code, message, List.of(message), path);
    }

    public static <T> ApiResponse<T> badRequest(String message, String path) {
        return error(400, message, path);
    }

    public static <T> ApiResponse<T> notFound(String message, String path) {
        return error(404, message, path);
    }

    public static <T> ApiResponse<T> internalError(String message, String path) {
        return error(500, message, path);
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    // Inner class for metadata
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Response Metadata")
    public static class Metadata {
        @Schema(description = "Total count for paginated responses")
        private Long total;

        @Schema(description = "Page number for paginated responses")
        private Integer page;

        @Schema(description = "Page size for paginated responses")
        private Integer size;

        @Schema(description = "Processing time in milliseconds")
        private Long processingTime;

        @Schema(description = "Cache status")
        private String cacheStatus;

        public Metadata() {}

        public Metadata(Long total, Integer page, Integer size) {
            this.total = total;
            this.page = page;
            this.size = size;
        }

        public Metadata(Long processingTime, String cacheStatus) {
            this.processingTime = processingTime;
            this.cacheStatus = cacheStatus;
        }

        // Getters and Setters
        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public Long getProcessingTime() {
            return processingTime;
        }

        public void setProcessingTime(Long processingTime) {
            this.processingTime = processingTime;
        }

        public String getCacheStatus() {
            return cacheStatus;
        }

        public void setCacheStatus(String cacheStatus) {
            this.cacheStatus = cacheStatus;
        }
    }
} 