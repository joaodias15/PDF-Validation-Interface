package com.exemplo.pdfapi.revocationValidation;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.ocsp.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.DigestCalculator;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;

import com.google.common.cache.CacheBuilder;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.Security;
import java.security.cert.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.exemplo.pdfapi.domain.validations.RevocationValidationResult;
import com.google.common.cache.Cache;

public class CertificateRevocationChecker {


    /*
     * Adds the Bouncy Castle provider to the security providers list.
     */
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static RevocationValidationResult revocationValidationResult;
    private static RevocationValidationResult lastOCSPAttempt = null;

    // Cache to avoid multiple OCSP requests for the same certificate
    private static final Cache<String, RevocationValidationResult> ocspCache = CacheBuilder.newBuilder()
            .expireAfterWrite(12, TimeUnit.HOURS)
            .maximumSize(1000)
            .build();


    /*
     * Checks if a certificate is valid by checking its revocation status using OCSP and CRL.
     * @param cert 
     * @param issuerCert
     * @param strictMode If true, the method will return false if the OCSP status is unknown.
     * @return true if the certificate is valid, false otherwise.
     */
    public static boolean isCertificateValid(X509Certificate cert, X509Certificate issuerCert, boolean strictMode) {
        try {
            checkOCSP(cert, issuerCert);
            System.out.println("Result OCSP to " + cert.getSubjectX500Principal() + ": " + revocationValidationResult.getRevocationStatus());
            
            String ocspStatus = revocationValidationResult.getRevocationStatus();

            if (ocspStatus.equals(RevocationStatusEnum.GOOD.toString())) {
                System.out.println("OCSP: certificate valid.");
                return true;
            }

            if (ocspStatus.equals(RevocationStatusEnum.REVOKED.toString())) {
                System.out.println("OCSP: certificate revoked.");
                return false;
            }


    
            // OCSP == UNKNOWN → tries CRL if strictMode is enabled
            if (strictMode) {
                System.out.println("OCSP returned UNKNOWN → trying CRL...");
                boolean crlValid = checkCRL(cert);
                if (!crlValid) {    
                    System.out.println("CRL: certificate revoked.");
                    return false;
                }
                System.out.println("CRL: certificate valid.");
                return true;
            } else {
                System.out.println("OCSP returned UNKNOWN but the strictMode is inactive → valid.");
                return true;
            }
    
        } catch (Exception e) {
            System.out.println("Error obtaining response OCSP to " + cert.getSubjectX500Principal() + ": " + e.getMessage());
    
            if (strictMode) {
                System.out.println("strictMode active → trying CRL...");
                try {
                    boolean crlValid = checkCRL(cert);
                    if (!crlValid) {
                        System.out.println("CRL: certificate revoked.");
                        return false;
                    }
                    System.out.println("CRL: certificate valid.");
                    return true;
                } catch (Exception ex) {
                    System.out.println("Error trying CRL: " + ex.getMessage());
                    return false;
                }
            } else {
                System.out.println("strictMode inactive → valid.");
                return true;
            }
        }
    }
    
    
    /*
     * Checks the revocation status of a certificate using OCSP.
     * @param cert
     * @param issuerCert
     * @return The revocation status of the certificate.
     * @throws Exception
     */
    private static void checkOCSP(X509Certificate cert, X509Certificate issuerCert) throws Exception {
        lastOCSPAttempt = null;
        revocationValidationResult = null;
        
        String cacheKey = generateCacheKey(cert);
        RevocationValidationResult cached = ocspCache.getIfPresent(cacheKey);

        if (cached != null) {
            System.out.println("OCSP response from cache for: " + cert.getSubjectX500Principal());
            revocationValidationResult = cached;
            return;
        }


        List<String> ocspUrls = getOCSPUrls(cert);
        if (ocspUrls.isEmpty()) {
            lastOCSPAttempt = new RevocationValidationResult(
                RevocationStatusEnum.UNKNOWN.toString(),
                RevocationTypeEnum.OCSP,
                null,
                "OCSP URL not found",
                Instant.now(),
                false
            );
            throw new Exception("OCSP URL not found");
        }        

        DigestCalculator digestCalculator = new JcaDigestCalculatorProviderBuilder()
        .build()
        .get(CertificateID.HASH_SHA1);

        CertificateID id = new CertificateID(
                digestCalculator,
                new JcaX509CertificateHolder(issuerCert),
                cert.getSerialNumber()
        );

        OCSPReqBuilder builder = new OCSPReqBuilder();
        builder.addRequest(id);
        BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());
        Hashtable<ASN1ObjectIdentifier, Extension> extensions = new Hashtable<>();
        extensions.put(OCSPObjectIdentifiers.id_pkix_ocsp_nonce,
                new Extension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce, false, new DEROctetString(nonce.toByteArray())));
        builder.setRequestExtensions(new Extensions(extensions.values().toArray(new Extension[0])));
        OCSPReq request = builder.build();


        for (String url : ocspUrls) {
            try {
                byte[] responseBytes = postOCSPRequest(url, request.getEncoded());
                OCSPResp response = new OCSPResp(responseBytes);
                if (response.getStatus() != OCSPResp.SUCCESSFUL) continue;

                BasicOCSPResp basicResp = (BasicOCSPResp) response.getResponseObject();
                if (basicResp != null && basicResp.getResponses().length > 0) {
                    CertificateStatus certStatus = basicResp.getResponses()[0].getCertStatus();
                
                    RevocationStatusEnum status = certStatus == CertificateStatus.GOOD ? RevocationStatusEnum.GOOD :
                                              certStatus instanceof RevokedStatus ? RevocationStatusEnum.REVOKED :
                                              RevocationStatusEnum.UNKNOWN;
                
                    RevocationValidationResult result = new RevocationValidationResult(
                            status.toString(),
                            RevocationTypeEnum.OCSP,
                            url,
                            "OCSP response received",
                            Instant.now(),
                            status == RevocationStatusEnum.GOOD
                    );
                
                    lastOCSPAttempt = result;

                    // Just cache the result if the status is not unknown
                    if (status != RevocationStatusEnum.UNKNOWN) {
                        ocspCache.put(cacheKey, result);
                    }
                
                    revocationValidationResult = result;
                    return;
                }                
            } catch (Exception e) {
                System.out.println("Error in OCSP call to " + cert.getSubjectX500Principal() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }       

        if (lastOCSPAttempt == null) {
            lastOCSPAttempt = new RevocationValidationResult(
                RevocationStatusEnum.UNKNOWN.toString(),
                RevocationTypeEnum.OCSP,
                (ocspUrls.isEmpty() ? null : ocspUrls.get(0)),
                (ocspUrls.isEmpty() ? "OCSP URL not found" : "OCSP response not received"),
                Instant.now(),
                false
            );
        }  
        
        if (revocationValidationResult == null) {
            revocationValidationResult = lastOCSPAttempt;
        } 
    }


    /*
     * Extracts the OCSP URLs from the certificate's Authority Information Access extension.
     * @param cert
     * @return A list of OCSP URLs.
     * @throws IOException
     */
    private static List<String> getOCSPUrls(X509Certificate cert) throws IOException {
        byte[] aiaExtension = cert.getExtensionValue(Extension.authorityInfoAccess.getId());
        if (aiaExtension == null) return Collections.emptyList();

        try (ASN1InputStream asn1 = new ASN1InputStream(aiaExtension)) {
            DEROctetString octets = (DEROctetString) asn1.readObject();
            try (ASN1InputStream asn1Seq = new ASN1InputStream(octets.getOctets())) {
                AuthorityInformationAccess aia = AuthorityInformationAccess.getInstance(asn1Seq.readObject());

                List<String> urls = new ArrayList<>();
                for (AccessDescription desc : aia.getAccessDescriptions()) {
                    if (desc.getAccessMethod().equals(X509ObjectIdentifiers.id_ad_ocsp)) {
                        GeneralName location = desc.getAccessLocation();
                        if (location.getTagNo() == GeneralName.uniformResourceIdentifier) {
                            DERIA5String str = DERIA5String.getInstance(location.getName());
                            urls.add(str.getString());
                        }
                    }
                }
                return urls;
            }
        }
    }


    /*
     * Sends an OCSP request to the specified URL.
     * @param serviceUrl
     * @param requestData
     * @return The OCSP response.
     * @throws IOException
     */
    private static byte[] postOCSPRequest(String serviceUrl, byte[] requestData) throws IOException {
        URL url = new URL(serviceUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Content-Type", "application/ocsp-request");
        conn.setRequestProperty("Accept", "application/ocsp-response");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setConnectTimeout(3000);
        conn.setReadTimeout(3000);
        try (OutputStream out = conn.getOutputStream()) {
            out.write(requestData);
            out.flush();
        }

        if (conn.getResponseCode() / 100 != 2) {
            throw new IOException("Invalid OCSP response code: " + conn.getResponseCode());
        }

        try (InputStream in = conn.getInputStream()) {
            return in.readAllBytes();
        }
    }


    /*
     * Checks the revocation status of a certificate using CRL.
     * @param cert
     * @return true if the certificate is valid, false otherwise.
     * @throws Exception
     */
    public static boolean checkCRL(X509Certificate cert) throws Exception {
        System.out.println("Verifying CRL to: " + cert.getSubjectX500Principal());

        byte[] ext = cert.getExtensionValue(Extension.cRLDistributionPoints.getId());
        if (ext == null) {
            System.out.println("Certificate dont have CRL extension.");
            revocationValidationResult = new RevocationValidationResult(
                    RevocationStatusEnum.GOOD.toString(),
                    RevocationTypeEnum.CRL,
                    null,
                    "Certificate dont have CRL extension.",
                    Instant.now(),
                    true
            );

            return true;
        }


        List<String> crlUrls = getCRLUrls(cert);
        String lastUsedUrl = null;
        for (String url : crlUrls) {
            lastUsedUrl = url;
            try (InputStream in = new URL(url).openStream()) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                X509CRL crl = (X509CRL) cf.generateCRL(in);

                if (crl.isRevoked(cert)) {
                    System.out.println("Revoked certificate in CRL: " + url);
                    revocationValidationResult = new RevocationValidationResult(
                            RevocationStatusEnum.REVOKED.toString(),
                            RevocationTypeEnum.CRL,
                            url,
                            "Revoked certificate in CRL",
                            Instant.now(),
                            false
                    );

                    return false;
                } else {
                    System.out.println("Certificate is not in CRL list:: " + url);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        revocationValidationResult = new RevocationValidationResult(
                RevocationStatusEnum.GOOD.toString(),
                RevocationTypeEnum.CRL,
                lastUsedUrl,
                "Certificate is not in CRL list",
                Instant.now(),
                true
        );

        return true;
    }


    /*
     * Extracts the CRL URLs from the certificate's CRL Distribution Points extension.
     * @param cert
     * @return A list of CRL URLs.
     * @throws IOException
     */
    private static List<String> getCRLUrls(X509Certificate cert) throws IOException {
        byte[] ext = cert.getExtensionValue(Extension.cRLDistributionPoints.getId());
        if (ext == null) return Collections.emptyList();
    
        ASN1InputStream asn1InputStream = new ASN1InputStream(ext);
        DEROctetString derOctetString = (DEROctetString) asn1InputStream.readObject();
        asn1InputStream.close();
    
        asn1InputStream = new ASN1InputStream(derOctetString.getOctets());
        ASN1Sequence seq = (ASN1Sequence) asn1InputStream.readObject();
        asn1InputStream.close();
    
        List<String> urls = new ArrayList<>();
    
        for (Enumeration<?> e = seq.getObjects(); e.hasMoreElements(); ) {
            DistributionPoint dp = DistributionPoint.getInstance(e.nextElement());
            DistributionPointName dpName = dp.getDistributionPoint();
            if (dpName != null && dpName.getType() == DistributionPointName.FULL_NAME) {
                GeneralName[] genNames = GeneralNames.getInstance(dpName.getName()).getNames();
                for (GeneralName genName : genNames) {
                    if (genName.getTagNo() == GeneralName.uniformResourceIdentifier) {
                        String url = DERIA5String.getInstance(genName.getName()).getString();
                        urls.add(url);
                    }
                }
            }
        }
    
        return urls;
    }
    

    /*
     * Generates a unique key for the OCSP cache.
     * @param cert
     * @param issuerCert
     * @return The cache key.
     */
    private static String generateCacheKey(X509Certificate cert) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedCert = cert.getEncoded();
            byte[] hash = digest.digest(encodedCert);
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            return Integer.toHexString(cert.hashCode());
        }
    }
    
    

    /*
     * Gets the last revocation validation result.
     * @return The last revocation validation result.
     */
    public static RevocationValidationResult getRevocationValidationResult() {
        return revocationValidationResult;
    }


    /*
     * Gets the last OCSP attempt.
     * @return The last OCSP attempt.
     */
    public static RevocationValidationResult getLastOCSPAttempt() {
        return lastOCSPAttempt;
    }

}