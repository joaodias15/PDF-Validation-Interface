package com.exemplo.pdfapi.domain;

import java.util.ArrayList;
import java.util.List;

import com.exemplo.pdfapi.domain.signature.DigitalSignatureInfo;

public class Report {

    private List<DigitalSignatureInfo> digitalSignatureInfo = new ArrayList<>();

    private boolean pdfValidation;

    public Report (List<DigitalSignatureInfo> digitalSignatureInfo, boolean signatureValidation) {
        this.digitalSignatureInfo = digitalSignatureInfo;
        this.pdfValidation = signatureValidation;
    }

    public Report () {}

    public List<DigitalSignatureInfo> getDigitalSignatureInfo() {
        return digitalSignatureInfo;
    }

    public boolean getPdfValidation() {
        return pdfValidation;
    }

    public void setDigitalSignatureInfo(List<DigitalSignatureInfo> digitalSignatureInfo) {
        this.digitalSignatureInfo = digitalSignatureInfo;
    }

    public void setPdfValidation(boolean signatureValidation) {
        this.pdfValidation = signatureValidation;
    }

    public void addDigitalSignatureInfo(DigitalSignatureInfo digitalSignatureInfo) {
        this.digitalSignatureInfo.add(digitalSignatureInfo);
    }

    @Override
    public String toString() {
        return "Report{" +
                "digitalSignatureInfo=" + digitalSignatureInfo +
                ", signatureValidation='" + pdfValidation + '\'' +
                '}';
    }

    
}
