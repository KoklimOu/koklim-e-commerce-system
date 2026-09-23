package kh.gov.mptc.koklim.ecommerce.order.domain.core.service;

import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.ProductId;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Business;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Order;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Product;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.event.OrderCancelledEvent;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.event.OrderCreatedEvent;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.event.OrderPaidEvent;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.exception.OrderDomainException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrderDomainServiceImpl implements OrderDomainService {
    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Business business) {
        if (!business.isActive()){
            throw new OrderDomainException("Business is not active");
        }
        setOrderProductInformation(order, business);
        order.validateOrder();
        order.initializeOrder();
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    private void setOrderProductInformation(Order order, Business business) {
        Map<ProductId, Product> businessProducts = business.getProducts().stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        order.getItems().forEach(orderItem -> {
            Product product = orderItem.getProduct();
            Product businessProduct = businessProducts.get(product.getId());
            if (businessProduct == null) {
                throw new OrderDomainException("Product " + product.getId().id() + " is not sold by business " + business.getId().id());
            }
            product.updateWithConfirmedNameAndPrice(businessProduct.getName(), businessProduct.getPrice());
        });
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
    }
}
