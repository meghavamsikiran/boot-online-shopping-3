package com.klu.repository;

import com.klu.entity.Cart;

public interface CustomCartRepository {
    Cart findByCustomerId(long customerId);
}
