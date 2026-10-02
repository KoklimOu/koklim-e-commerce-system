package kh.gov.mptc.koklim.ecommerce.business.persistence.adapter;

import kh.gov.mptc.koklim.ecommerce.business.domain.core.entity.OrderApproval;
import kh.gov.mptc.koklim.ecommerce.business.application.port.output.OrderApprovalRepository;
import kh.gov.mptc.koklim.ecommerce.business.persistence.entity.OrderApprovalEntity;
import kh.gov.mptc.koklim.ecommerce.business.persistence.mapper.OrderApprovalPersistenceMapper;
import kh.gov.mptc.koklim.ecommerce.business.persistence.repository.OrderApprovalJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderApprovalRepositoryAdapter implements OrderApprovalRepository {

    private final OrderApprovalJpaRepository orderApprovalJpaRepository;
    private final OrderApprovalPersistenceMapper orderApprovalPersistenceMapper;

    @Override
    public OrderApproval save(OrderApproval orderApproval) {
        OrderApprovalEntity orderApprovalEntity = orderApprovalPersistenceMapper.orderApprovalToOrderApprovalEntity(orderApproval);

        return orderApprovalPersistenceMapper.orderApprovalEntityToOrderApproval(orderApprovalJpaRepository.save(orderApprovalEntity));
    }
}
