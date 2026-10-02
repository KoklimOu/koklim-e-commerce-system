package kh.gov.mptc.koklim.ecommerce.payment.domain.core.exception;

import kh.gov.mptc.koklim.ecommerce.common.domain.exception.DomainException;

public class PaymentDomainException extends DomainException {
  public PaymentDomainException(String message) {
    super(message);
  }

  public PaymentDomainException(String message, Throwable cause) {
    super(message, cause);
  }
}
