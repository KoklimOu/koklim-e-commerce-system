package kh.gov.mptc.koklim.ecommerce.order.application.port.output;

import kh.gov.mptc.koklim.ecommerce.order.domain.core.entity.Business;

import java.util.Optional;
import java.util.UUID;


public interface BusinessRepository {
    //TODO: define the correct login
    Optional<Business> findBusiness(UUID businessId);
}
