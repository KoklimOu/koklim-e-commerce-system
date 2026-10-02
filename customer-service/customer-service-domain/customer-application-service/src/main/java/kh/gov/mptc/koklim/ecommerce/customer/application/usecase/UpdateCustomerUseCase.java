package kh.gov.mptc.koklim.ecommerce.customer.application.usecase;

import kh.gov.mptc.koklim.ecommerce.customer.application.dto.UpdateCustomerCommand;
import kh.gov.mptc.koklim.ecommerce.customer.application.dto.UpdateCustomerResult;
import kh.gov.mptc.koklim.ecommerce.customer.application.exception.CustomerAlreadyExistsException;
import kh.gov.mptc.koklim.ecommerce.customer.application.exception.CustomerNotFoundException;
import kh.gov.mptc.koklim.ecommerce.customer.application.mapper.CustomerDataMapper;
import kh.gov.mptc.koklim.ecommerce.customer.application.port.output.CustomerRepository;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.CustomerId;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.Email;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.entity.Customer;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.event.CustomerUpdatedEvent;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.service.CustomerDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

// Use case: change an existing customer's name, email and phone number.
// Called by the REST API controller (PUT /api/v1/customers/{customerId}).
// Flow: load customer -> check new email -> domain updates -> save -> return id
@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateCustomerUseCase {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;
    private final CustomerDataMapper customerDataMapper;

    @Transactional
    public UpdateCustomerResult execute(UpdateCustomerCommand updateCustomerCommand) {
        log.info("Execute UpdateCustomerUseCase : {}", updateCustomerCommand);

        Customer customer = customerRepository.findById(new CustomerId(updateCustomerCommand.customerId()))
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found: " + updateCustomerCommand.customerId()));

        Email newEmail = new Email(updateCustomerCommand.email());
        if (!newEmail.equals(customer.getEmail()) && customerRepository.existsByEmail(newEmail.value())) {
            throw new CustomerAlreadyExistsException("Email already exists");
        }

        CustomerUpdatedEvent customerUpdatedEvent = customerDomainService.updateCustomer(customer,
                updateCustomerCommand.familyName(),
                updateCustomerCommand.givenName(),
                newEmail,
                customerDataMapper.toPhoneNumber(updateCustomerCommand.phoneNumber()));
        Customer savedCustomer = customerRepository.save(customer);

        log.info("Customer updated with id: {} at {}",
                savedCustomer.getId().id(), customerUpdatedEvent.getUpdatedAt());
        return new UpdateCustomerResult(savedCustomer.getId().id());
    }
}
