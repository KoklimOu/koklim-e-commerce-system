package kh.gov.mptc.koklim.ecommerce.order.application.port.input;

import kh.gov.mptc.koklim.ecommerce.order.application.dto.CreateOrderCommand;
import kh.gov.mptc.koklim.ecommerce.order.application.dto.CreateOrderResult;

public interface CreateOrderPort {
    CreateOrderResult execute(CreateOrderCommand createOrderCommand);
}
