package kh.gov.mptc.koklim.ecommerce.user.controller;

import kh.gov.mptc.koklim.ecommerce.user.config.SecurityConfig;
import kh.gov.mptc.koklim.ecommerce.user.dto.response.AdminUserResponse;
import kh.gov.mptc.koklim.ecommerce.user.exception.KeycloakAdminException;
import kh.gov.mptc.koklim.ecommerce.user.exception.UserGlobalExceptionHandler;
import kh.gov.mptc.koklim.ecommerce.user.service.UserAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminUserController.class)
@Import({SecurityConfig.class, UserGlobalExceptionHandler.class})
class AdminUserControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean UserAdminService userAdminService;
    @MockitoBean JwtDecoder jwtDecoder;

    private static final String CREATE_BODY = """
            {"username":"koklim","email":"koklim@example.com","firstName":"Kok","lastName":"Lim",
             "temporaryPassword":"Passw0rd!"}
            """;

    @Test
    void customer_cannotUseAdminApi_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/admin/users")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_CUSTOMER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void admin_createsUser_returns201() throws Exception {
        when(userAdminService.create(any())).thenReturn(AdminUserResponse.builder()
                .id(UUID.randomUUID()).username("koklim").enabled(true).roles(List.of("CUSTOMER")).build());

        mockMvc.perform(post("/api/v1/admin/users")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CREATE_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("koklim"))
                .andExpect(jsonPath("$.roles[0]").value("CUSTOMER"));
    }

    @Test
    void admin_createsUser_returns400_whenPasswordTooShort() throws Exception {
        mockMvc.perform(post("/api/v1/admin/users")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"koklim","email":"koklim@example.com","firstName":"Kok","lastName":"Lim",
                                 "temporaryPassword":"short"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0].field").value("temporaryPassword"));
    }

    @Test
    void keycloakConflict_returns409() throws Exception {
        when(userAdminService.create(any()))
                .thenThrow(new KeycloakAdminException(HttpStatus.CONFLICT, "already exists"));

        mockMvc.perform(post("/api/v1/admin/users")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CREATE_BODY))
                .andExpect(status().isConflict());
    }

    @Test
    void keycloakDown_returns502() throws Exception {
        when(userAdminService.search(any(), org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.anyInt()))
                .thenThrow(new KeycloakAdminException(HttpStatus.BAD_GATEWAY, "Keycloak is not reachable or failed"));

        mockMvc.perform(get("/api/v1/admin/users")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isBadGateway());
    }
}
