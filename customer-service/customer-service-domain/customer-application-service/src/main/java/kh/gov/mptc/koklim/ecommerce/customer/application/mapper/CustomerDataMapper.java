package kh.gov.mptc.koklim.ecommerce.customer.application.mapper;

import kh.gov.mptc.koklim.ecommerce.customer.application.dto.CreateCustomerCommand;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.Email;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.PhoneNumber;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CustomerDataMapper {

    // id and status are not set here: the domain sets them in initiateCustomer()
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "loyaltyTier", ignore = true)
    Customer createCustomerCommandToCustomer(CreateCustomerCommand createCustomerCommand);

    default Email toEmail(String email) {
        return email == null ? null : new Email(email);
    }

    default PhoneNumber toPhoneNumber(String phoneNumber) {
        return phoneNumber == null ? null : new PhoneNumber(phoneNumber);
    }

}
