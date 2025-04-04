package com.exemplo.pdfapi.domain.error;

import java.util.List;

/**
 * Represents an API error response containing the HTTP status, an error message, 
 * and a list of specific errors.
 */
public class ApiErrorResponse {

    private int status;

    private String message;
    
    private List<String> errors;

    /**
     * Constructs a new ApiErrorResponse with the specified status, message, and errors.
     *
     * @param status the HTTP status code
     * @param message the error message
     * @param errors a list of specific error details
     */
    public ApiErrorResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    /**
     * Gets the HTTP status code.
     *
     * @return the HTTP status code
     */
    public int getStatus() {
        return status;
    }

    /**
     * Gets the error message.
     *
     * @return the error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the list of specific error details.
     *
     * @return the list of errors
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Sets the HTTP status code.
     *
     * @param status the HTTP status code to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Sets the error message.
     *
     * @param message the error message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets the list of specific error details.
     *
     * @param errors the list of errors to set
     */
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    /**
     * Returns a string representation of the ApiErrorResponse.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "ApiErrorResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", errors=" + errors +
                '}';
    }
}

