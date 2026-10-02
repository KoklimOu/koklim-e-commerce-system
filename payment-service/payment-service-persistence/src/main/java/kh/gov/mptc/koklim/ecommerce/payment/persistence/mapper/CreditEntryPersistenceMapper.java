package kh.gov.mptc.koklim.ecommerce.payment.persistence.mapper;

import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.CreditEntry;
import kh.gov.mptc.koklim.ecommerce.payment.persistence.entity.CreditEntryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreditEntryPersistenceMapper {
  @Mapping(source = "id.id", target = "id")
  @Mapping(source = "customerId.id", target = "customerId")
  @Mapping(source = "totalCreditAmount.amount", target = "totalCreditAmount")
  CreditEntryEntity creditEntryToCreditEntryEntity(CreditEntry creditEntry);

  @Mapping(target = "id.id", source = "id")
  @Mapping(target = "customerId.id", source = "customerId")
  @Mapping(target = "totalCreditAmount.amount", source = "totalCreditAmount")
  CreditEntry creditEntryEntityToCreditEntry(CreditEntryEntity creditEntryEntity);
}
