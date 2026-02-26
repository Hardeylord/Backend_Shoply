package com.shoply.Products.Controller;

import com.shoply.Products.Model.Checkout_session;
import com.shoply.Products.Model.Striperesponse;
import com.shoply.Products.services.CheckoutService;
import com.shoply.Products.services.StripePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckoutController {

    @Autowired
    CheckoutService checkoutService;
    @Autowired
    StripePaymentService paymentService;

    @PostMapping("checkout/validateCart")
    public Striperesponse checkout(@RequestBody Checkout_session userdetails,
                                   @RequestHeader("idempotency-Key") String idempotency_Key){

//        System.out.println("key -> "+idempotency_Key);
        Checkout_session checkoutResponse = checkoutService.validateCartItems(userdetails, idempotency_Key);
        return paymentService.checkoutProducts(checkoutResponse);
    }
}
