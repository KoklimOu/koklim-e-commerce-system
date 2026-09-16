package kh.gov.mptc.koklim.ecommerce.order.restapi.controller;


import kh.gov.mptc.koklim.ecommerce.order.restapi.dto.OrderCreateRequest;
import kh.gov.mptc.koklim.ecommerce.order.restapi.dto.OrderCreateResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderCreateResponse createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        return OrderCreateResponse.builder().orderId(UUID.randomUUID()).build();
    }
}
