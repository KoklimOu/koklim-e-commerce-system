package kh.gov.mptc.koklim.ecommerce.user.mapper;

import kh.gov.mptc.koklim.ecommerce.user.dto.request.ProfileUpdateRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.response.UserProfileResponse;
import kh.gov.mptc.koklim.ecommerce.user.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    UserProfileResponse toResponse(UserProfile profile);

    // Copies the editable fields onto the managed entity. id/username come from Keycloak, never from the client.
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ProfileUpdateRequest request, @MappingTarget UserProfile profile);
}
