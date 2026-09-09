package kh.gov.mptc.ecommerce.domain.entity;

import kh.gov.mptc.ecommerce.domain.exception.OrderDomainException;
import kh.mptc.gov.ecommerce.domian.entity.AggregateRoot;
import kh.mptc.gov.ecommerce.domian.valueobject.*;

import java.util.List;
import java.util.UUID;

// Why AggregateRoot<OrderId>?
// what is a Builder pattern?
public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final StreetAddress deliveryAddress;

    private final BusinessId businessId;
    private final Money price;
    private final List<OrderItem> items;
    private List<String> failureMessages;

    //why not final, because not updatable later
    private TrackingId trackingId;
    private OrderStatus orderStatus;


    public Order(CustomerId customerId, StreetAddress deliveryAddress, BusinessId businessId, Money price, List<OrderItem> items, List<String> failureMessages) {
        this.customerId = customerId;
        this.deliveryAddress = deliveryAddress;
        this.businessId = businessId;
        this.price = price;
        this.items = items;
        this.failureMessages = failureMessages;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }

    public BusinessId getBusinessId() {
        return businessId;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    // BUILDERS
    private Order(Builder builder) {
        // because we inherit from AggregateRoot, we need to call super.setId()
        super.setId(builder.id);
        customerId = builder.customerId;
        deliveryAddress = builder.deliveryAddress;
        businessId = builder.businessId;
        price = builder.price;
        items = builder.items;
        failureMessages = builder.failureMessages;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
    }

    public static final class Builder {
        private OrderId id;
        private CustomerId customerId;
        private StreetAddress deliveryAddress;
        private BusinessId businessId;
        private Money price;
        private List<OrderItem> items;
        private List<String> failureMessages;
        private TrackingId trackingId;
        private OrderStatus orderStatus;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(OrderId val) {
            id = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder businessId(BusinessId val) {
            businessId = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
    // END OF BUILDERS

    // MAIN FUNCTIONALITY
    // Critical Business Logic
    // The critical logic will only use their own property.

    public void validateOrder() {
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    private void validateInitialOrder() {
        if(orderStatus != null || super.getId() != null) {
            throw new OrderDomainException("Order is not in correct status for initialization");
        }
    }

    private void validateTotalPrice() {
        if (price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Order price is not valid");
        }
    }

    private void validateItemsPrice() {
        Money orderItemsTotalPrice = items.stream()
                .map(orderItem -> {
                    validateItemPrice(orderItem);
                    return orderItem.getSubTotal();
                })
                .reduce(Money.ZERO, Money::add);

        if (!price.equals(orderItemsTotalPrice)) {
            throw new OrderDomainException("Total price: " + price.getAmount()
                    + " is not equal to order items total price: " + orderItemsTotalPrice.getAmount());
        }
    }
    // END OF MAIN FUNCTIONALITY

    //  UTILS FUNCTIONALITY
    private void validateItemPrice(OrderItem orderItem) {
        if (orderItem.isPriceValid()){
            throw new OrderDomainException("Order item price is not valid");
        }
    }

    public void initializeOrder(){
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    private void initializeOrderItems() {
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if (failureMessages != null && this.failureMessages != null) {
            this.failureMessages.addAll(
                    failureMessages.stream().filter(message -> !message.isBlank()).toList()
            );
        }

        if (this.failureMessages == null) {
            this.failureMessages = failureMessages;
        }
    }
    // END OF UTILS FUNCTIONALITY


    // OPERATIONS ACTIONS
    public void pay() {
        if (orderStatus != OrderStatus.PENDING) {
            throw new OrderDomainException("Order is not in correct state for pay operation");
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve() {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for approve operation");
        }
        orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessages) {
        if (orderStatus != OrderStatus.PAID) {
            throw new OrderDomainException("Order is not in correct state for init cancel operation");
        }
        orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    public void cancel(){
        if (!(orderStatus == OrderStatus.CANCELLING || orderStatus == OrderStatus.PENDING)) {
            throw new OrderDomainException("Order is not in correct state for cancel operation");
        }
        orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    // END OF OPERATION ACTIONS
}
