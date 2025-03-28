package com.exemplo.pdfapi.domain.signature;

import java.security.PublicKey;
import java.util.Date;

import com.exemplo.pdfapi.domain.validations.HashValidation;
import com.exemplo.pdfapi.domain.validations.RevocationValidationResult;
import com.fasterxml.jackson.annotation.JsonIgnore;

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


    public CertificateType getType() { return type; }
    public String getSignerName() { return signerName; }
    public String getIssuer() { return issuer; }
    public Date getValidFrom() { return validFrom; }
    public Date getValidTo() { return validTo; }
    public String getSerialNumber() { return serialNumber; }
    public String getSignatureAlgorithm() { return signatureAlgorithm; }
    public boolean[] getKeyUsage() { return keyUsage; }
    @JsonIgnore
    public PublicKey getPublicKey() { return publicKey; }
    public String getPublicKeyBase64() { return publicKeyBase64; }
    public boolean getDataValidation() { return dataValidation; }
    public HashValidation getHashValidation() { return hashValidation; }
    public RevocationValidationResult getRevocationValidationResult() { return revocationValidationResult; }
    public RevocationValidationResult getOcspAttemptResult() { return ocspAttemptResult; }
    public boolean getIsValid() { return isValid; }

    public void setType(CertificateType type) { this.type = type; }
    public void setDataValidation(boolean dataValidation) { this.dataValidation = dataValidation; }
    public void setHashValidation(HashValidation hashValidation) { this.hashValidation = hashValidation; }
    public void setRevocationValidationResult(RevocationValidationResult revocationValidationResult) { this.revocationValidationResult = revocationValidationResult; }
    public void setOcspAttemptResult(RevocationValidationResult ocspAttemptResult) { this.ocspAttemptResult = ocspAttemptResult; }
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
