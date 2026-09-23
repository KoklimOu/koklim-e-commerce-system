package kh.gov.mptc.koklim.ecommerce.order.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateOrderCommand(
        UUID customerId,
        UUID businessId,
        OrderAddressCommand orderAddress,
        BigDecimal price,
        List<OrderItemCommand> items
) {

}
