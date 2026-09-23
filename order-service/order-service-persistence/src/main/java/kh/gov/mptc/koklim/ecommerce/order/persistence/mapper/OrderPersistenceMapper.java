package kh.gov.mptc.koklim.ecommerce.order.persistence.mapper;

import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Order;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.OrderItem;
import kh.gov.mptc.koklim.ecommerce.order.persistence.entity.OrderEntity;
import kh.gov.mptc.koklim.ecommerce.order.persistence.entity.OrderItemEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderPersistenceMapper {

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "customerId.id", target = "customerId")
    @Mapping(source = "businessId.id", target = "businessId")
    @Mapping(source = "price.amount", target = "price")
    @Mapping(source = "trackingId.id", target = "trackingId")
    @Mapping(source = "failureMessages", target = "failureMessages", qualifiedByName = "mapFailureMessages")
    OrderEntity orderToOrderEntity(Order order);

    // Address and items own the order_id foreign key, so each child must point back at its order
    @AfterMapping
    default void linkChildrenToOrder(@MappingTarget OrderEntity orderEntity) {
        if (orderEntity.getDeliveryAddress() != null) {
            orderEntity.getDeliveryAddress().setOrder(orderEntity);
        }
        if (orderEntity.getItems() != null) {
            orderEntity.getItems().forEach(item -> item.setOrder(orderEntity));
        }
    }

    @Named("mapFailureMessages")
    default String mapFailureMessages(List<String> failureMessages) {
        return failureMessages == null ? null : String.join(",", failureMessages);
    }


    // Issue Map List<OrderItem> to List<OrderItemEntity>
    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "product.id.id", target = "productId")
    @Mapping(source = "price.amount", target = "price")
    @Mapping(source = "subTotal.amount", target = "subTotal")
    OrderItemEntity orderItemToOrderItemEntity(OrderItem orderItem);

    @Mapping(target = "id.id", source = "id")
    @Mapping(target = "customerId.id", source = "customerId")
    @Mapping(target = "businessId.id", source = "businessId")
    @Mapping(target = "price.amount", source = "price")
    @Mapping(target = "trackingId.id", source = "trackingId")
    @Mapping(target = "failureMessages", source = "failureMessages", qualifiedByName = "mapFailureMessagesToList")
    Order orderEntityToOrder(OrderEntity orderEntity);

    // Issue Map List<OrderItemEntity> to List<OrderItem>
    @Mapping(target = "id.id", source = "id")
    @Mapping(target = "product.id.id", source = "productId")
    @Mapping(target = "price.amount", source = "price")
    @Mapping(target = "subTotal.amount", source = "subTotal")
    OrderItem orderItemEntityToOrderItem(OrderItemEntity orderItemEntity);

    @Named("mapFailureMessagesToList")
    default List<String> mapFailureMessagesToList(String failureMessages) {
        return failureMessages == null ? new ArrayList<>() : Arrays.stream(failureMessages.split(",")).toList();
    }
}
