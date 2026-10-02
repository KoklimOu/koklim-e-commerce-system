package kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity;

import kh.gov.mptc.koklim.ecommerce.common.domain.entity.AggregateRoot;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.CustomerId;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.Money;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.OrderId;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.PaymentId;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.PaymentStatus;
import kh.gov.mptc.koklim.ecommerce.payment.domain.core.exception.PaymentDomainException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

public class Payment extends AggregateRoot<PaymentId> {
  private final OrderId orderId;
  private final CustomerId customerId;
  private final Money price;

  private PaymentStatus paymentStatus;
  private ZonedDateTime createdAt;

  private Payment(Builder builder) {
    super.setId(builder.id);
    orderId = builder.orderId;
    customerId = builder.customerId;
    price = builder.price;
    paymentStatus = builder.paymentStatus;
    createdAt = builder.createdAt;
  }

  public static Builder builder() {
    return new Builder();
  }

  public void validatePayment() {
    if (orderId == null || customerId == null) {
      throw new PaymentDomainException("Order id and customer id must be present for a payment");
    }

    if (price == null || !price.isGreaterThanZero()) {
      throw new PaymentDomainException("Payment price must be greater than zero");
    }
  }

  public void initializePayment() {
    if (super.getId() != null || paymentStatus != null) {
      throw new PaymentDomainException("Payment is not in correct status for initialization");
    }

    setId(new PaymentId(UUID.randomUUID()));
    createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
    paymentStatus = PaymentStatus.PENDING;
  }

  public void updateStatus(PaymentStatus newPaymentStatus) {
    if (paymentStatus != PaymentStatus.PENDING) {
      throw new PaymentDomainException("Payment is not in correct state for status update");
    }

    paymentStatus = newPaymentStatus;
  }

  public OrderId getOrderId() {
    return orderId;
  }

  public CustomerId getCustomerId() {
    return customerId;
  }

  public Money getPrice() {
    return price;
  }

  public PaymentStatus getPaymentStatus() {
    return paymentStatus;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public static final class Builder {
    private PaymentId id;
    private OrderId orderId;
    private CustomerId customerId;
    private Money price;
    private PaymentStatus paymentStatus;
    private ZonedDateTime createdAt;

    private Builder() {
    }

    public Builder id(PaymentId val) {
      id = val;
      return this;
    }

    public Builder orderId(OrderId val) {
      orderId = val;
      return this;
    }

    public Builder customerId(CustomerId val) {
      customerId = val;
      return this;
    }

    public Builder price(Money val) {
      price = val;
      return this;
    }

    public Builder paymentStatus(PaymentStatus val) {
      paymentStatus = val;
      return this;
    }

    public Builder createdAt(ZonedDateTime val) {
      createdAt = val;
      return this;
    }

    public Payment build() {
      return new Payment(this);
    }
  }
}
