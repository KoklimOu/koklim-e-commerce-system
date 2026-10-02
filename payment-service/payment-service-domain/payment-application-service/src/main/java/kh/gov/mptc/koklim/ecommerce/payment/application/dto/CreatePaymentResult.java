package kh.gov.mptc.koklim.ecommerce.payment.application.dto;

import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.PaymentStatus;

import java.util.UUID;

public record CreatePaymentResult(
    UUID paymentId,
    PaymentStatus paymentStatus
) {
}
