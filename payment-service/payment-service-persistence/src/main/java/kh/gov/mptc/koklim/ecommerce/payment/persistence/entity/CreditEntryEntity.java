package kh.gov.mptc.koklim.ecommerce.payment.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "credit_entries")
public class CreditEntryEntity {
  @Id
  private UUID id;
  private UUID customerId;
  private BigDecimal totalCreditAmount;
}
