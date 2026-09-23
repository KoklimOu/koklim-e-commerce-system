package kh.gov.mptc.koklim.ecommerce.order.persistence.mapper;

import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.CustomerId;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Customer;
import kh.gov.mptc.koklim.ecommerce.order.persistence.entity.CustomerEntity;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OrderPersistenceMapper {


    Customer customerEntityToCustomer(CustomerEntity customerEntity);

    default CustomerId toCustomerId(UUID id) {
        return id == null ? null : new CustomerId(id);
    }
}
