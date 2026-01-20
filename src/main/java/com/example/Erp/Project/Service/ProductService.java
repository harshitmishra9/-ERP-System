package com.example.Erp.Project.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.Erp.Project.Entity.Product;

public interface ProductService {
    Product save(Product product);
    Product update(Long id, Product product);
    void delete(Long id);
    Page<Product> findAll(int page, int size, String search);
    List<Product> getLowStock();

}
