package com.example.Erp.Project.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.Erp.Project.Entity.Product;
import com.example.Erp.Project.Entity.SalesOrder;
import com.example.Erp.Project.Entity.SalesOrderItem;
import com.example.Erp.Project.Entity.SalesOrderStatus;
import com.example.Erp.Project.Repository.ProductRepository;
import com.example.Erp.Project.Repository.SalesOrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository orderRepo;
    private final ProductRepository productRepo;

    @Transactional
    @Override
    public SalesOrder createOrder(SalesOrder order) {
        order.getItems().forEach(item -> item.setUnitPrice(item.getProduct().getUnitPrice()));
        order.getItems().forEach(item -> item.setSalesOrder(order));
        order.calculateTotal();
        return orderRepo.save(order);
    }

    @Transactional
    @Override
    public SalesOrder approveOrder(Long id) {

        SalesOrder order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != SalesOrderStatus.CREATED) {
            throw new RuntimeException("Order already processed");
        }

        order.setStatus(SalesOrderStatus.APPROVED);


        for (SalesOrderItem item : order.getItems()) {

            Product product = item.getProduct();

            if (product.getCurrentStock() < item.getQuantity()) {
                throw new RuntimeException(
                    "Insufficient stock for " + product.getName()
                );
            }

            product.setCurrentStock(
                product.getCurrentStock() - item.getQuantity()
            );
            productRepo.save(product);
        }

        order.setStatus(SalesOrderStatus.APPROVED);

        return orderRepo.save(order);
    }


    @Override
    public Page<SalesOrder> listOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());
        return orderRepo.findAll(pageable);
    }
}
