package com.klu.service;

import com.klu.entity.Cart;
import com.klu.entity.Customer;
import com.klu.entity.LineItem;
import com.klu.entity.Product;
import com.klu.repository.CartRepository;
import com.klu.repository.CustomerRepository;
import com.klu.repository.LineItemRepository;
import com.klu.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

	public Cart findByCustomer_Id(long id) {
		// TODO Auto-generated method stub
		return cartRepository.findByCustomerId(id);
	}
	public Cart save(Cart cart) {
		// TODO Auto-generated method stub
		return cartRepository.save(cart);
	}
	public List<Cart> findAll() {
		// TODO Auto-generated method stub
		return cartRepository.findAll();
	}
	public void removeCart(long customerId) {
        Cart cart = findByCustomer_Id(customerId);
        if (cart != null) {
            cartRepository.delete(cart);
        }
    }
	
	
}
