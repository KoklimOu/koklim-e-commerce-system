package kh.gov.mptc.koklim.ecommerce.payment.persistence.adapter;

import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.Payment;
import kh.gov.mptc.koklim.ecommerce.payment.application.port.output.PaymentRepository;
import kh.gov.mptc.koklim.ecommerce.payment.persistence.entity.PaymentEntity;
import kh.gov.mptc.koklim.ecommerce.payment.persistence.mapper.PaymentPersistenceMapper;
import kh.gov.mptc.koklim.ecommerce.payment.persistence.repository.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryAdapter implements PaymentRepository {
  private final PaymentJpaRepository paymentJpaRepository;
  private final PaymentPersistenceMapper paymentPersistenceMapper;

  @Override
  public Payment savePayment(Payment payment) {
    PaymentEntity paymentEntity = paymentPersistenceMapper.paymentToPaymentEntity(payment);
    PaymentEntity savedPaymentEntity = paymentJpaRepository.save(paymentEntity);
    return paymentPersistenceMapper.paymentEntityToPayment(savedPaymentEntity);
  }
}
