package kh.mptc.gov.ecommerce.domian.valueobject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

// why using BigDecimal?
public record Money(BigDecimal amount) {
    public static final Money ZERO = new Money(BigDecimal.ZERO);
    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    };

    public boolean isGreaterThan(Money money) {
        return amount.compareTo(money.amount) > 0;
    }

    public boolean isGreaterThanZero() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public Money add(Money money) {
        return new Money(setScale(amount.add(money.amount)));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Money subtract(Money money) {
        return new Money(setScale(amount.subtract(money.amount)));
    }

    public Money multiply(int multiplier) {
        return new Money(setScale(amount.multiply(BigDecimal.valueOf(multiplier))));
    }

    private BigDecimal setScale(BigDecimal inputAmount) {
        return inputAmount.setScale(2, RoundingMode.HALF_EVEN);
    }

}
