package kh.gov.mptc.koklim.ecommerce.order.restapi.mapper;

import kh.gov.mptc.koklim.ecommerce.order.application.dto.CreateOrderCommand;
import kh.gov.mptc.koklim.ecommerce.order.application.dto.CreateOrderResult;
import kh.gov.mptc.koklim.ecommerce.order.restapi.dto.CreateOrderRequest;
import kh.gov.mptc.koklim.ecommerce.order.restapi.dto.CreateOrderResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderWebMapper {
    //Source = CreateOrderRequest, Target = CreateOrderCommand
    CreateOrderCommand toCreateOrderCommand(CreateOrderRequest createOrderRequest);
    CreateOrderResponse toCreateOrderResponse(CreateOrderResult createOrderResult);
}
