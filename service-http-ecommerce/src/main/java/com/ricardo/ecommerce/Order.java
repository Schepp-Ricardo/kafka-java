package com.ricardo.ecommerce;

import java.math.BigDecimal;

public class Order {

    private final String orderId;
    private final BigDecimal amout;
    private final String email;

    public Order(String orderId, BigDecimal amout, String email) {
        this.orderId = orderId;
        this.amout = amout;
        this.email = email;
    }
}
