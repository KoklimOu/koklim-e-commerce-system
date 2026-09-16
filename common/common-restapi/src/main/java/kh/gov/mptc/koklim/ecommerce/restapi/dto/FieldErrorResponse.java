package kh.gov.mptc.koklim.ecommerce.restapi.dto;

import lombok.Builder;

public record FieldErrorResponse(
        String field,
        String message,
        String code
) {
}
