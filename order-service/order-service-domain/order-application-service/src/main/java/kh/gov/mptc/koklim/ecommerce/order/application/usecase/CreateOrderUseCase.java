package kh.gov.mptc.koklim.ecommerce.order.application.usecase;

import kh.gov.mptc.koklim.ecommerce.order.application.dto.CreateOrderCommand;
import kh.gov.mptc.koklim.ecommerce.order.application.dto.CreateOrderResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

// there is the bean store?
@Component
@Slf4j
public class CreateOrderUseCase {
    public CreateOrderResult execute(CreateOrderCommand createOrderCommand) {
        //Validate Customer
        //Validate Business
        log.info("Create Order Request: {}", createOrderCommand);
        return new CreateOrderResult(UUID.randomUUID());
    }
}
