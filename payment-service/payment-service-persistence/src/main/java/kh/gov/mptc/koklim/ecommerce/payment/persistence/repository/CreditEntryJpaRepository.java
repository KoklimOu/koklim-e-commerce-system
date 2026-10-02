package kh.gov.mptc.koklim.ecommerce.payment.persistence.repository;

import kh.gov.mptc.koklim.ecommerce.payment.persistence.entity.CreditEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CreditEntryJpaRepository extends JpaRepository<CreditEntryEntity, UUID> {
  Optional<CreditEntryEntity> findByCustomerId(UUID customerId);
}
