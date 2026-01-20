package com.example.Erp.Project.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.Erp.Project.Entity.Supplier;
import com.example.Erp.Project.Repository.SupplierRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository repo;

    @Override
    public Supplier createSupplier(Supplier supplier) {

        if (supplier.getEmail() != null &&
            repo.existsByEmailAndActiveTrue(supplier.getEmail())) {
            throw new RuntimeException("Supplier email already exists");
        }

        if (repo.existsByPhoneAndActiveTrue(supplier.getPhone())) {
            throw new RuntimeException("Supplier phone already exists");
        }

        if (supplier.getGstin() != null &&
            repo.existsByGstinAndActiveTrue(supplier.getGstin())) {
            throw new RuntimeException("Supplier GSTIN already exists");
        }

        return repo.save(supplier);
    }


    @Override
    public Supplier updateSupplier(Long id, Supplier supplier) {

        Supplier s = repo.findById(id)
            .filter(Supplier::isActive)
            .orElseThrow(() -> new RuntimeException("Supplier not found"));

        if (supplier.getEmail() != null &&
            !supplier.getEmail().equals(s.getEmail()) &&
            repo.existsByEmailAndActiveTrue(supplier.getEmail())) {
            throw new RuntimeException("Supplier email already exists");
        }

        if (!supplier.getPhone().equals(s.getPhone()) &&
            repo.existsByPhoneAndActiveTrue(supplier.getPhone())) {
            throw new RuntimeException("Supplier phone already exists");
        }

        if (supplier.getGstin() != null &&
            !supplier.getGstin().equals(s.getGstin()) &&
            repo.existsByGstinAndActiveTrue(supplier.getGstin())) {
            throw new RuntimeException("Supplier GSTIN already exists");
        }

        s.setName(supplier.getName());
        s.setEmail(supplier.getEmail());
        s.setPhone(supplier.getPhone());
        s.setAddress(supplier.getAddress());
        s.setGstin(supplier.getGstin());

        return repo.save(s);
    }


    @Override
    public void deleteSupplier(Long id) {
        Supplier c = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        c.setActive(false);
        repo.save(c);
    }


    @Override
    public Supplier getSupplier(Long id) {
        return repo.findById(id)
            .filter(Supplier::isActive)
            .orElseThrow(() -> new RuntimeException("Supplier not found"));
    }

    @Override
    public Page<Supplier> listSuppliers(String search, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));

        if (search != null && !search.isEmpty()) {
            return repo.search(search, pageable);
        }

        return repo.findByActiveTrue(pageable);
    }

}
