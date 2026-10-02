package kh.gov.mptc.koklim.ecommerce.payment.persistence.repository;

import kh.gov.mptc.koklim.ecommerce.payment.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {
}
