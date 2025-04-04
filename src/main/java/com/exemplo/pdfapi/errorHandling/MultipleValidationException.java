package com.exemplo.pdfapi.errorHandling;

import java.util.List;

public class MultipleValidationException extends RuntimeException {

    /**
     * Serial version UID for serialization
     */
    private final List<String> errors;

    /**
     * Constructor for MultipleValidationException
     *
     * @param errors List of error messages
     */
    public MultipleValidationException(List<String> errors) {
        super("Validation failed");
        this.errors = errors;
    }

    /**
     * Constructor for MultipleValidationException with a custom message
     *
     * @param message Custom error message
     * @param errors  List of error messages
     */
    public List<String> getErrors() {
        return errors;
    }
}
