package kh.gov.mptc.koklim.ecommerce.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class PaymentApplicationService {
  public static void main(String[] args) {
    SpringApplication.run(PaymentApplicationService.class, args);
  }
}
