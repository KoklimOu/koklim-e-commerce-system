package kh.gov.mptc.koklim.ecommerce.user.dto;

import java.util.Date;

public record UserResponse(
        String id,
        String familyName,
        String givenName,
        String gender,
        Date birthday,
        String mobilePhone,
        String address
) {
}
