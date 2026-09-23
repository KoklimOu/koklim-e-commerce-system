package kh.gov.mptc.koklim.ecommerce.order.application.mapper;

import kh.gov.mptc.koklim.ecommerce.order.application.dto.CreateOrderCommand;
import kh.gov.mptc.koklim.ecommerce.order.application.dto.OrderItemCommand;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Order;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDomainMapper {

    @Mapping(source = "customerId", target = "customerId.id")
    @Mapping(source = "businessId", target = "businessId.id")
    @Mapping(source = "orderAddress", target = "deliveryAddress")
    @Mapping(source = "price", target = "price.amount")
    Order createdOrderCommandToOrder(CreateOrderCommand createOrderCommand);

    @Mapping(source = "productId", target = "product.id.id")
    @Mapping(source = "price", target = "price.amount")
    @Mapping(source = "subtotal", target = "subTotal.amount")
    OrderItem commandOrderItemToOrderItem(OrderItemCommand orderItemCommand);
}
