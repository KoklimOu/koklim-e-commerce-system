package kh.gov.mptc.koklim.ecommerce.payment;

import kh.gov.mptc.koklim.ecommerce.payment.domain.core.service.PaymentDomainService;
import kh.gov.mptc.koklim.ecommerce.payment.domain.core.service.PaymentDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
  @Bean
  public PaymentDomainService paymentDomainService() {
    return new PaymentDomainServiceImpl();
  }
}
