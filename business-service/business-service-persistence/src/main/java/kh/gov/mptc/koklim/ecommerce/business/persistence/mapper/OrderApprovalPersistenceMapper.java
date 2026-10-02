package kh.gov.mptc.koklim.ecommerce.business.persistence.mapper;

import kh.gov.mptc.koklim.ecommerce.business.domain.core.entity.OrderApproval;
import kh.gov.mptc.koklim.ecommerce.business.persistence.entity.OrderApprovalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderApprovalPersistenceMapper {

    @Mapping(source = "id.id", target = "id")
    @Mapping(source = "businessId.id", target = "businessId")
    @Mapping(source = "orderId.id", target = "orderId")
    @Mapping(source = "approvalStatus", target = "status")
    OrderApprovalEntity orderApprovalToOrderApprovalEntity(OrderApproval orderApproval);

    @Mapping(target = "id.id", source = "id")
    @Mapping(target = "businessId.id", source = "businessId")
    @Mapping(target = "orderId.id", source = "orderId")
    @Mapping(target = "approvalStatus", source = "status")
    OrderApproval orderApprovalEntityToOrderApproval(OrderApprovalEntity orderApprovalEntity);

}
