package kh.gov.mptc.koklim.ecommerce.payment.application.mapper;

import kh.gov.mptc.koklim.ecommerce.payment.application.dto.CreatePaymentCommand;
import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentDomainMapper {
  @Mapping(source = "orderId", target = "orderId.id")
  @Mapping(source = "customerId", target = "customerId.id")
  @Mapping(source = "price", target = "price.amount")
  Payment createPaymentCommandToPayment(CreatePaymentCommand createPaymentCommand);
}
