package com.shoply.Products.Controller;

import com.shoply.Products.Model.Orders;
import com.shoply.Products.services.OrdersServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrdersController {

    @Autowired
    OrdersServices ordersServices;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders/allOrders")
    public ResponseEntity<List<Orders>> productOrders(){

       return ResponseEntity.ok(ordersServices.getAllOrders());
    }
}
