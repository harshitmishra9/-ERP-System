package com.example.Erp.Project.Service;

import org.springframework.data.domain.Page;

import com.example.Erp.Project.Entity.Customer;

public interface CustomerService {
    Customer save(Customer customer);
    Customer update(Long id, Customer customer);
    void delete(Long id);
    Customer findById(Long id);

    Page<Customer> findAll(int page, int size, String search);
}

