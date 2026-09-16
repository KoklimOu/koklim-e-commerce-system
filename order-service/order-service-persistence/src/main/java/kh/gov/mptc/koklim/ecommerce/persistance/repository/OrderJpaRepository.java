package kh.gov.mptc.koklim.ecommerce.persistance.repository;

import kh.gov.mptc.koklim.ecommerce.persistance.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {
    Optional<OrderEntity> findByTrackingId(UUID trackingId);
}
