package com.ricardo.ecommerce;

import org.eclipse.jetty.servlet.Source;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {

    private final KafkaDispatcher orderDspatcher = new KafkaDispatcher<Order>();
    private final KafkaDispatcher emailDspatcher = new KafkaDispatcher<Email>();

    @Override
    public void destroy() {
        super.destroy();
        orderDspatcher.close();
        orderDspatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            String email = req.getParameter("email");
            BigDecimal amount = new BigDecimal(req.getParameter("amount"));

            String orderId = UUID.randomUUID().toString();

            Order order = new Order(orderId, amount, email);
            orderDspatcher.send("ECOMMERCE_NEW_ORDER", email, order);

            String emailCode = "thank you for your order! we are processing your order!";
            emailDspatcher.send("ECOMMERCE_SEND_EMAIL", email, emailCode);

            System.out.println("New order sent successfully");

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("New order sent successfully");

        } catch (ExecutionException e) {
            throw new ServletException(e);
        } catch (InterruptedException e) {
            throw new ServletException(e);
        }

    }

}
