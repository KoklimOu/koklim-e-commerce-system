package kh.gov.mptc.koklim.ecommerce.customer.restapi.controller;


import kh.gov.mptc.koklim.ecommerce.customer.application.dto.CreateCustomerCommand;
import kh.gov.mptc.koklim.ecommerce.customer.application.dto.CreateCustomerResult;
import kh.gov.mptc.koklim.ecommerce.customer.application.dto.DeactivateCustomerCommand;
import kh.gov.mptc.koklim.ecommerce.customer.application.dto.UpdateCustomerCommand;
import kh.gov.mptc.koklim.ecommerce.customer.application.dto.UpdateCustomerResult;
import kh.gov.mptc.koklim.ecommerce.customer.application.usecase.CreateCustomerUseCase;
import kh.gov.mptc.koklim.ecommerce.customer.application.usecase.DeactivateCustomerUseCase;
import kh.gov.mptc.koklim.ecommerce.customer.application.usecase.UpdateCustomerUseCase;
import kh.gov.mptc.koklim.ecommerce.customer.restapi.dto.CustomerCreateRequest;
import kh.gov.mptc.koklim.ecommerce.customer.restapi.dto.CustomerCreateResponse;
import kh.gov.mptc.koklim.ecommerce.customer.restapi.dto.CustomerUpdateRequest;
import kh.gov.mptc.koklim.ecommerce.customer.restapi.dto.CustomerUpdateResponse;
import kh.gov.mptc.koklim.ecommerce.customer.restapi.mapper.CustomerWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerCommandController {

    private final CustomerWebMapper customerWebMapper;
    private final CreateCustomerUseCase createCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeactivateCustomerUseCase deactivateCustomerUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerCreateResponse createCustomer(@Valid @RequestBody CustomerCreateRequest customerCreateRequest){
        CreateCustomerCommand createCustomerCommand = customerWebMapper.customerCreateRequestToCreateCustomerCommand(customerCreateRequest);
        CreateCustomerResult createCustomerResult = createCustomerUseCase.execute(createCustomerCommand);
        return customerWebMapper.createCustomerResultToCustomerCreateResponse(createCustomerResult);
    }

    @PutMapping("/{customerId}")
    public CustomerUpdateResponse updateCustomer(@PathVariable UUID customerId,
                                                 @Valid @RequestBody CustomerUpdateRequest customerUpdateRequest){
        UpdateCustomerCommand updateCustomerCommand = customerWebMapper.customerUpdateRequestToUpdateCustomerCommand(customerId, customerUpdateRequest);
        UpdateCustomerResult updateCustomerResult = updateCustomerUseCase.execute(updateCustomerCommand);
        return customerWebMapper.updateCustomerResultToCustomerUpdateResponse(updateCustomerResult);
    }

    @PatchMapping("/{customerId}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateCustomer(@PathVariable UUID customerId){
        deactivateCustomerUseCase.execute(new DeactivateCustomerCommand(customerId));
    }

}
