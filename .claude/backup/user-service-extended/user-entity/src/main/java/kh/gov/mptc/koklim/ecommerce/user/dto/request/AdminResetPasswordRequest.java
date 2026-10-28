package kh.gov.mptc.koklim.ecommerce.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdminResetPasswordRequest(
        @NotBlank @Size(min = 8, max = 100)
        String password,
        Boolean temporary      // null = true (user must change it at next login)
) {
}
