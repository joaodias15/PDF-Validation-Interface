package com.exemplo.pdfapi.domain.validations;

public class HashValidation {

    private String hashPDF;

    private String hashPDFSignature;

    private boolean hashValidation;

    public HashValidation(String hashPDF, String hashPDFSignature, boolean hashValidation) {
        this.hashPDF = hashPDF;
        this.hashPDFSignature = hashPDFSignature;
        this.hashValidation = hashValidation;
    }

    public HashValidation() {
    }

    public String getHashPDF() {
        return hashPDF;
    }

    public String getHashPDFSignature() {
        return hashPDFSignature;
    }

    public boolean getHashValidation() {
        return hashValidation;
    }

    public void setHashPDF(String hashPDF) {
        this.hashPDF = hashPDF;
    }

    public void setHashPDFSignature(String hashPDFSignature) {
        this.hashPDFSignature = hashPDFSignature;
    }

    public void setHashValidation(boolean hashValidation) {
        this.hashValidation = hashValidation;
    }

    @Override
    public String toString() {
        return "HashValidation{" +
                "hashPDF='" + hashPDF + '\'' +
                ", hashPDFSignature='" + hashPDFSignature + '\'' +
                ", hashValidation='" + hashValidation + '\'' +
                '}';
    }

    
}
