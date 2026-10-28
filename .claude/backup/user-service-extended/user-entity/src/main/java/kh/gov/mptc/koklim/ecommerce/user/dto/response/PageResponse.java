package kh.gov.mptc.koklim.ecommerce.user.dto.response;

import java.util.List;

// Stable JSON shape for paged results (instead of serializing Spring's Page directly).
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
