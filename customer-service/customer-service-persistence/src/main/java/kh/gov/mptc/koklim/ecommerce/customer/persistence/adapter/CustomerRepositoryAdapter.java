package kh.gov.mptc.koklim.ecommerce.customer.persistence.adapter;

import kh.gov.mptc.koklim.ecommerce.customer.application.port.output.CustomerRepository;
import kh.gov.mptc.koklim.ecommerce.customer.persistence.entity.CustomerEntity;
import kh.gov.mptc.koklim.ecommerce.customer.persistence.mapper.CustomerPersistenceMapper;
import kh.gov.mptc.koklim.ecommerce.customer.persistence.repository.CustomerJpaRepository;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.CustomerId;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {
    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerPersistenceMapper customerPersistenceMapper;

    @Override
    public Customer save(Customer customer) {
        CustomerEntity savedEntity = customerJpaRepository.save(
                customerPersistenceMapper.customerToCustomerEntity(customer));
        return customerPersistenceMapper.customerEntityToCustomer(savedEntity);
    }

    @Override
    public Optional<Customer> findById(CustomerId customerId) {
        return customerJpaRepository.findById(customerId.id())
                .map(customerPersistenceMapper::customerEntityToCustomer);
    }

    @Override
    public boolean existsByUsername(String username) {
        return customerJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerJpaRepository.existsByEmail(email);
    }
}
