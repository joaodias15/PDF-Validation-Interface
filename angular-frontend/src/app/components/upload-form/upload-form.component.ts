import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PdfUploadService } from '../../services/pdf-upload.service';
import { ValidationUtils } from '../../utils/validation-utils';

@Component({
  selector: 'app-upload-form',
  standalone: true,
  templateUrl: './upload-form.component.html',
  styleUrls: ['./upload-form.component.css'],
  imports: [CommonModule]
})


export class UploadFormComponent {
  selectedFile: File | null = null;
  responseData: any = null;
  loading = false;
  showModal = false;
  errorMessage: string | null = null;

  constructor(private pdfUploadService: PdfUploadService) {}

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  uploadFile() {
    this.errorMessage = null;
  
    if (!this.selectedFile) {
      this.errorMessage = "Select a file first!";
      return;
    }
  
    this.loading = true;
    this.pdfUploadService.uploadPdf(this.selectedFile).subscribe({
      next: (data) => {
        this.responseData = data;
        this.showModal = true;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        if (err.error?.errors?.length) {
          this.errorMessage = err.error.errors[0];
        } else if (err.error?.message) {
          this.errorMessage = err.error.message;
        } else {
          this.errorMessage = "Unexpected error while processing the file.";
        }
      }
    });
  }
  

  getCertificateByType(certificates: any[], type: string): any {
    return certificates.find(c => c.type === type);
  }
  
  isSignatureValid = ValidationUtils.isSignatureValid;
  getInvalidCertificates = ValidationUtils.getInvalidCertificates;

  closeModal() {
    this.showModal = false;
  }

  
}
