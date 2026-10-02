package kh.gov.mptc.koklim.ecommerce.customer.application.config;

import kh.gov.mptc.koklim.ecommerce.customer.domain.core.service.CustomerDomainService;
import kh.gov.mptc.koklim.ecommerce.customer.domain.core.service.CustomerDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// customer-domain-core has no Spring annotations, so we register its service as a bean here
@Configuration
public class CustomerDomainConfig {

    @Bean
    public CustomerDomainService customerDomainService() {
        return new CustomerDomainServiceImpl();
    }
}
