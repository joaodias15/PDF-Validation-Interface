package com.exemplo.pdfapi.errorHandling;

import java.util.List;

public class MultipleValidationException extends RuntimeException {
    private final List<String> errors;

    public MultipleValidationException(List<String> errors) {
        super("Validation failed");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
