package com.example.Erp.Project.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Erp.Project.Entity.*;
public interface GRNRepository extends JpaRepository<GRN, Long> {

	boolean existsByPurchaseOrder(PurchaseOrder purchaseOrder);

}

