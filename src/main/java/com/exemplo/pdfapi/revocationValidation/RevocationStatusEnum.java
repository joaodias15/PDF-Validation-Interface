package com.exemplo.pdfapi.revocationValidation;

/*
* Enum to represent the revocation status of a certificate.
* GOOD: The certificate is valid.
* REVOKED: The certificate is revoked.
* UNKNOWN: The revocation status is unknown.
*/
public enum RevocationStatusEnum {
    GOOD, REVOKED, UNKNOWN
}
