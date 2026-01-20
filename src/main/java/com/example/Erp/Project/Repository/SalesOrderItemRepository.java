package com.example.Erp.Project.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.Erp.Project.Entity.SalesOrderItem;

public interface SalesOrderItemRepository extends JpaRepository<SalesOrderItem, Long> {
	@Query("""
			SELECT i.product.name, SUM(i.quantity)
			FROM SalesOrderItem i
			GROUP BY i.product.name
			ORDER BY SUM(i.quantity) DESC
			""")
			List<Object[]> topSellingProducts();

}