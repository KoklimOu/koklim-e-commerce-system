package kh.gov.mptc.koklim.ecommerce.order.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

// JPA entity must me POJO class. plain old java object
// to become a POJO class, we need getter setter, and Contructor
@Entity
@Table(name = "orders")
@Setter
@Getter
@NoArgsConstructor
public class OrderEntity {
    @Id
    // no @GeneratedValue: the domain assigns the id in Order.initializeOrder()
    private UUID id;
    private String customerId;
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderAddressEntity deliveryAddress;

    private BigDecimal price;

    private UUID businessId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemEntity> items;

    //why not final, because not updatable later
    private UUID trackingId;
    private String failureMessages;
}
