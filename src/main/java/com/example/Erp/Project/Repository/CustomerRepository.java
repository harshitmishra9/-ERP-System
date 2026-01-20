package com.example.Erp.Project.Repository;

import org.springframework.data.domain.Pageable;


import org.springframework.data.domain.Page;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Erp.Project.Entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("""
        SELECT c FROM Customer c
        WHERE c.active = true
        AND (
            :search IS NULL
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%'))
            OR c.phone LIKE CONCAT('%', :search, '%')
        )
    """)
    Page<Customer> search(
            @Param("search") String search,
            Pageable pageable
    );
    boolean existsByEmailAndActiveTrue(String email);
    boolean existsByPhoneAndActiveTrue(String phone);
    Page<Customer> findByActiveTrue(Pageable pageable);


}

