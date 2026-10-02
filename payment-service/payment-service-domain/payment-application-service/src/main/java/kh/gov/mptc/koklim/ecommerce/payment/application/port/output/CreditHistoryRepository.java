package kh.gov.mptc.koklim.ecommerce.payment.application.port.output;

import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.CreditHistory;

public interface CreditHistoryRepository {
    CreditHistory save(CreditHistory creditHistory);
}
