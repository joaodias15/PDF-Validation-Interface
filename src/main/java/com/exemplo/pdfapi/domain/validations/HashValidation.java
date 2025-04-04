package com.exemplo.pdfapi.domain.validations;

/**
 * Represents the result of a hash validation for a PDF and its signature.
 */
public class HashValidation {

    private String hashPDF;

    private String hashPDFSignature;
    
    private boolean hashValidation;

    /**
     * Constructs a new HashValidation with the specified hash values and validation result.
     *
     * @param hashPDF the hash of the PDF
     * @param hashPDFSignature the hash of the PDF signature
     * @param hashValidation the result of the hash validation
     */
    public HashValidation(String hashPDF, String hashPDFSignature, boolean hashValidation) {
        this.hashPDF = hashPDF;
        this.hashPDFSignature = hashPDFSignature;
        this.hashValidation = hashValidation;
    }

    /**
     * Default constructor for HashValidation.
     */
    public HashValidation() {}

    /**
     * Gets the hash of the PDF.
     *
     * @return the hash of the PDF
     */
    public String getHashPDF() {
        return hashPDF;
    }

    /**
     * Gets the hash of the PDF signature.
     *
     * @return the hash of the PDF signature
     */
    public String getHashPDFSignature() {
        return hashPDFSignature;
    }

    /**
     * Gets the result of the hash validation.
     *
     * @return true if the hash validation is successful, false otherwise
     */
    public boolean getHashValidation() {
        return hashValidation;
    }

    /**
     * Sets the hash of the PDF.
     *
     * @param hashPDF the hash of the PDF to set
     */
    public void setHashPDF(String hashPDF) {
        this.hashPDF = hashPDF;
    }

    /**
     * Sets the hash of the PDF signature.
     *
     * @param hashPDFSignature the hash of the PDF signature to set
     */
    public void setHashPDFSignature(String hashPDFSignature) {
        this.hashPDFSignature = hashPDFSignature;
    }

    /**
     * Sets the result of the hash validation.
     *
     * @param hashValidation the result of the hash validation to set
     */
    public void setHashValidation(boolean hashValidation) {
        this.hashValidation = hashValidation;
    }

    /**
     * Returns a string representation of the HashValidation.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "HashValidation{" +
                "hashPDF='" + hashPDF + '\'' +
                ", hashPDFSignature='" + hashPDFSignature + '\'' +
                ", hashValidation='" + hashValidation + '\'' +
                '}';
    }
}
