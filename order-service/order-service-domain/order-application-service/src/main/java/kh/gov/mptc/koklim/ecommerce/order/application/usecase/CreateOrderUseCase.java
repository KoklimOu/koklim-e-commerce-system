package kh.gov.mptc.koklim.ecommerce.order.application.usecase;

import kh.gov.mptc.koklim.ecommerce.order.application.dto.CreateOrderCommand;
import kh.gov.mptc.koklim.ecommerce.order.application.dto.CreateOrderResult;
import kh.gov.mptc.koklim.ecommerce.order.application.mapper.OrderDomainMapper;
import kh.gov.mptc.koklim.ecommerce.order.application.port.input.CreateOrderPort;
import kh.gov.mptc.koklim.ecommerce.order.application.port.output.BusinessRepository;
import kh.gov.mptc.koklim.ecommerce.order.application.port.output.CustomerRepository;
import kh.gov.mptc.koklim.ecommerce.order.application.port.output.OrderRepository;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Business;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Order;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.event.OrderCreatedEvent;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.exception.OrderDomainException;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.service.OrderDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

// there is the bean store?
@Component
@Slf4j
@RequiredArgsConstructor
public class CreateOrderUseCase implements CreateOrderPort {

    private final CustomerRepository customerRepository;
    private final BusinessRepository businessRepository;
    private final OrderRepository orderRepository;

    private final OrderDomainMapper orderDomainMapper;
    private final OrderDomainService orderDomainService;

    @Override
    public CreateOrderResult execute(CreateOrderCommand createOrderCommand) {
        customerRepository.findCustomer(createOrderCommand.customerId())
                .orElseThrow(() -> new OrderDomainException(
                        "Customer not found: " + createOrderCommand.customerId()));

        Business business = businessRepository.findBusiness(createOrderCommand.businessId())
                .orElseThrow(() -> new OrderDomainException(
                        "Business not found: " + createOrderCommand.businessId()));

        Order order = orderDomainMapper.createdOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, business);
        log.info("Order createdEvent: {}", orderCreatedEvent.getOrder().getId());

        // save order to database
        Order saveOrder = orderRepository.saveOrder(orderCreatedEvent.getOrder());
        if (saveOrder == null) {
            throw new OrderDomainException("Order not saved");
        }

        return new CreateOrderResult(saveOrder.getId().id());
    }
}
