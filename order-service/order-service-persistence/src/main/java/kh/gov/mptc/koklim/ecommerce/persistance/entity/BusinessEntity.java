package kh.gov.mptc.koklim.ecommerce.persistance.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "businesses")
@Getter
@Setter
@NoArgsConstructor
@IdClass(BusinessIdEntity.class)
public class BusinessEntity {
    @Id
    private UUID businessId;
    @Id
    private UUID productId;

    private Boolean businessActive;
    private String name;
    private BigDecimal price;
}
