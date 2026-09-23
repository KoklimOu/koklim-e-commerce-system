package kh.gov.mptc.koklim.ecommerce.order.application.port.output;

import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Optional<Customer> findCustomer(UUID customerId);
}
