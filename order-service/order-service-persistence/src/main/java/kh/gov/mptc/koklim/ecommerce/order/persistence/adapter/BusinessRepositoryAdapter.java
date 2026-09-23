package kh.gov.mptc.koklim.ecommerce.order.persistence.adapter;

import kh.gov.mptc.koklim.ecommerce.order.application.port.output.BusinessRepository;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Business;
import kh.gov.mptc.koklim.ecommerce.order.persistence.entity.BusinessEntity;
import kh.gov.mptc.koklim.ecommerce.order.persistence.mapper.BusinessPersistenceMapper;
import kh.gov.mptc.koklim.ecommerce.order.persistence.mapper.OrderPersistenceMapper;
import kh.gov.mptc.koklim.ecommerce.order.persistence.repository.BusinessJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BusinessRepositoryAdapter implements BusinessRepository {

    private final BusinessJpaRepository businessJpaRepository;
    private final BusinessPersistenceMapper businessPersistenceMapper;

    @Override
    public Optional<Business> findBusiness(UUID businessId) {
        List<BusinessEntity> businessEntities = businessJpaRepository.findByBusinessId(businessId);
        if (businessEntities.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(businessPersistenceMapper.businessEntitiesToBusiness(businessEntities));
    }
}
