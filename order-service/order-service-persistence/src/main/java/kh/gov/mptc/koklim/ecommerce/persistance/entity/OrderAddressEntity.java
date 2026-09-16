package kh.gov.mptc.koklim.ecommerce.persistance.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "order_addresses")
@Setter
@Getter
@NoArgsConstructor
public class OrderAddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String street;
    private String postalCode;
    private String city;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private OrderEntity order;
}
