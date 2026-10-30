package kh.gov.mptc.koklim.ecommerce.user.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final String BEARER = "bearer-jwt";

    @Bean
    public OpenAPI userServiceOpenApi() {
        return new OpenAPI()
                .info(new Info().title("User Service API").version("v1"))
                // "/" = same host that served the page: works on 8554 (direct) and 8585 (gateway)
                .servers(List.of(new Server().url("/")))
                // Adds the "Authorize" button: paste a Keycloak access token (the gateway needs one)
                .components(new Components().addSecuritySchemes(BEARER, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER));
    }
}
