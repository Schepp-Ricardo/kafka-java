package com.ricardo.ecommerce;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {

        try (KafkaDispatcher orderDspatcher = new KafkaDispatcher<Order>()) {
            try (KafkaDispatcher emailDspatcher = new KafkaDispatcher<Email>()) {

                String email = Math.random()+"@email.com";
                for (int i = 0; i < 10; i++) {

                    String orderId = UUID.randomUUID().toString();
                    BigDecimal amount = new BigDecimal(Math.random() * 5000 + 1);

                    Order order = new Order(orderId, amount, email);
                    orderDspatcher.send("ECOMMERCE_NEW_ORDER", email, order);

                    String emailCode = "thank you for your order! we are processing your order!";
                    emailDspatcher.send("ECOMMERCE_SEND_EMAIL", email, emailCode);
                }
            }
        }
    }
}
