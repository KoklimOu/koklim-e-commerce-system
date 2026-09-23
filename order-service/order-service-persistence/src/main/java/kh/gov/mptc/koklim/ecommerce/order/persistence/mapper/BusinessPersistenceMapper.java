package kh.gov.mptc.koklim.ecommerce.order.persistence.mapper;

import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.BusinessId;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Business;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Product;
import kh.gov.mptc.koklim.ecommerce.order.persistence.entity.BusinessEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BusinessPersistenceMapper {

    @Mapping(source = "productId", target = "id.id")
    @Mapping(source = "price", target = "price.amount")
    Product businessEntityToProduct(BusinessEntity businessEntity);

    List<Product> businessEntitiesToProducts(List<BusinessEntity> businessEntities);

    // Every row carries the same business columns, so read them from the first row
    default Business businessEntitiesToBusiness(List<BusinessEntity> businessEntities) {
        BusinessEntity first = businessEntities.getFirst();
        return Business.builder()
                .id(new BusinessId(first.getBusinessId()))
                .status(Boolean.TRUE.equals(first.getBusinessActive()))
                .products(businessEntitiesToProducts(businessEntities))
                .build();
    }
}
