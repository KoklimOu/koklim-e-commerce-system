package kh.gov.mptc.koklim.ecommerce.user.controller;

import kh.gov.mptc.koklim.ecommerce.user.config.SecurityConfig;
import kh.gov.mptc.koklim.ecommerce.user.dto.response.UserProfileResponse;
import kh.gov.mptc.koklim.ecommerce.user.exception.UserGlobalExceptionHandler;
import kh.gov.mptc.koklim.ecommerce.user.exception.UserNotFoundException;
import kh.gov.mptc.koklim.ecommerce.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, UserGlobalExceptionHandler.class})
class UserControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean UserService userService;
    @MockitoBean JwtDecoder jwtDecoder;   // the resource server needs a decoder bean; tests never call Keycloak

    private final UUID id = UUID.randomUUID();

    // A logged-in customer, as the gateway would forward it
    private JwtRequestPostProcessor customer() {
        return jwt().jwt(j -> j.subject(id.toString())
                        .claim("preferred_username", "koklim")
                        .claim("email", "koklim@example.com")
                        .claim("name", "Kok Lim"))
                .authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
    }

    private UserProfileResponse profile() {
        return UserProfileResponse.builder().id(id).username("koklim").displayName("Kok Lim").build();
    }

    @Test
    void noToken_returns401() throws Exception {
        mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void me_mergesProfileWithKeycloakIdentity() throws Exception {
        when(userService.getOrCreateMyProfile(any())).thenReturn(profile());

        mockMvc.perform(get("/api/v1/users/me").with(customer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("koklim"))
                .andExpect(jsonPath("$.displayName").value("Kok Lim"))
                .andExpect(jsonPath("$.email").value("koklim@example.com"))   // from the token, not the database
                .andExpect(jsonPath("$.roles[0]").value("CUSTOMER"));
    }

    @Test
    void updateMe_returns200() throws Exception {
        when(userService.updateMyProfile(any(), any())).thenReturn(profile());

        mockMvc.perform(put("/api/v1/users/me").with(customer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"displayName":"Kok Lim","bio":"Backend learner"}
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void updateMe_returns400_whenDisplayNameTooLong() throws Exception {
        mockMvc.perform(put("/api/v1/users/me").with(customer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"displayName\":\"" + "x".repeat(101) + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0].field").value("displayName"));
    }

    @Test
    void updateMe_returns400_whenAvatarUrlNotHttp() throws Exception {
        mockMvc.perform(put("/api/v1/users/me").with(customer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"avatarUrl":"javascript:alert(1)"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0].field").value("avatarUrl"));
    }

    @Test
    void getById_returnsPublicProfile_toAnyLoggedInUser() throws Exception {
        when(userService.getById(id)).thenReturn(profile());

        mockMvc.perform(get("/api/v1/users/{id}", id).with(customer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("koklim"));
    }

    @Test
    void getById_returns404_whenMissing() throws Exception {
        when(userService.getById(id)).thenThrow(new UserNotFoundException(id));

        mockMvc.perform(get("/api/v1/users/{id}", id).with(customer()))
                .andExpect(status().isNotFound());
    }
}
