package com.shoply.Products.services;

import com.shoply.Products.Model.Orders;
import com.shoply.Products.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersServices {

    @Autowired
    OrderRepository orderRepository;

    public List<Orders> getAllOrders() {
      return orderRepository.findAll();
    }
}
