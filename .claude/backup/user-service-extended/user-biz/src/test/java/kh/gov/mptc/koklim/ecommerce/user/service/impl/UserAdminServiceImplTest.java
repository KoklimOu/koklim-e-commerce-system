package kh.gov.mptc.koklim.ecommerce.user.service.impl;

import kh.gov.mptc.koklim.ecommerce.user.dto.request.AdminCreateUserRequest;
import kh.gov.mptc.koklim.ecommerce.user.dto.response.AdminUserResponse;
import kh.gov.mptc.koklim.ecommerce.user.config.KeycloakAdminClient;
import kh.gov.mptc.koklim.ecommerce.user.config.KeycloakUser;
import kh.gov.mptc.koklim.ecommerce.user.repository.UserProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAdminServiceImplTest {

    @Mock KeycloakAdminClient keycloak;
    @Mock UserProfileRepository userProfileRepository;
    @InjectMocks UserAdminServiceImpl service;

    private final UUID id = UUID.randomUUID();

    private void stubKeycloakUser(List<String> roles) {
        when(keycloak.getUser(id)).thenReturn(new KeycloakUser(id, "koklim", "koklim@example.com", "Kok", "Lim", true));
        when(keycloak.getRealmRoles(id)).thenReturn(roles);
    }

    @Test
    void create_defaultsToCustomerRole_andReturnsKeycloakData() {
        AdminCreateUserRequest request = AdminCreateUserRequest.builder()
                .username("koklim").email("koklim@example.com").firstName("Kok").lastName("Lim")
                .temporaryPassword("Passw0rd!").build();
        when(keycloak.createUser(any(), eq(List.of("CUSTOMER")))).thenReturn(id);
        stubKeycloakUser(List.of("CUSTOMER"));

        AdminUserResponse response = service.create(request);

        assertThat(response.username()).isEqualTo("koklim");
        assertThat(response.roles()).containsExactly("CUSTOMER");
        assertThat(response.displayName()).isNull();   // profile is created at the user's first /me
    }

    @Test
    void setRoles_addsMissingAndRemovesExtra() {
        // first call reads the current roles; the final getById reads them again
        when(keycloak.getRealmRoles(id)).thenReturn(List.of("CUSTOMER"), List.of("ADMIN"));
        when(keycloak.getUser(id)).thenReturn(new KeycloakUser(id, "koklim", "koklim@example.com", "Kok", "Lim", true));

        service.setRoles(id, List.of("ADMIN"));

        verify(keycloak).addRealmRoles(id, List.of("ADMIN"));
        verify(keycloak).removeRealmRoles(id, List.of("CUSTOMER"));
    }

    @Test
    void setRoles_doesNothing_whenRolesAlreadyMatch() {
        stubKeycloakUser(List.of("ADMIN"));

        service.setRoles(id, List.of("ADMIN"));

        verify(keycloak, never()).addRealmRoles(any(), any());
        verify(keycloak, never()).removeRealmRoles(any(), any());
    }

    @Test
    void delete_removesKeycloakAccount_thenProfile() {
        service.delete(id);

        var order = inOrder(keycloak, userProfileRepository);
        order.verify(keycloak).deleteUser(id);
        order.verify(userProfileRepository).deleteById(id);
    }
}
