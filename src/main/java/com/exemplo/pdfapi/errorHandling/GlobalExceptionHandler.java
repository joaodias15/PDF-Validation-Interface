package com.exemplo.pdfapi.errorHandling;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.exemplo.pdfapi.domain.error.ApiErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

     /*
     * This class handles exceptions globally for the application.
     * @param ex
     * 
     * @return ResponseEntity<ApiErrorResponse>
     * 
     * @ExceptionHandler(FileValidationException.class)
     */
    @ExceptionHandler(FileValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleFileValidation(FileValidationException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "File validation error",
            List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /*
     * This method handles multiple validation errors.
     * @param ex
     * 
     * @return ResponseEntity<ApiErrorResponse>
     * 
     * @ExceptionHandler(MultipleValidationException.class)
     */
    @ExceptionHandler(MultipleValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleMultipleErrors(MultipleValidationException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation failed",
            ex.getErrors()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /*
     * This method handles generic exceptions.
     * @param ex
     * 
     * @return ResponseEntity<ApiErrorResponse>
     * 
     * @ExceptionHandler(Exception.class)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex) {
        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Unexpected server error",
            List.of("An internal error occurred.")
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}

