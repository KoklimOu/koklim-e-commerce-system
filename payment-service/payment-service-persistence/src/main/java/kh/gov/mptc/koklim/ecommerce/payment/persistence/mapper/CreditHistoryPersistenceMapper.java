package kh.gov.mptc.koklim.ecommerce.payment.persistence.mapper;

import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.CreditHistory;
import kh.gov.mptc.koklim.ecommerce.payment.persistence.entity.CreditHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreditHistoryPersistenceMapper {
  @Mapping(source = "id.id", target = "id")
  @Mapping(source = "customerId.id", target = "customerId")
  @Mapping(source = "amount.amount", target = "amount")
  CreditHistoryEntity creditHistoryToCreditHistoryEntity(CreditHistory creditHistory);

  @Mapping(target = "id.id", source = "id")
  @Mapping(target = "customerId.id", source = "customerId")
  @Mapping(target = "amount.amount", source = "amount")
  CreditHistory creditHistoryEntityToCreditHistory(CreditHistoryEntity creditHistoryEntity);
}
