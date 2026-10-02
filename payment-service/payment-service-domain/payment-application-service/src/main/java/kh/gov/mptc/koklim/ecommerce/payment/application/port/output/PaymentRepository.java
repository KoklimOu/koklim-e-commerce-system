package kh.gov.mptc.koklim.ecommerce.payment.application.port.output;

import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.Payment;

public interface PaymentRepository {
  Payment savePayment(Payment payment);
}
