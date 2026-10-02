package kh.gov.mptc.koklim.ecommerce.business.config;

import kh.gov.mptc.koklim.ecommerce.business.domain.core.service.BusinessDomainService;
import kh.gov.mptc.koklim.ecommerce.business.domain.core.service.BusinessDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public BusinessDomainService businessDomainService() {
        return new BusinessDomainServiceImpl();
    }
}
