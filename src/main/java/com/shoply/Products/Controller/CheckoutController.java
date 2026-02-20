package com.shoply.Products.Controller;

import com.shoply.Products.Model.Checkout_session;
import com.shoply.Products.Model.Striperesponse;
import com.shoply.Products.services.CheckoutService;
import com.shoply.Products.services.StripePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckoutController {

    @Autowired
    CheckoutService checkoutService;
    @Autowired
    StripePaymentService paymentService;

    @PostMapping("checkout/validateCart")
    public Striperesponse checkout(){

        Checkout_session checkoutResponse = checkoutService.validateCartItems();

        return paymentService.checkoutProducts(checkoutResponse);
    }
}
