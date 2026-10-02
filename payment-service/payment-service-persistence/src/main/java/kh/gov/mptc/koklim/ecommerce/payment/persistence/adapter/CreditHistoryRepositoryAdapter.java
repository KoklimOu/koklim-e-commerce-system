package kh.gov.mptc.koklim.ecommerce.payment.persistence.adapter;

import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.CreditHistory;
import kh.gov.mptc.koklim.ecommerce.payment.application.port.output.CreditHistoryRepository;
import kh.gov.mptc.koklim.ecommerce.payment.persistence.entity.CreditHistoryEntity;
import kh.gov.mptc.koklim.ecommerce.payment.persistence.mapper.CreditHistoryPersistenceMapper;
import kh.gov.mptc.koklim.ecommerce.payment.persistence.repository.CreditHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CreditHistoryRepositoryAdapter implements CreditHistoryRepository {
  private final CreditHistoryJpaRepository creditHistoryJpaRepository;
  private final CreditHistoryPersistenceMapper creditHistoryPersistenceMapper;

  @Override
  public CreditHistory save(CreditHistory creditHistory) {
    CreditHistoryEntity creditHistoryEntity =
        creditHistoryPersistenceMapper.creditHistoryToCreditHistoryEntity(creditHistory);
    CreditHistoryEntity savedCreditHistoryEntity = creditHistoryJpaRepository.save(creditHistoryEntity);
    return creditHistoryPersistenceMapper.creditHistoryEntityToCreditHistory(savedCreditHistoryEntity);
  }
}
