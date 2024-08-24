package com.klu.service;

import java.util.List;

import com.klu.entity.Order;
import com.klu.repository.CustomOrderRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

public class CustomOrderRepositoryImpl implements CustomOrderRepository {

	@PersistenceContext
	private EntityManager em;
	@Override
	public Order findByCustomerId(long customerId) {
		String getOrderByCustId = "SELECT * FROM order_table where customer_id="+customerId;
		Query q = em.createNativeQuery(getOrderByCustId, Order.class);
		List<Order> orders = q.getResultList();
		if(orders.size()==0) {
			return null;
		}
		System.out.println("******"+orders.get(0).getClass());
		Order temp = (Order)orders.get(0);
		return temp;
	}

}
