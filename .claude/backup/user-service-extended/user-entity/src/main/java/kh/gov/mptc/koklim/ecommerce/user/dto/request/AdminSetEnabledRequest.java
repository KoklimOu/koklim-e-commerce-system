package kh.gov.mptc.koklim.ecommerce.user.dto.request;

import jakarta.validation.constraints.NotNull;

public record AdminSetEnabledRequest(@NotNull Boolean enabled) {
}
