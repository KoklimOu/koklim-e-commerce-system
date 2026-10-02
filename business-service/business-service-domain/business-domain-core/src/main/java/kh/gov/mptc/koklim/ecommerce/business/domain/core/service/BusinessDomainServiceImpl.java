package kh.gov.mptc.koklim.ecommerce.business.domain.core.service;

import kh.gov.mptc.koklim.ecommerce.business.domain.core.entity.Business;
import kh.gov.mptc.koklim.ecommerce.business.domain.core.event.OrderApprovalEvent;
import kh.gov.mptc.koklim.ecommerce.business.domain.core.event.OrderApprovedEvent;
import kh.gov.mptc.koklim.ecommerce.business.domain.core.event.OrderRejectedEvent;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.OrderApprovalStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class BusinessDomainServiceImpl implements BusinessDomainService {

    @Override
    public OrderApprovalEvent validateOrder(Business business, List<String> failureMessages) {
        business.validateOrder(failureMessages);

        if (failureMessages.isEmpty()) {
            business.constructOrderApproval(OrderApprovalStatus.APPROVED);
            return new OrderApprovedEvent(business.getOrderApproval(), business.getId(),
                    failureMessages, ZonedDateTime.now(ZoneId.of("UTC")));
        }

        business.constructOrderApproval(OrderApprovalStatus.REJECTED);
        return new OrderRejectedEvent(business.getOrderApproval(), business.getId(),
                failureMessages, ZonedDateTime.now(ZoneId.of("UTC")));
    }
}
