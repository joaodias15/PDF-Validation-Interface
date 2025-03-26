export class ValidationUtils {
    static isSignatureValid(certificates: any[]): boolean {
      return certificates.every(c =>
        c.dataValidation &&
        c.hashValidation?.hashValidation &&
        c.revocationValidationResult?.result
      );
    }
  
    static getInvalidCertificates(certificates: any[]): any[] {
      return certificates.filter(c =>
        !c.dataValidation ||
        !c.hashValidation?.hashValidation ||
        !c.revocationValidationResult?.result
      );
    }
  }
  