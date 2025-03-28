package com.exemplo.pdfapi.domain.signature;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DigitalSignatureInfo {
    
    private String reason;

    private int[] byteRange;
    
    private Date signingDate;

    private List<SignatureCertificate> certificates;

    private String structureError;




    public DigitalSignatureInfo(String reason, Date signingDate, int[] byteRange, List<SignatureCertificate> certificates, String structureError) {
        this.reason = reason;
        this.signingDate = signingDate;
        this.byteRange = byteRange;
        this.certificates = certificates;
        this.structureError = structureError;
    }


    public String getReason() { return reason; }
    public Date getSigningDate() { return signingDate; }
    public int[] getByteRange() { return byteRange; }
    public List<SignatureCertificate> getCertificates() { return certificates; }
    public String getStructureError() { return structureError; }

    
    public SignatureCertificate getCertificateBySigner (String signerName) {
        for (SignatureCertificate certificate : certificates) {
            if (certificate.getSignerName().equals(signerName)) {
                return certificate;
            }
        }
        return null;
    }


    public void setReason(String reason) { this.reason = reason; }
    public void setSigningDate(Date signingDate) { this.signingDate = signingDate; }
    public void setByteRange(int[] byteRange) { this.byteRange = byteRange; }
    public void setCertificates(List<SignatureCertificate> certificates) { this.certificates = certificates; }
    public void setStructureError(String structureError) { this.structureError = structureError; }



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
