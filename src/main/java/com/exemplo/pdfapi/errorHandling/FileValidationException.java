package com.exemplo.pdfapi.errorHandling;

public class FileValidationException extends RuntimeException {
    public FileValidationException(String message) {
        super(message);
    }
}
