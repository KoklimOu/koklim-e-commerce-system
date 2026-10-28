package kh.gov.mptc.koklim.ecommerce.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// Lives in the root package "...ecommerce.user", so component/entity/repository scanning
// finds controller, service, repository and entity in the other modules automatically.
@SpringBootApplication
@ConfigurationPropertiesScan   // picks up KeycloakAdminProperties (keycloak.admin.*)
@EnableDiscoveryClient
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
