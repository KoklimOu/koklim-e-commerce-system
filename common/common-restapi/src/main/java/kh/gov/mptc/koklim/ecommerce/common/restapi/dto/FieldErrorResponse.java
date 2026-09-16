package kh.gov.mptc.koklim.ecommerce.common.restapi.dto;

import lombok.Builder;

public record FieldErrorResponse(
        String field,
        String reason,
        String code
) {
}
