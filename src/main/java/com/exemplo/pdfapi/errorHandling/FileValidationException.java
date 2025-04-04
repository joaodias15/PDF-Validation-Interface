package com.exemplo.pdfapi.errorHandling;

public class FileValidationException extends RuntimeException {
    
    /*
     * This exception is thrown when a file validation error occurs.
     * It extends the RuntimeException class, allowing it to be thrown
     * without being declared in the method signature.
     * 
     * @param message The error message to be associated with this exception.
     */
    public FileValidationException(String message) {
        super(message);
    }
}
