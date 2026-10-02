package kh.gov.mptc.koklim.ecommerce.business.domain.core.event;


import kh.gov.mptc.koklim.ecommerce.business.domain.core.entity.OrderApproval;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.BusinessId;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderApprovedEvent extends OrderApprovalEvent {
    public OrderApprovedEvent(OrderApproval orderApproval, BusinessId businessId, List<String> failureMessages, ZonedDateTime createdAt) {
        super(orderApproval, businessId, failureMessages, createdAt);
    }
}
