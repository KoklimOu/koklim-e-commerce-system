package kh.gov.mptc.koklim.ecommerce.order;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "kh.gov.mptc.koklim.ecommerce.order.persistence.entity")
@EnableJpaRepositories(basePackages = "kh.gov.mptc.koklim.ecommerce.order.persistence.repository")
@SpringBootApplication
public class OrderServiceApplication {
    static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(OrderServiceApplication.class, args);
    }
}
