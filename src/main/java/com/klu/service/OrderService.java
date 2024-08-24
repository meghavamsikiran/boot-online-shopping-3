package com.klu.service;

import com.klu.entity.Order;
import com.klu.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order findByCustomer_Id(long id) {
        // TODO: Implement the actual logic to find by customer ID
        return orderRepository.findByCustomerId(id);
    }
    
    public Order save(Order order) {
        return orderRepository.save(order);
    }
    
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus("Cancelled");
        return orderRepository.save(order);
    }
    
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }
    
    public List<Order> findOrdersByDate(LocalDateTime date) {
        return orderRepository.findByOrderDate(date);
    }

    public List<Order> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }

    public List<Order> findOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
}
