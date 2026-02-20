package com.shoply.Products.services;

import com.shoply.Products.Model.Checkout_session;
import com.shoply.Products.Model.Striperesponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    public Striperesponse checkoutProducts(Checkout_session checkoutResponse){
        Stripe.apiKey = secretKey;
        double total = checkoutResponse.getSub_total();

        long stripeAmount = Math.round(total * 100);

        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(checkoutResponse.getId())
                .build();

        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(checkoutResponse.getCurrency() == null ? "usd" : checkoutResponse.getCurrency())
                .setUnitAmount(stripeAmount)
                .setProductData(productData)
                .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(priceData)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/sucsess")
                .setCancelUrl("http://localhost:8080/cancel")
                .putMetadata("checkoutSessionId", checkoutResponse.getId())
                .addLineItem(lineItem)
                .build();

        Session session = null;

        try{
            session=Session.create(params);
            return new Striperesponse("SUCCESS", "PAYMENT SESSION CREATED", session.getId(), session.getUrl());
        } catch (StripeException e) {
            System.out.println(e.getMessage());
            return new Striperesponse("FAILED", "UNABLE TO CREATE PAYMENT SESSION", session.getId(), session.getUrl());
        }

    }
}
