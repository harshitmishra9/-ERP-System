package com.example.Erp.Project.Service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import com.example.Erp.Project.Entity.Customer;
import com.example.Erp.Project.Repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repo;

    @Override
    public Customer save(Customer customer) {

        if (customer.getEmail() != null &&
            repo.existsByEmailAndActiveTrue(customer.getEmail())) {
            throw new RuntimeException("Customer email already exists");
        }

        if (repo.existsByPhoneAndActiveTrue(customer.getPhone())) {
            throw new RuntimeException("Customer phone already exists");
        }

        return repo.save(customer);
    }

    @Override
    public Customer update(Long id, Customer customer) {

        Customer existing = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (customer.getEmail() != null &&
            !customer.getEmail().equals(existing.getEmail()) &&
            repo.existsByEmailAndActiveTrue(customer.getEmail())) {
            throw new RuntimeException("Customer email already exists");
        }

        if (!customer.getPhone().equals(existing.getPhone()) &&
            repo.existsByPhoneAndActiveTrue(customer.getPhone())) {
            throw new RuntimeException("Customer phone already exists");
        }

        existing.setName(customer.getName());
        existing.setEmail(customer.getEmail());
        existing.setPhone(customer.getPhone());
        existing.setAddress(customer.getAddress());
        existing.setGstin(customer.getGstin());

        return repo.save(existing);
    }


    @Override
    public void delete(Long id) {
        Customer c = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        c.setActive(false);
        repo.save(c);
    }


    @Override
    public Page<Customer> findAll(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        if (search != null && !search.isEmpty()) {
            return repo.search(search, pageable);
        }
        return repo.findAll(pageable);
    }
    @Override
    public Customer findById(Long id) {
        return repo.findById(id)
            .filter(Customer::isActive)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
    }


}
