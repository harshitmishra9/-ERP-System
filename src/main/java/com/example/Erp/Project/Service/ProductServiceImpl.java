package com.example.Erp.Project.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.Erp.Project.Entity.Product;
import com.example.Erp.Project.Repository.ProductRepository;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    @Override
    public Product update(Long id, Product product) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (!existing.getSku().equals(product.getSku())
                && repo.existsBySku(product.getSku())) {
            throw new IllegalArgumentException("SKU already exists");
        }

        existing.setName(product.getName());
        existing.setSku(product.getSku());
        existing.setCategory(product.getCategory());
        existing.setUnitPrice(product.getUnitPrice());
        existing.setCurrentStock(product.getCurrentStock());
        existing.setReorderLevel(product.getReorderLevel());

        return repo.save(existing);
    }


    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public Page<Product> findAll(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return repo.search(search, null, pageable);
    }

    @Override
    public Product save(Product product) {
        if (repo.existsBySku(product.getSku())) {
            throw new IllegalArgumentException("SKU already exists");
        }
        return repo.save(product);
    }
    @Override
    public List<Product> getLowStock() {
        return repo.findLowStock();
    }

}
