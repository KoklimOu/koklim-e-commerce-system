package kh.gov.mptc.koklim.ecommerce.payment.persistence.mapper;

import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.Payment;
import kh.gov.mptc.koklim.ecommerce.payment.persistence.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentPersistenceMapper {
  @Mapping(source = "id.id", target = "id")
  @Mapping(source = "customerId.id", target = "customerId")
  @Mapping(source = "orderId.id", target = "orderId")
  @Mapping(source = "price.amount", target = "price")
  PaymentEntity paymentToPaymentEntity(Payment payment);

  @Mapping(target = "id.id", source = "id")
  @Mapping(target = "customerId.id", source = "customerId")
  @Mapping(target = "orderId.id", source = "orderId")
  @Mapping(target = "price.amount", source = "price")
  Payment paymentEntityToPayment(PaymentEntity paymentEntity);
}
