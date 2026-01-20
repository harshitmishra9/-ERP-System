package com.example.Erp.Project.Service;

import com.example.Erp.Project.Entity.Invoice;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public byte[] generateInvoicePdf(Invoice invoice) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("ERP INVOICE"));
            document.add(new Paragraph("Invoice ID: " + invoice.getId()));

            // ✅ FIXED CUSTOMER ACCESS
            if (invoice.getSalesOrder() != null &&
                invoice.getSalesOrder().getCustomer() != null) {

                document.add(new Paragraph(
                    "Customer: " +
                    invoice.getSalesOrder().getCustomer().getName()
                ));
            } else {
                document.add(new Paragraph("Customer: N/A"));
            }

            document.add(new Paragraph(
                "Total Amount: " + invoice.getTotalPayable()
            ));

            document.add(new Paragraph(
                "Status: " + invoice.getStatus()
            ));

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}
