package kh.gov.mptc.koklim.ecommerce.order.restapi.mapper;

import kh.gov.mptc.koklim.ecommerce.order.application.dto.CreateOrderCommand;
import kh.gov.mptc.koklim.ecommerce.order.application.dto.CreateOrderResult;
import kh.gov.mptc.koklim.ecommerce.order.restapi.dto.OrderCreateRequest;
import kh.gov.mptc.koklim.ecommerce.order.restapi.dto.OrderCreateResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderWebMapper {
    //Source = OrderCreateRequest, Target = CreateOrderCommand
    CreateOrderCommand សំណើបង្កើតការបញ្ជាទិញទៅកាន់ពាក្យបញ្ជាបង្កើតការបញ្ជាទិញ(OrderCreateRequest orderCreateRequest);
    OrderCreateResponse បំប្លែងលទ្ធផលនៃការបង្កើតការបញ្ជាទិញទៅជាការឆ្លើយតបនៃការបង្កើតការបញ្ជាទិញ(CreateOrderResult createOrderResult);
}
