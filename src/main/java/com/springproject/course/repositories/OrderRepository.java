package com.springproject.course.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springproject.course.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
