package kh.gov.mptc.koklim.ecommerce.order.persistence.mapper;

import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.CustomerId;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Customer;
import kh.gov.mptc.koklim.ecommerce.order.persistence.entity.CustomerEntity;

import java.util.UUID;

@org.mapstruct.Mapper(componentModel = "spring")
public interface CustomerPersistenceMapper {
    Customer customerEntityToCustomer(CustomerEntity customerEntity);

    default CustomerId toCustomerId(UUID id) {
        return id == null ? null : new CustomerId(id);
    }
}
