package com.example.Erp.Project.Service;

import org.springframework.data.domain.Page;

import com.example.Erp.Project.Entity.Supplier;

public interface SupplierService {
    Supplier createSupplier(Supplier supplier);
    Supplier updateSupplier(Long id, Supplier supplier);
    void deleteSupplier(Long id);
    Supplier getSupplier(Long id);
    Page<Supplier> listSuppliers(String search, int page, int size);
}

