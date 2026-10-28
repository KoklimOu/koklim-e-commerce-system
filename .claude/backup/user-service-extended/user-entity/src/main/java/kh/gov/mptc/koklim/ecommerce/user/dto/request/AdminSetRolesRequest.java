package kh.gov.mptc.koklim.ecommerce.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

// The user ends up with exactly these realm roles
public record AdminSetRolesRequest(@NotNull List<@NotBlank String> roles) {
}
