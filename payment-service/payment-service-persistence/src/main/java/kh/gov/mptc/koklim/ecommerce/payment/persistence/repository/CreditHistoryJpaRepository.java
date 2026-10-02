package kh.gov.mptc.koklim.ecommerce.payment.persistence.repository;

import kh.gov.mptc.koklim.ecommerce.payment.persistence.entity.CreditHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CreditHistoryJpaRepository extends JpaRepository<CreditHistoryEntity, UUID> {
}
