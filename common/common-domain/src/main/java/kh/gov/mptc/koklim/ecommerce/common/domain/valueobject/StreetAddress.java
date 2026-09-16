package kh.gov.mptc.koklim.ecommerce.common.domain.valueobject;

import java.util.UUID;

public record StreetAddress(UUID id, String street, String postalCode, String city) {
}
