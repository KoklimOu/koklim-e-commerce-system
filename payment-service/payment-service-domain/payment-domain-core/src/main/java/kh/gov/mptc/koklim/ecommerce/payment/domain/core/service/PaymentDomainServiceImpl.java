package kh.gov.mptc.koklim.ecommerce.payment.domain.core.service;

import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.CreditHistoryId;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.PaymentStatus;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.TransactionType;
import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.CreditEntry;
import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.CreditHistory;
import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.Payment;

import java.util.UUID;

public class PaymentDomainServiceImpl implements PaymentDomainService {
  @Override
  public CreditHistory validateAndInitiatePayment(Payment payment, CreditEntry creditEntry) {
    // 1. Payment logic
    payment.validatePayment();
    payment.initializePayment();

    // 2. CreditEntry logic → ដកលុយពី credit របស់ customer
    creditEntry.subtractCreditAmount(payment.getPrice());

    // 3. Payment success
    payment.updateStatus(PaymentStatus.COMPLETED);

    // 4. CreditHistory → កត់ត្រាថាបានដកលុយ (DEBIT)
    return CreditHistory.builder()
            .id(new CreditHistoryId(UUID.randomUUID()))
            .customerId(payment.getCustomerId())
            .amount(payment.getPrice())
            .transactionType(TransactionType.DEBIT)
            .build();
  }

  @Override
  public void updatePaymentStatus(Payment payment, PaymentStatus newPaymentStatus) {
    payment.updateStatus(newPaymentStatus);
  }
}
