package kh.gov.mptc.koklim.ecommerce.user.mapper;

import kh.gov.mptc.koklim.ecommerce.user.dto.UserCreateRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.UserResponse;
import kh.gov.mptc.koklim.ecommerce.user.dto.UserUpdateRequest;
import kh.gov.mptc.koklim.ecommerce.user.entity.UserEntity;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserEntity toEntity(UserCreateRequest userCreateRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntity(UserUpdateRequest userUpdateRequest, @MappingTarget UserEntity userEntity);

    @Mapping(source = "userEntity.id", target = "id")
    @Mapping(source = "keycloakUserRepresentation.lastName",  target = "familyName")
    @Mapping(source = "keycloakUserRepresentation.firstName", target = "givenName")
    UserResponse toResponse(UserEntity userEntity, UserRepresentation keycloakUserRepresentation);

    // Keycloak calls them firstName / lastName
    @Mapping(source = "familyName", target = "firstName")
    @Mapping(source = "givenName", target = "lastName")
    UserRepresentation toRepresentation(UserCreateRequest userCreateRequest);

}
