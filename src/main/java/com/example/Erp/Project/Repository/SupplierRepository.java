package com.example.Erp.Project.Repository;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.Erp.Project.Entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query("""
        SELECT s FROM Supplier s
        WHERE s.active = true
        AND (
            :search IS NULL
            OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(s.email) LIKE LOWER(CONCAT('%', :search, '%'))
            OR s.phone LIKE CONCAT('%', :search, '%')
        )
    """)
    Page<Supplier> search(String search, Pageable pageable);

    Page<Supplier> findByActiveTrue(Pageable pageable);

    boolean existsByEmailAndActiveTrue(String email);
    boolean existsByPhoneAndActiveTrue(String phone);
    boolean existsByGstinAndActiveTrue(String gstin);


}
