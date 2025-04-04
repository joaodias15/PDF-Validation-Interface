package com.exemplo.pdfapi.domain.signature;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Represents information about a digital signature in a PDF document.
 */
public class DigitalSignatureInfo {

    private String reason;

    private int[] byteRange;

    private Date signingDate;
    
    private List<SignatureCertificate> certificates;
    
    private String structureError;

    /**
     * Constructs a new DigitalSignatureInfo with the specified details.
     *
     * @param reason the reason for the signature
     * @param signingDate the date of signing
     * @param byteRange the byte range of the signature
     * @param certificates the list of certificates associated with the signature
     * @param structureError any structural error in the signature
     */
    public DigitalSignatureInfo(String reason, Date signingDate, int[] byteRange, List<SignatureCertificate> certificates, String structureError) {
        this.reason = reason;
        this.signingDate = signingDate;
        this.byteRange = byteRange;
        this.certificates = certificates;
        this.structureError = structureError;
    }

    /**
     * Gets the reason for the signature.
     *
     * @return the reason for the signature
     */
    public String getReason() { return reason; }

    /**
     * Gets the date of signing.
     *
     * @return the date of signing
     */
    public Date getSigningDate() { return signingDate; }

    /**
     * Gets the byte range of the signature.
     *
     * @return the byte range of the signature
     */
    public int[] getByteRange() { return byteRange; }

    /**
     * Gets the list of certificates associated with the signature.
     *
     * @return the list of certificates
     */
    public List<SignatureCertificate> getCertificates() { return certificates; }

    /**
     * Gets any structural error in the signature.
     *
     * @return the structural error
     */
    public String getStructureError() { return structureError; }

    /**
     * Gets the certificate associated with the specified signer name.
     *
     * @param signerName the name of the signer
     * @return the certificate associated with the signer, or null if not found
     */
    public SignatureCertificate getCertificateBySigner(String signerName) {
        for (SignatureCertificate certificate : certificates) {
            if (certificate.getSignerName().equals(signerName)) {
                return certificate;
            }
        }
        return null;
    }

    /**
     * Sets the reason for the signature.
     *
     * @param reason the reason for the signature
     */
    public void setReason(String reason) { this.reason = reason; }

    /**
     * Sets the date of signing.
     *
     * @param signingDate the date of signing
     */
    public void setSigningDate(Date signingDate) { this.signingDate = signingDate; }

    /**
     * Sets the byte range of the signature.
     *
     * @param byteRange the byte range of the signature
     */
    public void setByteRange(int[] byteRange) { this.byteRange = byteRange; }

    /**
     * Sets the list of certificates associated with the signature.
     *
     * @param certificates the list of certificates
     */
    public void setCertificates(List<SignatureCertificate> certificates) { this.certificates = certificates; }

    /**
     * Sets any structural error in the signature.
     *
     * @param structureError the structural error
     */
    public void setStructureError(String structureError) { this.structureError = structureError; }

    /**
     * Returns a string representation of the digital signature information.
     *
     * @return a string representation of the digital signature information
     */
    @Override
    public String toString() {
        return "\n\n\n\n=================Informação================\n" + 
                "\nDados Importantes:\n" +
                "-> Data de assinatura = " + signingDate + "\n" +
                "\nDados Menos Importantes:\n" +
                "-> Razão = " + reason + "\n" +
                "-> Byte Range = " + Arrays.toString(byteRange) + "\n" +
                "-> Erro de Estrutura = " + structureError +
                 "\n" + certificates + "\n\n\n\n";
    }
}
