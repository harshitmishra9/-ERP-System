package com.example.Erp.Project.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.Erp.Project.Entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    @Query("""
    	    SELECT p FROM Product p
    	    WHERE (:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')))
    	    AND (:category IS NULL OR p.category = :category)
    	""")
    	Page<Product> search(String search, String category, Pageable pageable);

    @Query("""
    		SELECT p FROM Product p
    		WHERE p.currentStock <= p.minStockLevel
    		""")
    		List<Product> findLowStock();

    boolean existsBySku(String sku);
}


