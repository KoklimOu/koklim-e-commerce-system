package kh.gov.mptc.koklim.ecommerce.user.mapper;

import kh.gov.mptc.koklim.ecommerce.user.dto.UserCreateRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.UserResponse;
import kh.gov.mptc.koklim.ecommerce.user.entity.UserEntity;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserEntity toEntity(UserCreateRequest userCreateRequest);

    UserResponse toResponse(UserEntity userEntity);

    // Keycloak calls them firstName / lastName
    @Mapping(source = "familyName", target = "firstName")
    @Mapping(source = "givenName", target = "lastName")
    UserRepresentation toRepresentation(UserCreateRequest userCreateRequest);
}
