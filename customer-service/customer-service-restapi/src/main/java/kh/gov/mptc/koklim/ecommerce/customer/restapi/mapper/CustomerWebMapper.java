package kh.gov.mptc.koklim.ecommerce.customer.restapi.mapper;

import kh.gov.mptc.koklim.ecommerce.customer.application.dto.CreateCustomerCommand;
import kh.gov.mptc.koklim.ecommerce.customer.application.dto.CreateCustomerResult;
import kh.gov.mptc.koklim.ecommerce.customer.application.dto.UpdateCustomerCommand;
import kh.gov.mptc.koklim.ecommerce.customer.application.dto.UpdateCustomerResult;
import kh.gov.mptc.koklim.ecommerce.customer.restapi.dto.CustomerCreateRequest;
import kh.gov.mptc.koklim.ecommerce.customer.restapi.dto.CustomerCreateResponse;
import kh.gov.mptc.koklim.ecommerce.customer.restapi.dto.CustomerUpdateRequest;
import kh.gov.mptc.koklim.ecommerce.customer.restapi.dto.CustomerUpdateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CustomerWebMapper {
    CreateCustomerCommand customerCreateRequestToCreateCustomerCommand(CustomerCreateRequest customerCreateRequest);
    CustomerCreateResponse createCustomerResultToCustomerCreateResponse(CreateCustomerResult createCustomerResult);

    // customerId comes from the path, the rest from the request body
    @Mapping(source = "customerId", target = "customerId")
    UpdateCustomerCommand customerUpdateRequestToUpdateCustomerCommand(UUID customerId, CustomerUpdateRequest customerUpdateRequest);
    CustomerUpdateResponse updateCustomerResultToCustomerUpdateResponse(UpdateCustomerResult updateCustomerResult);
}
