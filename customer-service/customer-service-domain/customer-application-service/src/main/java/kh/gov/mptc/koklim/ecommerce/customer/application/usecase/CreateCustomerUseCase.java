package kh.gov.mptc.koklim.ecommerce.customer.application.usecase;

import kh.gov.mptc.koklim.ecommerce.customer.application.dto.CreateCustomerCommand;
import kh.gov.mptc.koklim.ecommerce.customer.application.dto.CreateCustomerResult;
import kh.gov.mptc.koklim.ecommerce.customer.application.exception.CustomerAlreadyExistsException;
import kh.gov.mptc.koklim.ecommerce.customer.application.mapper.CustomerDataMapper;
import kh.gov.mptc.koklim.ecommerce.customer.application.port.output.CustomerRepository;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.entity.Customer;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.event.CustomerCreatedEvent;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.service.CustomerDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// Use case: register a new customer.
// Called by the REST API controller (POST /api/v1/customers).
// Flow: check duplicates -> build domain Customer -> domain validates & initiates -> save -> return new id
@Component
@Slf4j
@RequiredArgsConstructor
public class CreateCustomerUseCase {

    private final CustomerDomainService customerDomainService; // business rules (from customer-domain-core)
    private final CustomerRepository customerRepository;
    private final CustomerDataMapper customerDataMapper;

    @Transactional // everything below runs in one DB transaction: if anything fails, nothing is saved
    public CreateCustomerResult execute(CreateCustomerCommand createCustomerCommand) {
        log.info("Execute CreateCustomerUseCase : {}", createCustomerCommand);

        if (customerRepository.existsByUsername(createCustomerCommand.username())) {
            throw new CustomerAlreadyExistsException("Username already exists");
        }
        if (customerRepository.existsByEmail(createCustomerCommand.email())) {
            throw new CustomerAlreadyExistsException("Email already exists");
        }

        Customer customer = customerDataMapper.createCustomerCommandToCustomer(createCustomerCommand);

        CustomerCreatedEvent customerCreatedEvent = customerDomainService.validateAndInitiateCustomer(customer);

        Customer savedCustomer = customerRepository.save(customer);

        log.info("Customer created with id: {} at {}",
                savedCustomer.getId().id(), customerCreatedEvent.getCreatedAt());

        return new CreateCustomerResult(savedCustomer.getId().id());
    }
}
