package com.exemplo.pdfapi.domain;

public class DigitalSignatureReport {
    private StringBuilder simpleValidation;
    private StringBuilder technicalValidation;

    public DigitalSignatureReport() {
        this.simpleValidation = new StringBuilder();
        this.technicalValidation = new StringBuilder();
    }

    public void addSimpleValidation(String text) {
        simpleValidation.append(text).append("\n");
    }

    public void addTechnicalValidation(String text) {
        technicalValidation.append(text).append("\n");
    }

    public void addBothValidations(String text) {
        addSimpleValidation(text);
        addTechnicalValidation(text);
    }

    public String getSimpleValidation() {
        return simpleValidation.toString();
    }

    public String getTechnicalValidation() {
        return technicalValidation.toString();
    }

    @Override
    public String toString() {
        return  "\n\n➡️ === Validação Simples ===\n" + getSimpleValidation() + "\n"
                + "\n\n\n\n\n\n➡️ === Validação Técnica ===\n" + getTechnicalValidation();
    }
}
