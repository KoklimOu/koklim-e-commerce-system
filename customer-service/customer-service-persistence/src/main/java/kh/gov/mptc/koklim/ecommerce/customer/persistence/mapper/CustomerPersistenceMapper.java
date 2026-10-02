package kh.gov.mptc.koklim.ecommerce.customer.persistence.mapper;

import kh.gov.mptc.koklim.ecommerce.customer.persistence.entity.CustomerEntity;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.PhoneNumber;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CustomerPersistenceMapper {
    @Mapping(source="id.id", target = "id")
    @Mapping(source="email.value", target = "email")
    @Mapping(source="phoneNumber.number", target = "phoneNumber")
    CustomerEntity customerToCustomerEntity(Customer customer);


    @Mapping(source="id", target = "id.id")
    @Mapping(source="email", target = "email.value")
    @Mapping(source = "phoneNumber", target = "phoneNumber", qualifiedByName = "toPhoneNumber")
    Customer customerEntityToCustomer(CustomerEntity customerEntity);

    @Named("toPhoneNumber")
    default PhoneNumber toPhoneNumber(String phoneNumber) {
        return phoneNumber == null ? null : new PhoneNumber(phoneNumber);
    }
}
