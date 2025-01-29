package food_dating.com.food_dating.Models;

public class OrderProduct {
    private String orderId;
    private Integer amount;

    public OrderProduct(String orderId, Integer amount) {
        this.orderId = orderId;
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Product{orderId='" + orderId + "', amount=" + amount + "}";
    }
}

