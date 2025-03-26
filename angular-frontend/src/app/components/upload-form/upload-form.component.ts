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

  constructor(private pdfUploadService: PdfUploadService) {}

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  uploadFile() {
    if (!this.selectedFile) {
      alert("Select a file first!");
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
        console.error("Error uploading:", err);
        alert("Error processing the file.");
        this.loading = false;
      }
    });
  }

  isSignatureValid = ValidationUtils.isSignatureValid;
  getInvalidCertificates = ValidationUtils.getInvalidCertificates;

  closeModal() {
    this.showModal = false;
  }

  
}
