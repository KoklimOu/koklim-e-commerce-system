package kh.gov.mptc.koklim.ecommerce.user.service.impl;

import jakarta.ws.rs.core.Response;
import kh.gov.mptc.koklim.ecommerce.user.exception.KeycloakException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeycloakUserServiceImplTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS) Keycloak keycloak;
    @InjectMocks KeycloakUserServiceImpl keycloakService;

    @BeforeEach
    void setRealm() {
        ReflectionTestUtils.setField(keycloakService, "realm", "ecommerce");
    }

    @Test
    void createKeycloakUser_passes_whenKeycloakSays201() {
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(201);
        when(keycloak.realm("ecommerce").users().create(any())).thenReturn(response);

        assertThatCode(() -> keycloakService.createKeycloakUser(new UserRepresentation())).doesNotThrowAnyException();
    }

    @Test
    void createKeycloakUser_keepsKeycloakStatus_whenItRefuses() {
        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(409);
        when(keycloak.realm("ecommerce").users().create(any())).thenReturn(response);

        assertThatThrownBy(() -> keycloakService.createKeycloakUser(new UserRepresentation()))
                .isInstanceOfSatisfying(KeycloakException.class,
                        e -> assertThat(e.getStatus()).isEqualTo(HttpStatus.CONFLICT));
    }

    @Test
    void getKeycloakUserByUsername_returnsFirstMatch() {
        UserRepresentation found = new UserRepresentation();
        when(keycloak.realm("ecommerce").users().search("koklim", true)).thenReturn(List.of(found));

        assertThat(keycloakService.getKeycloakUserByUsername("koklim")).isSameAs(found);
    }

    @Test
    void getKeycloakUserByUsername_throwsNotFound_whenNoMatch() {
        when(keycloak.realm("ecommerce").users().search("koklim", true)).thenReturn(List.of());

        assertThatThrownBy(() -> keycloakService.getKeycloakUserByUsername("koklim"))
                .isInstanceOfSatisfying(KeycloakException.class,
                        e -> assertThat(e.getStatus()).isEqualTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void deleteKeycloakUserQuietly_deletesById() {
        Response response = mock(Response.class);
        when(keycloak.realm("ecommerce").users().delete("kc-id-1")).thenReturn(response);

        keycloakService.deleteKeycloakUserQuietly("kc-id-1");

        verify(keycloak.realm("ecommerce").users()).delete("kc-id-1");
    }
}
