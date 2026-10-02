package kh.gov.mptc.koklim.ecommerce.payment.persistence.adapter;

import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.CustomerId;
import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.CreditEntry;
import kh.gov.mptc.koklim.ecommerce.payment.application.port.output.CreditEntityRepository;
import kh.gov.mptc.koklim.ecommerce.payment.persistence.entity.CreditEntryEntity;
import kh.gov.mptc.koklim.ecommerce.payment.persistence.mapper.CreditEntryPersistenceMapper;
import kh.gov.mptc.koklim.ecommerce.payment.persistence.repository.CreditEntryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CreditEntryRepositoryAdapter implements CreditEntityRepository {
  private final CreditEntryJpaRepository creditEntryJpaRepository;
  private final CreditEntryPersistenceMapper creditEntryPersistenceMapper;

  @Override
  public CreditEntry findByCustomerId(CustomerId customerId) {
    return creditEntryJpaRepository.findByCustomerId(customerId.id())
        .map(creditEntryPersistenceMapper::creditEntryEntityToCreditEntry)
        .orElse(null);
  }

  @Override
  public CreditEntry save(CreditEntry creditEntry) {
    CreditEntryEntity creditEntryEntity = creditEntryPersistenceMapper.creditEntryToCreditEntryEntity(creditEntry);
    CreditEntryEntity savedCreditEntryEntity = creditEntryJpaRepository.save(creditEntryEntity);
    return creditEntryPersistenceMapper.creditEntryEntityToCreditEntry(savedCreditEntryEntity);
  }
}
