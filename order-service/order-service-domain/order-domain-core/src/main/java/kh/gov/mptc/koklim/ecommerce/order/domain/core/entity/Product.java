package kh.gov.mptc.koklim.ecommerce.order.domain.core.entity;

import kh.gov.mptc.koklim.ecommerce.common.domain.entity.BaseEntity;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.Money;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.ProductId;

public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }
}
