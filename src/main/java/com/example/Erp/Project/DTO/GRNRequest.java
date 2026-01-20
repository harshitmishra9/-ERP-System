package com.example.Erp.Project.DTO;



import java.util.List;

public record GRNRequest(
        Long purchaseOrderId,
        List<GRNItemRequest> items
) {}
