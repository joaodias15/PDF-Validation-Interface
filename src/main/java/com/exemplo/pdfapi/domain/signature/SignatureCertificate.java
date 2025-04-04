package com.exemplo.pdfapi.domain.signature;

import java.security.PublicKey;
import java.util.Date;

import com.exemplo.pdfapi.domain.validations.HashValidation;
import com.exemplo.pdfapi.domain.validations.RevocationValidationResult;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a certificate used in a digital signature.
 */
public class SignatureCertificate {

    private CertificateType type;

    private String signerName;

    private String issuer;
    
    private Date validFrom;
    
    private Date validTo;
    
    private String serialNumber;
    
    private String signatureAlgorithm;
    
    private boolean[] keyUsage;
    
    @JsonIgnore
    private PublicKey publicKey;
    
    private String publicKeyBase64;

    private boolean dataValidation;

    private HashValidation hashValidation;

    private RevocationValidationResult revocationValidationResult;

    private RevocationValidationResult ocspAttemptResult;

    private boolean isValid;

    /**
     * Constructs a new SignatureCertificate with the specified details.
     *
     * @param type the type of the certificate
     * @param signerName the name of the signer
     * @param validFrom the start date of the certificate's validity
     * @param validTo the end date of the certificate's validity
     * @param issuer the issuer of the certificate
     * @param serialNumber the serial number of the certificate
     * @param signatureAlgorithm the signature algorithm used
     * @param keyUsage the key usage of the certificate
     * @param publicKey the public key of the certificate
     * @param publicKeyBase64 the Base64-encoded public key
     * @param dataValidation the result of data validation
     * @param hashValidation the result of hash validation
     * @param revocationValidationResult the result of revocation validation
     * @param ocspAttemptResult the result of OCSP attempt validation
     * @param isTotalValid the overall validity of the certificate
     */
    public SignatureCertificate(CertificateType type, String signerName, Date validFrom, Date validTo, String issuer, String serialNumber, String signatureAlgorithm, boolean[] keyUsage, PublicKey publicKey, String publicKeyBase64, boolean dataValidation, HashValidation hashValidation, RevocationValidationResult revocationValidationResult, RevocationValidationResult ocspAttemptResult, boolean isTotalValid) {
        this.type = type;
        this.signerName = signerName;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.issuer = issuer;
        this.serialNumber = serialNumber;
        this.signatureAlgorithm = signatureAlgorithm;
        this.keyUsage = keyUsage;
        this.publicKey = publicKey;
        this.publicKeyBase64 = publicKeyBase64;
        this.dataValidation = dataValidation;
        this.hashValidation = hashValidation;
        this.revocationValidationResult = revocationValidationResult;
        this.ocspAttemptResult = ocspAttemptResult;
        this.isValid = isTotalValid;
    }

    /**
     * Gets the type of the certificate.
     *
     * @return the type of the certificate
     */
    public CertificateType getType() { return type; }

    /**
     * Gets the name of the signer.
     *
     * @return the name of the signer
     */
    public String getSignerName() { return signerName; }

    /**
     * Gets the issuer of the certificate.
     *
     * @return the issuer of the certificate
     */
    public String getIssuer() { return issuer; }

    /**
     * Gets the start date of the certificate's validity.
     *
     * @return the start date of the certificate's validity
     */
    public Date getValidFrom() { return validFrom; }

    /**
     * Gets the end date of the certificate's validity.
     *
     * @return the end date of the certificate's validity
     */
    public Date getValidTo() { return validTo; }

    /**
     * Gets the serial number of the certificate.
     *
     * @return the serial number of the certificate
     */
    public String getSerialNumber() { return serialNumber; }

    /**
     * Gets the signature algorithm used.
     *
     * @return the signature algorithm used
     */
    public String getSignatureAlgorithm() { return signatureAlgorithm; }

    /**
     * Gets the key usage of the certificate.
     *
     * @return the key usage of the certificate
     */
    public boolean[] getKeyUsage() { return keyUsage; }

    /**
     * Gets the public key of the certificate.
     *
     * @return the public key of the certificate
     */
    @JsonIgnore
    public PublicKey getPublicKey() { return publicKey; }

    /**
     * Gets the Base64-encoded public key.
     *
     * @return the Base64-encoded public key
     */
    public String getPublicKeyBase64() { return publicKeyBase64; }

    /**
     * Gets the result of data validation.
     *
     * @return the result of data validation
     */
    public boolean getDataValidation() { return dataValidation; }

    /**
     * Gets the result of hash validation.
     *
     * @return the result of hash validation
     */
    public HashValidation getHashValidation() { return hashValidation; }

    /**
     * Gets the result of revocation validation.
     *
     * @return the result of revocation validation
     */
    public RevocationValidationResult getRevocationValidationResult() { return revocationValidationResult; }

    /**
     * Gets the result of OCSP attempt validation.
     *
     * @return the result of OCSP attempt validation
     */
    public RevocationValidationResult getOcspAttemptResult() { return ocspAttemptResult; }

    /**
     * Gets the overall validity of the certificate.
     *
     * @return the overall validity of the certificate
     */
    public boolean getIsValid() { return isValid; }

    /**
     * Sets the type of the certificate.
     *
     * @param type the type of the certificate
     */
    public void setType(CertificateType type) { this.type = type; }

    /**
     * Sets the result of data validation.
     *
     * @param dataValidation the result of data validation
     */
    public void setDataValidation(boolean dataValidation) { this.dataValidation = dataValidation; }

    /**
     * Sets the result of hash validation.
     *
     * @param hashValidation the result of hash validation
     */
    public void setHashValidation(HashValidation hashValidation) { this.hashValidation = hashValidation; }

    /**
     * Sets the result of revocation validation.
     *
     * @param revocationValidationResult the result of revocation validation
     */
    public void setRevocationValidationResult(RevocationValidationResult revocationValidationResult) { this.revocationValidationResult = revocationValidationResult; }

    /**
     * Sets the result of OCSP attempt validation.
     *
     * @param ocspAttemptResult the result of OCSP attempt validation
     */
    public void setOcspAttemptResult(RevocationValidationResult ocspAttemptResult) { this.ocspAttemptResult = ocspAttemptResult; }

    /**
     * Sets the overall validity of the certificate.
     *
     * @param isValid the overall validity of the certificate
     */
    public void setIsValid(boolean isValid) { this.isValid = isValid; }

    @Override
    public String toString() {
        return "SignatureCertificate{" +
                "type=" + type +
                "signerName='" + signerName + '\'' +
                ", issuer='" + issuer + '\'' +
                ", validFrom=" + validFrom +
                ", validTo=" + validTo +
                ", serialNumber='" + serialNumber + '\'' +
                ", signatureAlgorithm='" + signatureAlgorithm + '\'' +
                ", keyUsage=" + keyUsage +
                ", publicKeyBase64='" + publicKeyBase64 + '\'' +
                ", dataValidation='" + dataValidation + '\'' +
                ", hashValidation=" + hashValidation +
                ", revocationValidationResult=" + revocationValidationResult +
                ", ocspAttemptResult=" + ocspAttemptResult +
                ", isTotalValid=" + isValid +
                '}';
    }

    
}
