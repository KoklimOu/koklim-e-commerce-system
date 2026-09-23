package kh.gov.mptc.koklim.ecommerce.order.persistence.repository;

import kh.gov.mptc.koklim.ecommerce.order.application.port.output.BusinessRepository;
import kh.gov.mptc.koklim.ecommerce.order.persistence.entity.BusinessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BusinessJpaRepository extends JpaRepository<BusinessEntity, UUID> {
}
