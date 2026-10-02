package kh.gov.mptc.koklim.ecommerce.business.domain.core.service;

import kh.gov.mptc.koklim.ecommerce.business.domain.core.entity.Business;
import kh.gov.mptc.koklim.ecommerce.business.domain.core.event.OrderApprovalEvent;

import java.util.List;

public interface BusinessDomainService {
    OrderApprovalEvent validateOrder(Business business, List<String> failureMessages);
}
