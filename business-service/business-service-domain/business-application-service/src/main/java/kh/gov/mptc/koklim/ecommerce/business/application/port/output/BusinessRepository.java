package kh.gov.mptc.koklim.ecommerce.business.application.port.output;

import kh.gov.mptc.koklim.ecommerce.business.domain.core.entity.Business;

import java.util.Optional;

public interface BusinessRepository {

    Optional<Business> findBusinessInformation(Business business);

}
