import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PdfUploadService } from '../../services/pdf-upload.service';

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
      alert("Selecione um ficheiro primeiro!");
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
        console.error("Erro no upload:", err);
        alert("Erro ao processar o ficheiro.");
        this.loading = false;
      }
    });
  }

  closeModal() {
    this.showModal = false;
  }
}
