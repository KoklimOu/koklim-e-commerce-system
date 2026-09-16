package kh.gov.mptc.koklim.ecommerce.restapi.dto;


import lombok.Builder;

@Builder
public record RestApiErrorResponse<T>(
        String message,
        String code,
        T details
) {
}
