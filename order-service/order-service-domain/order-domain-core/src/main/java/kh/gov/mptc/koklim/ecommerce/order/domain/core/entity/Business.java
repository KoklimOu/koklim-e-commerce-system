package kh.gov.mptc.koklim.ecommerce.order.domain.core.entity;

import kh.gov.mptc.koklim.ecommerce.common.domain.entity.AggregateRoot;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.BusinessId;

import java.util.List;

public class Business extends AggregateRoot<BusinessId> {
    private List<Product> products;
    private boolean status;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    private Business(Builder builder) {
        super.setId(builder.id);
        products = builder.products;
        status = builder.status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private BusinessId id;
        private List<Product> products;
        private boolean status;

        private Builder() {
        }

        public Builder id(BusinessId val) {
            id = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public Builder status(boolean val) {
            status = val;
            return this;
        }

        public Business build() {
            return new Business(this);
        }
    }
}
