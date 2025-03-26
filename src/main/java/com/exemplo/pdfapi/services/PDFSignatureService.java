package com.exemplo.pdfapi.services;

import com.exemplo.pdfapi.domain.DigitalSignatureInfo;
import com.exemplo.pdfapi.domain.HashValidation;
import com.exemplo.pdfapi.domain.Report;
import com.exemplo.pdfapi.domain.RevocationValidationResult;
import com.exemplo.pdfapi.domain.SignatureCertificate;
import com.exemplo.pdfapi.ocsp.CertificateRevocationChecker;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.util.Store;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.*;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.*;
import java.nio.file.Files;

@Service
public class PDFSignatureService {

    private final Instant nowUTC;

    /*
     * Constructor
     * @param env
     */
    public PDFSignatureService(Environment env) {
        this.nowUTC = Instant.now();
    }

    /*
     * Processes the PDF document and extracts the digital signature
     * @param file
     * @return Report
     * @throws IOException
     */
    public Report processAndExtractSignature(MultipartFile file) throws IOException {
        // Verification if the file is more than 5MB
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IOException("File size exceeds the limit of 5MB.");
        }

        File tempFile = File.createTempFile("uploaded_", ".pdf");
        file.transferTo(tempFile);

        return extractSignature(tempFile);
    }

    /*
     * Extracts the digital signature from the PDF document
     * @param pdfFile
     * @return Report
     */
    public Report extractSignature(File pdfFile) {
        Report report = new Report();

        try (PDDocument document = PDDocument.load(pdfFile)) {
            List<PDSignature> signatures = document.getSignatureDictionaries();

            if (signatures.isEmpty()) {
                return report;
            }


            for (PDSignature signature : signatures) {
                // Extracts the certificates from the PDF document
                InputStream fileStream = new FileInputStream(pdfFile);
                byte[] signatureBytes = signature.getContents(fileStream);
                List<X509Certificate> certs = extractCertificates(signatureBytes);


                DigitalSignatureInfo signatureInfo = extractSignatureInfo(signature, certs, pdfFile);
               
                if (signatureInfo != null) {
                    report.addDigitalSignatureInfo(signatureInfo);

                    boolean isValid = verifySignatureIntegrity(certs, signatureInfo, pdfFile, signature);

                    report.setPdfValidation(isValid);
                    if (!isValid) {
                        break;
                    }
                } else {
                    System.out.println("Error extracting the signature.");
                }
            }

            return report;
        } catch (Exception e) {
            return report;
        }
    }



    /*
     * Extracts the digital signature from the PDF document
     * @param signature
     * @param pdfFile
     * @return DigitalSignatureInfo
     */
    private DigitalSignatureInfo extractSignatureInfo(PDSignature signature, List<X509Certificate> certs, File pdfFile) {
        try {
            String reason = signature.getReason();
            Date signingDate = signature.getSignDate().getTime();
            int[] byteRange = signature.getByteRange();

            

            if (certs.isEmpty()) {
                System.out.println("None certificate found in the signature.");
                return null;
            }

            List<SignatureCertificate> certList = new ArrayList<>();
            DigitalSignatureInfo signatureInfo = new DigitalSignatureInfo(reason, signingDate, byteRange, certList);

            for (X509Certificate cert : certs) {
                certList.add(new SignatureCertificate(cert.getSubjectX500Principal().getName() ,cert.getNotBefore(),
                            cert.getNotAfter(), cert.getIssuerX500Principal().getName(), cert.getSerialNumber().toString(), cert.getSigAlgName(), cert.getKeyUsage(), cert.getPublicKey(), Base64.getEncoder().encodeToString(cert.getPublicKey().getEncoded()), false, null, null, null));
            }

            
            return signatureInfo;

        } catch (Exception e) {
            System.out.println("Error extracting the signature: " + e.getMessage());
            return null;
        }
    }


    /*
     * Extracts the certificates from the PDF document
     * @param signatureBytes
     * @return List<X509Certificate>
     */
    private List<X509Certificate> extractCertificates(byte[] signatureBytes) {
        List<X509Certificate> certificateList = new ArrayList<>();
        try {
            CMSSignedData cmsSignedData = new CMSSignedData(signatureBytes);
            Store<X509CertificateHolder> certs = cmsSignedData.getCertificates();
            Collection<X509CertificateHolder> certCollection = certs.getMatches(null);
            for (X509CertificateHolder certHolder : certCollection) {
                X509Certificate cert = new JcaX509CertificateConverter().getCertificate(certHolder);
                certificateList.add(cert);
            }
        } catch (Exception e) {
            System.out.println("Error extracting the certificate: " + e.getMessage());
        }
        return certificateList;
    }


    /*
     * Verifies the integrity of the digital signature
     * @param signatureInfo
     * @param pdfFile
     * @param signature
     * @return boolean
     * @throws Exception
     */
    private boolean verifySignatureIntegrity(List<X509Certificate> certs, DigitalSignatureInfo signatureInfo, File pdfFile, PDSignature signature) {
        try {
            boolean isCertValid = true;

            // Verification of the data validity of the certificates
            if (!isAllDataValid(signatureInfo)) {
                isCertValid = false;
            }

            // Verification of the certificates signatures
            for (SignatureCertificate cert : signatureInfo.getCertificates()) {
                if (!verifyCMSIntegrity(pdfFile, signature, cert)) isCertValid = false;
            }

            // Verification of the certificates revocation
            for (int i = 0; i < certs.size(); i++) {
                X509Certificate cert = certs.get(i);
                X509Certificate issuerCert = findIssuer(cert, certs);
            
                RevocationValidationResult result = verifyCertificateRevocation(cert, issuerCert);
                boolean notRevoked = result != null && result.getResult();
            
                SignatureCertificate certInfo = signatureInfo.getCertificates().get(i);
                certInfo.setRevocationValidationResult(result);
                certInfo.setOcspAttemptResult(CertificateRevocationChecker.getLastOCSPAttempt());
            
                if (!notRevoked) {
                    System.out.println("Revoked certificate: " + cert.getSubjectX500Principal());
                    isCertValid = false;
                }
            }
            

            return isCertValid;
        } catch (Exception e) {
            return false;
        }
    }


    /*
     * Verifies the validity of the certificates
     * @param signatureInfo
     * @return boolean
     */
    private boolean isAllDataValid(DigitalSignatureInfo signatureInfo) {
        List<SignatureCertificate> certList = signatureInfo.getCertificates();

        for (SignatureCertificate cert : certList) {
            Instant validFromUTC = cert.getValidFrom().toInstant();
            Instant validToUTC = cert.getValidTo().toInstant();
    
    
            if (validFromUTC.isAfter(nowUTC)) {
                cert.setDataValidation(false);
                return false;
            } else if (validToUTC.isBefore(nowUTC)) {
                cert.setDataValidation(false);
                return false;
            }
            
            cert.setDataValidation(true);
        }
    
        return true;
    }


    /*
     * Verifies the revocation of the certificate via OCSP 
     * @param cert
     * @param issuerCert
     * @return RevocationValidationResult
     */
    private RevocationValidationResult verifyCertificateRevocation(X509Certificate cert, X509Certificate issuerCert) {
        try {
            CertificateRevocationChecker.isCertificateValid(cert, issuerCert, true);
            RevocationValidationResult result = CertificateRevocationChecker.getRevocationValidationResult();
            return result;
        } catch (Exception e) {
            System.out.println("Error verifying the certificate revocation: " + e.getMessage());
            return null;
        }
    }


    /*
     * Test the CRL verification manually
     * @param certs
     */
    private RevocationValidationResult testCRLManually(List<X509Certificate> certs) {
        try {
            if (certs.isEmpty()) {
                System.out.println("Certificates list empty.");
                return null;
            }
    
            X509Certificate cert = certs.get(2);
            boolean result = CertificateRevocationChecker.checkCRL(cert);
            System.out.println("CRL verification result: " + (result ? "VÁLID" : "REVOKED"));
            return CertificateRevocationChecker.getRevocationValidationResult();

        } catch (Exception e) {
            System.out.println("Error testing CRL: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Finds the issuer of the certificate
     * @param cert
     * @param allCerts
     * @return X509Certificate
     */
    private X509Certificate findIssuer(X509Certificate cert, List<X509Certificate> allCerts) {
        return allCerts.stream()
            .filter(issuer -> cert.getIssuerX500Principal().equals(issuer.getSubjectX500Principal()))
            .findFirst()
            .orElse(cert);
    }
    


    /*
     * Verifies the integrity of CMS (Cryptographic Message Syntax) 
     * @param pdfFile
     * @param signature
     * @param mainCert
     * @return boolean
     * @throws Exception
     */
    private boolean verifyCMSIntegrity(File pdfFile, PDSignature signature, SignatureCertificate cert) {
        try {
            // Extract the data from the PDF for signature verification
            ByteArrayInputStream pdfBytes = new ByteArrayInputStream(Files.readAllBytes(pdfFile.toPath()));
            byte[] contentToSigned = getByteRangeData(pdfBytes, signature.getByteRange());
    
            // Extract the data from the CMS signature
            CMSSignedData signedData = new CMSSignedData(signature.getContents());
            SignerInformation signerInfo = signedData.getSignerInfos().getSigners().iterator().next();
    
            String messageDigest = Base64.getEncoder().encodeToString(
                    Hex.decode(signerInfo.getSignedAttributes()
                            .get(PKCSObjectIdentifiers.pkcs_9_at_messageDigest)
                            .getAttributeValues()[0]
                            .toString().substring(1)));
    
            MessageDigest digest = MessageDigest.getInstance(signerInfo.getDigestAlgOID());
            String calculatedDigest = Base64.getEncoder().encodeToString(digest.digest(contentToSigned));
    
            
            if (!messageDigest.equals(calculatedDigest)) {
                cert.setHashValidation(new HashValidation(messageDigest, calculatedDigest, false));
                return false;
            } else {
                cert.setHashValidation(new HashValidation(messageDigest, calculatedDigest, true));
            }
    
            return verifySignature(signerInfo, signedData, cert);
        } catch (Exception e) {
            return false;
        }
    }
    

    /*
     * Verifies the digital signature
     * @param signer
     * @param pubKey
     * @param mainCert
     * @return boolean
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private boolean verifySignature(SignerInformation signerInfo, CMSSignedData signedData, SignatureCertificate cert) {
        try {

            // Obtain the certificate of the signatory
            Store<X509CertificateHolder> certStore = signedData.getCertificates();
            Collection<X509CertificateHolder> certCollection = certStore.getMatches(signerInfo.getSID());

            if (certCollection.isEmpty()) {
                System.out.println("Signatory certificate not found.");
                return false;
            }

            X509Certificate signerCert = new JcaX509CertificateConverter().getCertificate(certCollection.iterator().next());

            PublicKey pubKey = signerCert.getPublicKey();

            Signature rsaSign = Signature.getInstance(signerCert.getSigAlgName());
            rsaSign.initVerify(pubKey);
            rsaSign.update(signerInfo.getEncodedSignedAttributes());

            boolean valid = rsaSign.verify(signerInfo.getSignature());
            return valid;
        } catch (Exception e) {
            System.out.println("Error validating the digital signature: " + e.getMessage());
            return false;
        }
    }


    /*
     * Extracts the byte range data from the PDF
     * @param bis
     * @param byteRange
     * @return byte[]
     */
    private byte[] getByteRangeData(ByteArrayInputStream bis, int[] byteRange) {
        try {
            int length = byteRange[1] + byteRange[3];
        
            // Array to store the signed content
            byte[] contentSigned = new byte[length];

            // Skip the first bytes of the PDF
            bis.skip(byteRange[0]);

            // Read the signed content 
            bis.read(contentSigned, 0, byteRange[1]);
            bis.skip(byteRange[2] - byteRange[1] - byteRange[0]);
            bis.read(contentSigned, byteRange[1], byteRange[3]);

            // Reset the stream
            bis.reset();
            return contentSigned;
        } catch (Exception e) {
            System.out.println("Erro ao processar byte range: " + e.getMessage());
            return null;
        }
    }
}
