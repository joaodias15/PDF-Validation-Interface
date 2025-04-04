package com.exemplo.pdfapi.controllers;

import com.exemplo.pdfapi.domain.Report;
import com.exemplo.pdfapi.services.PDFSignatureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pdf/byID")
public class PdfByIDController {

    private final PDFSignatureService signatureService;

    /**
     * Constructs a new PdfByIDController with the specified PDF signature service.
     *
     * @param signatureService the service for processing PDF signatures
     */
    public PdfByIDController(PDFSignatureService signatureService) {
        this.signatureService = signatureService;
    }

    /**
     * Validates a PDF file by its ID.
     *
     * @param fileId the ID of the PDF file
     * @return a ResponseEntity containing the validation report or an error message
     */
    @GetMapping("/{fileId}")
    public ResponseEntity<Report> validateByID(@PathVariable String fileId) {
        Report report = signatureService.validateByID(fileId);

        if (report == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(report);
    }
}
