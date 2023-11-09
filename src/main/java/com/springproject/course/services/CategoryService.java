package com.springproject.course.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.springproject.course.entities.Category;
import com.springproject.course.repositories.CategoryRepository;
import com.springproject.course.services.exceptions.DatabaseException;
import com.springproject.course.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	public List<Category> findAll() {
		return repository.findAll();
	}
	
	public Category findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id));
	}
	
	public Category insert(Category obj) {
		return repository.save(obj);
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
	
	public Category update(Long id, Category obj) {
		Category entity = repository.getReferenceById(id);
		entity.setName(obj.getName());
		return repository.save(entity);	
	}

}
