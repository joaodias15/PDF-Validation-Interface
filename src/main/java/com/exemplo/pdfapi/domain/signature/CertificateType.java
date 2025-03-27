package com.exemplo.pdfapi.domain.signature;


/**
 * Enum to represent the type of certificate
 * 
 * ENTITY: Certificate used to sign the document
 * INTERMEDIATE: Certificates used to sign the ENTITY certificate
 * ROOT: Certificate used to sign the INTERMEDIATE certificate
 */
public enum CertificateType {
    ENTITY, INTERMEDIATE, ROOT
}
