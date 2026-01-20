package com.example.Erp.Project.Controller;


import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Erp.Project.Entity.Invoice;
import com.example.Erp.Project.Repository.InvoiceRepository;
import com.example.Erp.Project.Service.InvoiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService service;
    private final InvoiceRepository invoiceRepo;

    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_EXECUTIVE')")
    @PostMapping("/generate/{salesOrderId}")
    public Invoice generate(@PathVariable Long salesOrderId) {
        return service.generateInvoice(salesOrderId);
    }

    @GetMapping
    public List<Invoice> list() {
        return service.listInvoices();
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) throws Exception {

        Invoice invoice = invoiceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        byte[] pdf = service.generateInvoicePdf(invoice);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=invoice_" + id + ".pdf")
                .body(pdf);
    }
}
