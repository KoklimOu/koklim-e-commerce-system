package kh.gov.mptc.koklim.ecommerce.business.persistence.adapter;

import kh.gov.mptc.koklim.ecommerce.business.domain.core.entity.Business;
import kh.gov.mptc.koklim.ecommerce.business.application.port.output.BusinessRepository;
import kh.gov.mptc.koklim.ecommerce.business.persistence.entity.BusinessEntity;
import kh.gov.mptc.koklim.ecommerce.business.persistence.mapper.BusinessPersistenceMapper;
import kh.gov.mptc.koklim.ecommerce.business.persistence.repository.BusinessJpaRepository;
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
    public Optional<Business> findBusinessInformation(Business business) {
        List<UUID> businessProducts = businessPersistenceMapper.businessToBusinessProducts(business);

        List<BusinessEntity> businessEntities = businessJpaRepository.findByBusinessIdAndProductIdIn(
                business.getId().id(),
                businessProducts
        );

        return Optional.of(businessPersistenceMapper.businessEntityToBusiness(businessEntities));
    }
}
