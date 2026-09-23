package kh.gov.mptc.koklim.ecommerce.order.persistence.adapter;

import kh.gov.mptc.koklim.ecommerce.order.application.port.output.CustomerRepository;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Customer;
import kh.gov.mptc.koklim.ecommerce.order.persistence.entity.CustomerEntity;
import kh.gov.mptc.koklim.ecommerce.order.persistence.mapper.OrderPersistenceMapper;
import kh.gov.mptc.koklim.ecommerce.order.persistence.repository.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {
    private final CustomerJpaRepository customerJpaRepository;
    private final OrderPersistenceMapper orderPersistenceMapper;

    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return customerJpaRepository.findById(customerId).map(orderPersistenceMapper::customerEntityToCustomer);
    }
}
