package com.klu.repository;

import com.klu.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, CustomOrderRepository {
	 List<Order> findByOrderDate(LocalDateTime date);
     List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
     List<Order> findByStatus(String status);
}
