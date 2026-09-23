package kh.gov.mptc.koklim.ecommerce.order;

import kh.gov.mptc.koklim.ecommerce.order.domain.core.service.OrderDomainService;
import kh.gov.mptc.koklim.ecommerce.order.domain.core.service.OrderDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public OrderDomainService orderDomainService() {
        return new OrderDomainServiceImpl();
    }
}
