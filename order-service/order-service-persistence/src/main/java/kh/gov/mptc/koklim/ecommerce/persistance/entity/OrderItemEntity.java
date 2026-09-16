package kh.gov.mptc.koklim.ecommerce.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Setter
@Getter
@NoArgsConstructor
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment
    private Integer id;

    private UUID productId;

    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subTotal;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private OrderEntity order;
}
