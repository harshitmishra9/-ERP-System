package com.example.Erp.Project.Service;

import java.util.List;

import com.example.Erp.Project.Entity.Invoice;

public interface InvoiceService {
    Invoice generateInvoice(Long salesOrderId);
    byte[] generateInvoicePdf(Invoice invoice) throws Exception;
    List<Invoice> listInvoices();
}

