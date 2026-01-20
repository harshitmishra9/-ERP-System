package com.example.Erp.Project.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class TopProductDTO {

    private String productName;
    private Long totalQuantitySold;
}
