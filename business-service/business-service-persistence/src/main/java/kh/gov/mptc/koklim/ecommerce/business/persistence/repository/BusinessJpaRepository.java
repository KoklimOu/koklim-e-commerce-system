package kh.gov.mptc.koklim.ecommerce.business.persistence.repository;

import kh.gov.mptc.koklim.ecommerce.business.persistence.entity.BusinessEntity;
import kh.gov.mptc.koklim.ecommerce.business.persistence.entity.BusinessIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BusinessJpaRepository extends JpaRepository<BusinessEntity, BusinessIdEntity> {

    List<BusinessEntity> findByBusinessIdAndProductIdIn(
            UUID businessId,
            List<UUID> productIds
    );
}
