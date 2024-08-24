package com.klu.repository;

import com.klu.entity.Order;

public interface CustomOrderRepository {
    Order findByCustomerId(long customerId);
}
