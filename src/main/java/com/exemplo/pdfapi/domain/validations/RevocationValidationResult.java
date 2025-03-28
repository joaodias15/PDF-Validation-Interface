package com.exemplo.pdfapi.domain.validations;

import java.time.Instant;

import com.exemplo.pdfapi.enums.revocationValidation.RevocationTypeEnum;

public class RevocationValidationResult {
    
    String revocationStatus;

    RevocationTypeEnum revocationType;

    String url;

    String reason;

    Instant validationTimestamp;

    boolean result;

    public RevocationValidationResult() {
    }

    public RevocationValidationResult(String revocationStatus, RevocationTypeEnum validatedBy, String url, String reason, Instant validationTimestamp, boolean result) {
        this.revocationStatus = revocationStatus;
        this.revocationType = validatedBy;
        this.url = url;
        this.reason = reason;
        this.validationTimestamp = validationTimestamp;
        this.result = result;
    }


    public String getRevocationStatus() {
        return revocationStatus;
    }

    public void setRevocationStatus(String revocationStatus) {
        this.revocationStatus = revocationStatus;
    }

    public RevocationTypeEnum getRevocationType() {
        return revocationType;
    }

    public void setValidatedBy(RevocationTypeEnum validatedBy) {
        this.revocationType = validatedBy;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String ocspUrl) {
        this.url = ocspUrl;
    }
 
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getValidationTimestamp() {
        return validationTimestamp;
    }

    public void setValidationTimestamp(Instant validationTimestamp) {
        this.validationTimestamp = validationTimestamp;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    @Override

    public String toString() {
        return "RevocationValidationResult{" +
                "revocationStatus='" + revocationStatus + '\'' +
                ", validatedBy='" + revocationType + '\'' +
                ", ocspUrl='" + url + '\'' +
                ", reason='" + reason + '\'' +
                ", validationTimestamp=" + validationTimestamp +
                ", result=" + result +
                '}';
    }
}
