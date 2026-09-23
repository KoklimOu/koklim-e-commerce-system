package kh.gov.mptc.koklim.ecommerce.order.domain.core.entity;

import kh.gov.mptc.koklim.ecommerce.common.domain.entity.AggregateRoot;
import kh.gov.mptc.koklim.ecommerce.common.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {
    private String username;
    private String familyName;
    private String givenName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }


    public static Builder builder() {
        return new Builder();
    }

    private Customer(Builder builder) {
        super.setId(builder.id);
        username = builder.username;
        familyName = builder.familyName;
        givenName = builder.givenName;
    }

    public static final class Builder {
        private CustomerId id;
        private String username;
        private String familyName;
        private String givenName;

        private Builder() {
        }


        public Builder id(CustomerId val) {
            id = val;
            return this;
        }

        public Builder username(String val) {
            username = val;
            return this;
        }

        public Builder familyName(String val) {
            familyName = val;
            return this;
        }

        public Builder givenName(String val) {
            givenName = val;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
