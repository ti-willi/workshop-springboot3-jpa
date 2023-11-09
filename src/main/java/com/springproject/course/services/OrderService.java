package com.springproject.course.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.springproject.course.entities.Order;
import com.springproject.course.entities.OrderItem;
import com.springproject.course.entities.Payment;
import com.springproject.course.repositories.OrderRepository;
import com.springproject.course.services.exceptions.DatabaseException;
import com.springproject.course.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repository;

	public List<Order> findAll() {
		return repository.findAll();
	}

	public Order findById(Long id) {
		Optional<Order> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	public Order insert(Order obj) {
		setItems(obj);
		setPayment(obj);
		return repository.save(obj);
	}
	
	private void setItems(Order obj) {
		if (obj.getItems() != null) {
			for (OrderItem item : obj.getItems()) {
				item.setOrder(obj);
			}
		}
	}
	
	private void setPayment(Order obj) {
		if (obj.getPayment() != null) {
			Payment payment = obj.getPayment();
			payment.setOrder(obj);
			obj.setPayment(payment);
		}
	}
	
	public void delete(Long id) {
		if (!repository.existsById(id))
			throw new ResourceNotFoundException(id);
		try {
			repository.deleteById(id);			
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	public Order update(Long id, Order obj) {
		try {
			Order entity = repository.getReferenceById(id);
			updateData(entity, obj);
			setPayment(entity);
			return repository.save(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}
	
	private void updateData(Order entity, Order obj) {
		entity.setClient(obj.getClient());
		entity.setMoment(obj.getMoment());
		entity.setOrderStatus(obj.getOrderStatus());
		entity.setPayment(obj.getPayment());
		
		for (OrderItem item : obj.getItems()) {
			entity.getItems().clear();
			item.setOrder(entity);
			entity.getItems().add(item);
		}	
	}
	
}
