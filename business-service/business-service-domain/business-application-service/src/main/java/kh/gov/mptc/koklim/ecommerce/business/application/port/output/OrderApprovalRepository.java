package kh.gov.mptc.koklim.ecommerce.business.application.port.output;

import kh.gov.mptc.koklim.ecommerce.business.domain.core.entity.OrderApproval;

public interface OrderApprovalRepository {

    OrderApproval save(OrderApproval orderApproval);

}
