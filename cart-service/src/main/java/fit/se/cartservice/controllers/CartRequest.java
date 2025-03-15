package fit.se.cartservice.controllers;

public class CartRequest {
    private Long customerId;

    // Getter và Setter
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}