package com.shoply.Products.Controller.webhookController;

import com.shoply.Products.Model.*;
import com.shoply.Products.repository.CartRepository;
import com.shoply.Products.repository.CheckOutRepository;
import com.shoply.Products.repository.OrderRepository;
import com.shoply.Products.rolePermission.CHECKOUT_STATUS;
import com.shoply.Products.rolePermission.ORDER_STATUS;
import com.shoply.Products.services.EmailService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.ApiResource;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class StripeWebhookController {

    @Autowired
    CheckOutRepository checkOutRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    EmailService emailService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/webhook/stripe")
    public ResponseEntity<String> handleWebhookEvent(HttpServletRequest request, @RequestBody String payload,
                                                     @RequestHeader("Stripe-Signature") String header){
        Event event;
        try {
            event= Webhook.constructEvent(payload, header, webhookSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if ("checkout.session.completed".equals(event.getType())){
            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();

            Session session;

            if (deserializer.getObject().isPresent()) {
                session = (Session) deserializer.getObject().get();
            } else {
                session = ApiResource.GSON.fromJson(
                        deserializer.getRawJson(),
                        Session.class
                );
            }
            String checkoutSessionId = session.getMetadata().get("checkoutSessionId");

            if (!"paid".equals(session.getPaymentStatus())){
                throw new RuntimeException("PAYMENT FAILED");
            }
            Checkout_session checkoutSession = checkOutRepository.findById(checkoutSessionId).orElseThrow(()->new RuntimeException("CHECKOUT SESSION EXPIRED"));

            if (checkoutSession.getStatus() == CHECKOUT_STATUS.SUCCESS) {
                return ResponseEntity.ok("ALREADY PROCESSED");
            }

            Users userId = checkoutSession.getUserId();

            Orders productOrder = new Orders();
            productOrder.setOrderStatus(ORDER_STATUS.PAID);
            productOrder.setItems(checkoutSession.getItems());
            productOrder.setPrice(checkoutSession.getSub_total());
            productOrder.setOwner(checkoutSession.getUserId());
            productOrder.setEmail(userId.getEmail());
            productOrder.setFirstName(checkoutSession.getFirstName());
            productOrder.setLastName(checkoutSession.getLastName());
            productOrder.setPhonenumber(checkoutSession.getPhonenumber());
            productOrder.setDeliveryAddress(checkoutSession.getDeliveryAddress());

            orderRepository.save(productOrder);

            checkoutSession.setStatus(CHECKOUT_STATUS.SUCCESS);
            checkOutRepository.save(checkoutSession);

//            send order confirmation mail
            emailService.orderConfirmation(userId.getEmail(), checkoutSession.getSub_total(), checkoutSession.getItems(), checkoutSession.getId());

            clearCart(checkoutSession);

        }
        return ResponseEntity.ok("SUCCESS");
    }

    @Transactional
    public void clearCart(Checkout_session checkoutSession) {
        Users userId = checkoutSession.getUserId();
        Cart cart = cartRepository
                .findByUserId(userId.getId())
                .orElseThrow(() -> new RuntimeException("CART NOT FOUND"));

        cart.setCartItems(new ArrayList<>());

        cartRepository.save(cart);
    }

}
