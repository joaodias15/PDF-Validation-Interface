export class ValidationUtils {

    /*
     * Check if all certificates in the array are valid.
     * A certificate is considered valid if all of the following are true:
     * - dataValidation is true
     * - hashValidation is true
     * - revocationValidationResult is true
     * 
     * @param certificates Array of certificates to check
     * @returns True if all certificates are valid, false otherwise
     */
    static isSignatureValid(certificates: any[]): boolean {
      return certificates.every(c =>
        c.dataValidation &&
        c.hashValidation?.hashValidation &&
        c.revocationValidationResult?.result
      );
    }

  
    /*
     * Get all certificates in the array that are invalid.
     * A certificate is considered invalid if any of the following are true:
     * - dataValidation is false
     * - hashValidation.hashValidation is false
     * - revocationValidationResult.result is false
     *   
     * @param certificates Array of certificates to check
     * @returns Array of invalid certificates
     */
    static getInvalidCertificates(certificates: any[]): any[] {
      return certificates.filter(c =>
        !c.dataValidation ||
        !c.hashValidation?.hashValidation ||
        !c.revocationValidationResult?.result
      );
    }
  }
  