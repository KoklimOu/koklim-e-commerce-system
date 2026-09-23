package kh.gov.mptc.koklim.ecommerce.order.persistence.repository;

import kh.gov.mptc.koklim.ecommerce.order.persistence.entity.BusinessEntity;
import kh.gov.mptc.koklim.ecommerce.order.persistence.entity.BusinessIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BusinessJpaRepository extends JpaRepository<BusinessEntity, BusinessIdEntity> {
    // one row per (business, product), so a business comes back as many rows
    List<BusinessEntity> findByBusinessId(UUID businessId);
}
