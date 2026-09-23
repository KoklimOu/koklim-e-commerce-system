package kh.gov.mptc.koklim.ecommerce.order.application.usecase;

import kh.gov.mptc.koklim.ecommerce.order.application.dto.CreateOrderCommand;
import kh.gov.mptc.koklim.ecommerce.order.application.dto.CreateOrderResult;
import kh.gov.mptc.koklim.ecommerce.order.application.port.input.CreateOrderPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

// there is the bean store?
@Component
@Slf4j
public class CreateOrderUseCase implements CreateOrderPort {

    @Override
    public CreateOrderResult execute(CreateOrderCommand createOrderCommand) {
        //Validate Customer
        //Validate Business
        log.info("Create Order Request: {}", createOrderCommand);
        return new CreateOrderResult(UUID.randomUUID());
    }
}
