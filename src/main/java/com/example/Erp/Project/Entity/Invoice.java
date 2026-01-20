package com.example.Erp.Project.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private SalesOrder salesOrder;

    private BigDecimal tax = BigDecimal.ZERO;
    private BigDecimal totalPayable = BigDecimal.ZERO;

    private String status = "UNPAID";

    private LocalDate createdDate = LocalDate.now();

    public void calculateTotal() {
        if (salesOrder == null) return;

        BigDecimal base = salesOrder.getTotalAmount();
        BigDecimal taxAmount = base.multiply(tax).divide(BigDecimal.valueOf(100));
        this.totalPayable = base.add(taxAmount);
    }
}
