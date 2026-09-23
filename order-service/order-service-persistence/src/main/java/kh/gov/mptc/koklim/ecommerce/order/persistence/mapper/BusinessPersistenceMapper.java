package kh.gov.mptc.koklim.ecommerce.order.persistence.mapper;

import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Business;
import kh.gov.mptc.koklim.ecommerce.order.persistence.entity.BusinessEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusinessPersistenceMapper {
    Business businessEntityToBusiness(BusinessEntity businessEntity);
}
