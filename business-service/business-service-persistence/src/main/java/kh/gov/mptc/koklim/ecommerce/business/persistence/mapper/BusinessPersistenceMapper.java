package kh.gov.mptc.koklim.ecommerce.business.persistence.mapper;

import kh.gov.mptc.koklim.ecommerce.business.domain.core.entity.Business;
import kh.gov.mptc.koklim.ecommerce.business.domain.core.entity.OrderDetail;
import kh.gov.mptc.koklim.ecommerce.business.domain.core.entity.Product;
import kh.gov.mptc.koklim.ecommerce.business.persistence.entity.BusinessEntity;
import kh.gov.mptc.koklim.ecommerce.business.persistence.exception.BusinessPersistenceException;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.BusinessId;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.Money;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.ProductId;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface BusinessPersistenceMapper {

    default List<UUID> businessToBusinessProducts(Business business) {
        return business.getOrderDetail().getProducts().stream().map(product -> product.getId().id()).toList();
    }

    default Business businessEntityToBusiness(List<BusinessEntity> businessEntities) {
        BusinessEntity businessEntity = businessEntities.stream().findFirst().orElseThrow(() -> new BusinessPersistenceException("Business could not be found"));

        List<Product> products = businessEntities.stream().map(entity -> Product.builder()
                .id(new ProductId(entity.getProductId()))
                .name(entity.getProductName())
                .price(new Money(entity.getProductPrice()))
                .available(entity.getProductAvailable())
                .build()).toList();

        return Business.builder()
                .id(new BusinessId(businessEntity.getBusinessId()))
                .active(businessEntity.getBusinessActive())
                .orderDetail(OrderDetail.builder().products(products).build())
                .build();
    }

}
