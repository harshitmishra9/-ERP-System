package com.example.Erp.Project.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Erp.Project.Entity.*;
import com.example.Erp.Project.Repository.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepo;
    private final SalesOrderRepository salesRepo;

    @Override
    @Transactional
    public Invoice generateInvoice(Long salesOrderId) {

        SalesOrder order = salesRepo.findById(salesOrderId)
                .orElseThrow(() -> new RuntimeException("Sales Order not found"));

        if (order.getStatus() != SalesOrderStatus.APPROVED) {
            throw new RuntimeException("Only APPROVED orders allowed");
        }

        if (invoiceRepo.existsBySalesOrder(order)) {
            throw new RuntimeException("Invoice already exists");
        }

        Invoice invoice = new Invoice();
        invoice.setSalesOrder(order);

        invoice.setTax(BigDecimal.valueOf(18));
        invoice.calculateTotal();

        invoice.setStatus("UNPAID");

        return invoiceRepo.save(invoice);
    }

    @Override
    public byte[] generateInvoicePdf(Invoice invoice) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter.getInstance(doc, out);

        doc.open();

        doc.add(new Paragraph("INVOICE"));
        doc.add(new Paragraph("Invoice ID: " + invoice.getId()));
        doc.add(new Paragraph("Customer: " +
                invoice.getSalesOrder().getCustomer().getName()));
        doc.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        table.addCell("Product");
        table.addCell("Qty");
        table.addCell("Price");
        table.addCell("Total");

        for (SalesOrderItem item : invoice.getSalesOrder().getItems()) {

            table.addCell(item.getProduct().getName());
            table.addCell(item.getQuantity().toString());
            table.addCell(item.getUnitPrice().toString());

            table.addCell(
                    item.getUnitPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()))
                            .toString()
            );
        }

        doc.add(table);

        doc.add(new Paragraph("GST: " + invoice.getTax() + "%"));
        doc.add(new Paragraph("Total Payable: " + invoice.getTotalPayable()));

        doc.close();

        return out.toByteArray();
    }

    @Override
    public List<Invoice> listInvoices() {
        return invoiceRepo.findAll();
    }
}
