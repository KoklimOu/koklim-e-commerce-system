package kh.gov.mptc.koklim.ecommerce.customer.application.port.output;


import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.CustomerId;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.entity.Customer;

import java.util.Optional;

public interface CustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> findById(CustomerId customerId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
