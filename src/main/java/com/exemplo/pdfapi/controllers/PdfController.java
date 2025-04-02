package com.exemplo.pdfapi.controllers;

import com.exemplo.pdfapi.domain.Report;
import com.exemplo.pdfapi.services.PDFSignatureService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200") 
@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    private final PDFSignatureService pdfSignatureService;
    
    @Value("${pdf.storage.path}")
    private String pdfStoragePath;



    public PdfController(PDFSignatureService pdfSignatureService) {
        this.pdfSignatureService = pdfSignatureService;
    }

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
            return ResponseEntity.status(500).body(Map.of("error", "Error processing tthe file: " + e.getMessage()));
        }
    }


    
    @GetMapping("/validate/{fileId}")
    public ResponseEntity<Report> validateByFileId(@PathVariable String fileId) {
        File file = new File(pdfStoragePath + "/" + fileId + ".pdf");

        if (!file.exists()) {
            System.out.println("File not found: " + file.getAbsolutePath());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Report report = pdfSignatureService.extractSignature(file);
        return ResponseEntity.ok(report);
    }
    

}
