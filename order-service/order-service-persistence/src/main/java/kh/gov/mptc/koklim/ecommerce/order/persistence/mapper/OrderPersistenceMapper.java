package kh.gov.mptc.koklim.ecommerce.order.persistence.mapper;

import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.CustomerId;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Customer;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Order;
import kh.gov.mptc.koklim.ecommerce.order.persistence.entity.CustomerEntity;
import kh.gov.mptc.koklim.ecommerce.order.persistence.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OrderPersistenceMapper {




//    @Mapping(target = "id", source = "id.value")
//    @Mapping(target = "customerId", source = "customerId.value")
//    @Mapping(target = "businessId", source = "businessId.value")
//    @Mapping(target = "price", source = "price.amount")
//    OrderEntity orderToOrderEntity(Order order);
//
//    Order orderEntityToOrder(OrderEntity orderEntity);
}
