package com.exemplo.pdfapi.domain;

import java.util.ArrayList;
import java.util.List;

import com.exemplo.pdfapi.domain.signature.DigitalSignatureInfo;

/**
 * Represents a report containing information about digital signatures and PDF validation.
 */
public class Report {

    private List<DigitalSignatureInfo> digitalSignatureInfo = new ArrayList<>();
    
    private boolean pdfValidation;

    /**
     * Constructs a new Report with the specified digital signature information and PDF validation status.
     *
     * @param digitalSignatureInfo the list of digital signature information
     * @param signatureValidation the PDF validation status
     */
    public Report(List<DigitalSignatureInfo> digitalSignatureInfo, boolean signatureValidation) {
        this.digitalSignatureInfo = digitalSignatureInfo;
        this.pdfValidation = signatureValidation;
    }

    /**
     * Default constructor for Report.
     */
    public Report() {}

    /**
     * Gets the list of digital signature information.
     *
     * @return the list of digital signature information
     */
    public List<DigitalSignatureInfo> getDigitalSignatureInfo() {
        return digitalSignatureInfo;
    }

    /**
     * Gets the PDF validation status.
     *
     * @return true if the PDF is valid, false otherwise
     */
    public boolean getPdfValidation() {
        return pdfValidation;
    }

    /**
     * Sets the list of digital signature information.
     *
     * @param digitalSignatureInfo the list of digital signature information to set
     */
    public void setDigitalSignatureInfo(List<DigitalSignatureInfo> digitalSignatureInfo) {
        this.digitalSignatureInfo = digitalSignatureInfo;
    }

    /**
     * Sets the PDF validation status.
     *
     * @param signatureValidation the PDF validation status to set
     */
    public void setPdfValidation(boolean signatureValidation) {
        this.pdfValidation = signatureValidation;
    }

    /**
     * Adds a digital signature information to the list.
     *
     * @param digitalSignatureInfo the digital signature information to add
     */
    public void addDigitalSignatureInfo(DigitalSignatureInfo digitalSignatureInfo) {
        this.digitalSignatureInfo.add(digitalSignatureInfo);
    }

    /**
     * Returns a string representation of the Report.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "Report{" +
                "digitalSignatureInfo=" + digitalSignatureInfo +
                ", signatureValidation='" + pdfValidation + '\'' +
                '}';
    }
}
