package com.example.Erp.Project.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.example.Erp.Project.Entity.PurchaseOrder;

public interface PurchaseOrderRepository
        extends JpaRepository<PurchaseOrder, Long> {

	@Query("""
			SELECT COALESCE(SUM(i.quantity * i.unitPrice),0)
			FROM PurchaseOrder p
			JOIN p.items i
			WHERE p.orderDate BETWEEN :start AND :end
			""")
			BigDecimal totalPurchasesBetweenDates(
			        @Param("start") LocalDate start,
			        @Param("end") LocalDate end
			);

}
