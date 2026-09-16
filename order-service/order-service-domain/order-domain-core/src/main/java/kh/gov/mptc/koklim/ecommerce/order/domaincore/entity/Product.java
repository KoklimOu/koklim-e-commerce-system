package kh.gov.mptc.koklim.ecommerce.order.domaincore.entity;

import kh.gov.mptc.koklim.ecommerce.commondomain.entity.BaseEntity;
import kh.gov.mptc.koklim.ecommerce.commondomain.valueobject.Money;
import kh.gov.mptc.koklim.ecommerce.commondomain.valueobject.ProductId;

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
