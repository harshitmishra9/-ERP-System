package com.example.Erp.Project.Controller;



import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import com.example.Erp.Project.Entity.Supplier;
import com.example.Erp.Project.Service.SupplierService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService service;

    @PreAuthorize("hasRole('ADMIN') or hasRole('PURCHASE_MANAGER')")
    @PostMapping
    public Supplier create(@Valid @RequestBody Supplier supplier) {
        return service.createSupplier(supplier);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PURCHASE_MANAGER')")
    @PutMapping("/{id}")
    public Supplier update(@PathVariable Long id, @Valid @RequestBody Supplier supplier) {
        return service.updateSupplier(id, supplier);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteSupplier(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PURCHASE_MANAGER')")
    @GetMapping("/{id}")
    public Supplier get(@PathVariable Long id) {
        return service.getSupplier(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('PURCHASE_MANAGER')")
    @GetMapping
    public Page<Supplier> list(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.listSuppliers(search, page, size);
    }
}

