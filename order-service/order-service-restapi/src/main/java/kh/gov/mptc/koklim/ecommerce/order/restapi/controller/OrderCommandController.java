package kh.gov.mptc.koklim.ecommerce.order.restapi.controller;


import jakarta.validation.Valid;
import kh.gov.mptc.koklim.ecommerce.order.application.dto.CreateOrderResult;
import kh.gov.mptc.koklim.ecommerce.order.application.port.input.CreateOrderPort;
import kh.gov.mptc.koklim.ecommerce.order.restapi.dto.CreateOrderRequest;
import kh.gov.mptc.koklim.ecommerce.order.restapi.dto.CreateOrderResponse;
import kh.gov.mptc.koklim.ecommerce.order.restapi.mapper.OrderWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor //auto inject constructor
public class OrderCommandController {

    private final CreateOrderPort createOrderPort;
    private final OrderWebMapper orderWebMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        CreateOrderResult createOrderResult = createOrderPort.execute(
                orderWebMapper.toCreateOrderCommand(createOrderRequest)
        );

        return orderWebMapper.toCreateOrderResponse(createOrderResult);
    }
}
