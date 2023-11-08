package com.springproject.course.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.course.entities.Order;
import com.springproject.course.entities.OrderItem;
import com.springproject.course.entities.Payment;
import com.springproject.course.repositories.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repository;

	public List<Order> findAll() {
		return repository.findAll();
	}

	public Order findById(Long id) {
		Optional<Order> obj = repository.findById(id);
		
		return obj.get();
	}
	
	public Order insert(Order obj) {
		getItems(obj);
		getPayment(obj);
		return repository.save(obj);
	}
	
	private void getItems(Order obj) {
		if (obj.getItems() != null) {
			for (OrderItem item : obj.getItems()) {
				item.setOrder(obj);
			}
		}
	}
	
	private void getPayment(Order obj) {
		if (obj.getPayment() != null) {
			Payment payment = obj.getPayment();
			payment.setOrder(obj);
			obj.setPayment(payment);
		}
	}

}
