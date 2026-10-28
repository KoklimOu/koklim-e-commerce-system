package kh.gov.mptc.koklim.ecommerce.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Root package "...ecommerce.user": component, entity and repository scanning
// finds the classes of all four modules automatically.
@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
