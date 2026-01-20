package com.example.Erp.Project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Erp.Project.Entity.*;
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {}