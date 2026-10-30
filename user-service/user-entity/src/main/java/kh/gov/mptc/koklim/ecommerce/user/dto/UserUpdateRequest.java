package kh.gov.mptc.koklim.ecommerce.user.dto;

public record UserUpdateRequest(
        String familyName,
        String givenName,
        String gender,
        String address,
        String birthday,
        String mobilePhone
) {
}
