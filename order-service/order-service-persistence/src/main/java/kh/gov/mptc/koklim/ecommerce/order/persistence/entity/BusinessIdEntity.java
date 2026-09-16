package kh.gov.mptc.koklim.ecommerce.order.persistence.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class BusinessIdEntity implements Serializable {
    private UUID businessId;
    private UUID productId;
}
