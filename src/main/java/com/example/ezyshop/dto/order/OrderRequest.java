package com.example.ezyshop.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequest {

    private String address_id;

    private List<OrderItemRequest> items;

}
