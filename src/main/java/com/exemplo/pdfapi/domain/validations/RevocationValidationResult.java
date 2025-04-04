package com.exemplo.pdfapi.domain.validations;

import java.time.Instant;

import com.exemplo.pdfapi.enums.revocationValidation.RevocationTypeEnum;

/**
 * Represents the result of a revocation validation for a certificate.
 */
public class RevocationValidationResult {

    private String revocationStatus;

    private RevocationTypeEnum revocationType;

    private String url;

    private String reason;

    private Instant validationTimestamp;
    
    private boolean result;

    /**
     * Default constructor for RevocationValidationResult.
     */
    public RevocationValidationResult() {}

    /**
     * Constructs a new RevocationValidationResult with the specified details.
     *
     * @param revocationStatus the revocation status
     * @param validatedBy the type of revocation validation
     * @param url the URL used for validation
     * @param reason the reason for revocation
     * @param validationTimestamp the timestamp of the validation
     * @param result the result of the validation
     */
    public RevocationValidationResult(String revocationStatus, RevocationTypeEnum validatedBy, String url, String reason, Instant validationTimestamp, boolean result) {
        this.revocationStatus = revocationStatus;
        this.revocationType = validatedBy;
        this.url = url;
        this.reason = reason;
        this.validationTimestamp = validationTimestamp;
        this.result = result;
    }

    /**
     * Gets the revocation status.
     *
     * @return the revocation status
     */
    public String getRevocationStatus() {
        return revocationStatus;
    }

    /**
     * Sets the revocation status.
     *
     * @param revocationStatus the revocation status
     */
    public void setRevocationStatus(String revocationStatus) {
        this.revocationStatus = revocationStatus;
    }

    /**
     * Gets the type of revocation validation.
     *
     * @return the type of revocation validation
     */
    public RevocationTypeEnum getRevocationType() {
        return revocationType;
    }

    /**
     * Sets the type of revocation validation.
     *
     * @param validatedBy the type of revocation validation
     */
    public void setValidatedBy(RevocationTypeEnum validatedBy) {
        this.revocationType = validatedBy;
    }

    /**
     * Gets the URL used for validation.
     *
     * @return the URL used for validation
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL used for validation.
     *
     * @param ocspUrl the URL used for validation
     */
    public void setUrl(String ocspUrl) {
        this.url = ocspUrl;
    }

    /**
     * Gets the reason for revocation.
     *
     * @return the reason for revocation
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason for revocation.
     *
     * @param reason the reason for revocation
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Gets the timestamp of the validation.
     *
     * @return the timestamp of the validation
     */
    public Instant getValidationTimestamp() {
        return validationTimestamp;
    }

    /**
     * Sets the timestamp of the validation.
     *
     * @param validationTimestamp the timestamp of the validation
     */
    public void setValidationTimestamp(Instant validationTimestamp) {
        this.validationTimestamp = validationTimestamp;
    }

    /**
     * Gets the result of the validation.
     *
     * @return the result of the validation
     */
    public boolean getResult() {
        return result;
    }

    /**
     * Sets the result of the validation.
     *
     * @param result the result of the validation
     */
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
