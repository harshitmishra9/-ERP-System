package com.example.Erp.Project.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate orderDate;

    private LocalDate expectedDelivery;

    @Enumerated(EnumType.STRING)
    private PurchaseOrderStatus status;

    private BigDecimal totalAmount = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier supplier;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    private List<PurchaseItem> items;

    @OneToMany(mappedBy = "purchaseOrder")
    private List<GRN> grns;

    @PrePersist
    public void prePersist() {
        orderDate = LocalDate.now();
        status = PurchaseOrderStatus.CREATED;
    }

    public void calculateTotal() {
        this.totalAmount = items.stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
