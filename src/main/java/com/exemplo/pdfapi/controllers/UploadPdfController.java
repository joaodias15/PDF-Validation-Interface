package com.exemplo.pdfapi.controllers;

import com.exemplo.pdfapi.domain.Report;
import com.exemplo.pdfapi.services.PDFSignatureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

/**
 * Controller for handling PDF-related operations such as uploading and validating PDFs.
 */
@CrossOrigin(origins = "http://localhost:4200") 
@RestController
@RequestMapping("/api/pdf")
public class UploadPdfController {

    private final PDFSignatureService pdfSignatureService;

    /**
     * Constructs a new PdfController with the specified PDF signature service.
     *
     * @param pdfSignatureService the service for processing PDF signatures
     */
    public UploadPdfController(PDFSignatureService pdfSignatureService) {
        this.pdfSignatureService = pdfSignatureService;
    }

    /**
     * Handles the upload of a PDF file and processes its digital signature.
     *
     * @param file the uploaded PDF file
     * @return a ResponseEntity containing the report or an error message
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadPdf(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Please select a PDF file."));
        } else if (!file.getContentType().equals("application/pdf")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Please select a PDF file.")); 
        }

        try {
            Report report = pdfSignatureService.processAndExtractSignature(file);

            if (report == null) {
                return ResponseEntity.status(500).body(Map.of("error", "Error extracting signature from PDF."));
            }

            return ResponseEntity.ok(report);

        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error processing the file: " + e.getMessage()));
        }
    }
}
