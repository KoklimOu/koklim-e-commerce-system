package kh.gov.mptc.koklim.ecommerce.payment.persistence.entity;

import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class PaymentEntity {
  @Id
  private UUID id;
  private UUID customerId;
  private UUID orderId;
  private BigDecimal price;
  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;
  private ZonedDateTime createdAt;
}
