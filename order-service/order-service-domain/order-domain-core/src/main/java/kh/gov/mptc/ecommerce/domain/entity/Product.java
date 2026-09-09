package kh.gov.mptc.ecommerce.domain.entity;

import kh.mptc.gov.ecommerce.domian.entity.BaseEntity;
import kh.mptc.gov.ecommerce.domian.valueobject.Money;
import kh.mptc.gov.ecommerce.domian.valueobject.ProductId;

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
