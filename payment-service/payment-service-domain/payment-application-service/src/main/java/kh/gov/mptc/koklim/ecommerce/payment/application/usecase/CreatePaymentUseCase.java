package kh.gov.mptc.koklim.ecommerce.payment.application.usecase;

import kh.gov.mptc.koklim.ecommerce.payment.application.dto.CreatePaymentCommand;
import kh.gov.mptc.koklim.ecommerce.payment.application.dto.CreatePaymentResult;
import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.CreditEntry;
import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.CreditHistory;
import kh.gov.mptc.koklim.ecommerce.payment.domain.core.entity.Payment;
import kh.gov.mptc.koklim.ecommerce.payment.domain.core.exception.PaymentDomainException;
import kh.gov.mptc.koklim.ecommerce.payment.application.mapper.PaymentDomainMapper;
import kh.gov.mptc.koklim.ecommerce.payment.application.port.output.CreditEntityRepository;
import kh.gov.mptc.koklim.ecommerce.payment.application.port.output.CreditHistoryRepository;
import kh.gov.mptc.koklim.ecommerce.payment.application.port.output.PaymentRepository;
import kh.gov.mptc.koklim.ecommerce.payment.domain.core.service.PaymentDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreatePaymentUseCase {
  private final PaymentDomainService paymentDomainService;
  private final PaymentRepository paymentRepository;
  private final PaymentDomainMapper paymentDomainMapper;
  private final CreditEntityRepository creditEntityRepository;
  private final CreditHistoryRepository creditHistoryRepository;

  public CreatePaymentResult execute(CreatePaymentCommand createPaymentCommand) {
    log.info("executing CreatePaymentUseCase: {}", createPaymentCommand);

    //1. convert input object by map-struct
    Payment payment = paymentDomainMapper.createPaymentCommandToPayment(createPaymentCommand);

    //2. load customer credit
    CreditEntry creditEntry = creditEntityRepository.findByCustomerId(payment.getCustomerId());
    if (creditEntry == null) {
      throw new PaymentDomainException("Could not find credit entry for customer: "
              + payment.getCustomerId().id());
    }

    //3. domain logic (validate → initialize → subtract credit → COMPLETED)
    CreditHistory creditHistory = paymentDomainService.validateAndInitiatePayment(payment, creditEntry);

    //4. save
    Payment savePayment = paymentRepository.savePayment(payment);
    if(savePayment == null){
      throw  new PaymentDomainException("Could not save payment into Database");
    }
    creditEntityRepository.save(creditEntry);
    creditHistoryRepository.save(creditHistory);

    log.info("Payment {} completed for customer {}", savePayment.getId().id(),
            payment.getCustomerId().id());

    return new CreatePaymentResult(savePayment.getId().id(), savePayment.getPaymentStatus());
  }
}
