package com.spring.backend.services;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.spring.backend.dto.CategoryDTO;
import com.spring.backend.entities.Category;
import com.spring.backend.repositories.CategoryRepository;
import com.spring.backend.services.exceptions.DataBaseException;
import com.spring.backend.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	public Page<CategoryDTO> findAllPaged(PageRequest pageRequest){
		Page<Category> pages = categoryRepository.findAll(pageRequest);
		
		return pages.map(x -> new CategoryDTO(x));
	}
	
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = categoryRepository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Category Not Found!!!"));
		
		return new CategoryDTO(entity);
	}

	public CategoryDTO save(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = categoryRepository.save(entity);
		
		return new CategoryDTO(entity);
	}

	public CategoryDTO update(Long id,CategoryDTO dto) {
		try {
			Category entity = categoryRepository.findById(id).get();
			
			entity.setName(dto.getName());
			entity = categoryRepository.save(entity);
			
			return new CategoryDTO(entity);
			
		} 
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Category Not Found!");
		}
	}

	public void delete(Long id) {
		try {
			categoryRepository.delete(categoryRepository.findById(id).get());
			//categoryRepository.deleteById(id);
		} 
		catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Category Not Found!");
		}
		catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Product linked category");
		}
		
	}

}
