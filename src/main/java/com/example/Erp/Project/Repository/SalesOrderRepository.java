package com.example.Erp.Project.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Erp.Project.Entity.*;
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {}

