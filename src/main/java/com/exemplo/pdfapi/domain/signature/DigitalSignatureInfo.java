package com.exemplo.pdfapi.domain.signature;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DigitalSignatureInfo {
    
    private String reason;

    private int[] byteRange;
    
    private Date signingDate;

    private List<SignatureCertificate> certificates;




    public DigitalSignatureInfo(String reason, Date signingDate, int[] byteRange, List<SignatureCertificate> certificates) {
        this.reason = reason;
        this.signingDate = signingDate;
        this.byteRange = byteRange;
        this.certificates = certificates;
    }


    public String getReason() { return reason; }
    public Date getSigningDate() { return signingDate; }
    public int[] getByteRange() { return byteRange; }
    public List<SignatureCertificate> getCertificates() { return certificates; }


    public void setReason(String reason) { this.reason = reason; }
    public void setSigningDate(Date signingDate) { this.signingDate = signingDate; }
    public void setByteRange(int[] byteRange) { this.byteRange = byteRange; }
    public void setCertificates(List<SignatureCertificate> certificates) { this.certificates = certificates; }


    @Override
    public String toString() {
        return "\n\n\n\n=================Informação================\n" + 
                "\nDados Importantes:\n" +
                "-> Data de assinatura = " + signingDate + "\n" +
                "\nDados Menos Importantes:\n" +
                "-> Razão = " + reason + "\n" +
                "-> Byte Range = " + Arrays.toString(byteRange) +
                 "\n" + certificates + "\n\n\n\n";
    }
}
