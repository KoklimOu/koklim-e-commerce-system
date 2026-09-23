package kh.gov.mptc.koklim.ecommerce.order.restapi.controller;


import jakarta.validation.Valid;
import kh.gov.mptc.koklim.ecommerce.order.application.dto.CreateOrderResult;
import kh.gov.mptc.koklim.ecommerce.order.application.usecase.CreateOrderUseCase;
import kh.gov.mptc.koklim.ecommerce.order.restapi.dto.OrderCreateRequest;
import kh.gov.mptc.koklim.ecommerce.order.restapi.dto.OrderCreateResponse;
import kh.gov.mptc.koklim.ecommerce.order.restapi.mapper.OrderWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor //auto inject constructor
public class OrderCommandController {

    private final CreateOrderUseCase createOrderUseCase;
    private final OrderWebMapper orderWebMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderCreateResponse createOrder(@Valid @RequestBody OrderCreateRequest orderCreateRequest) {
        createOrderUseCase.execute(
                orderWebMapper.orderCreateRequestToCreateOrderCommand(orderCreateRequest)
        );

        CreateOrderResult createOrderResult = new CreateOrderResult(UUID.randomUUID());
        return orderWebMapper.createOrderResultToOrderCreateResponse(createOrderResult);
    }
}
