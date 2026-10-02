package kh.gov.mptc.koklim.ecommerce.payment.domain.core.service;

import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.PaymentStatus;
import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.CreditEntry;
import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.CreditHistory;
import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.Payment;

public interface PaymentDomainService {
  CreditHistory validateAndInitiatePayment(Payment payment, CreditEntry creditEntry);

  void updatePaymentStatus(Payment payment, PaymentStatus newPaymentStatus);
}
