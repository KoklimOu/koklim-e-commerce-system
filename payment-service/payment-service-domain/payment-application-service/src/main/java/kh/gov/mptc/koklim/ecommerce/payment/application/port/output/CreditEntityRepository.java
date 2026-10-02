package kh.gov.mptc.koklim.ecommerce.payment.application.port.output;

import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.CustomerId;
import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.CreditEntry;

public interface CreditEntityRepository  {
    CreditEntry findByCustomerId(CustomerId customerId);

    CreditEntry save(CreditEntry creditEntry);
}
