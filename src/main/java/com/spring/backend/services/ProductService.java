package com.spring.backend.services;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.spring.backend.dto.CategoryDTO;
import com.spring.backend.dto.ProductDTO;
import com.spring.backend.entities.Category;
import com.spring.backend.entities.Product;
import com.spring.backend.repositories.CategoryRepository;
import com.spring.backend.repositories.ProductRepository;
import com.spring.backend.services.exceptions.DataBaseException;
import com.spring.backend.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository ProductRepository;
	
	@Autowired
	private CategoryRepository CategoryRepository;

	public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
		Page<Product> pages = ProductRepository.findAll(pageRequest);

		return pages.map(x -> new ProductDTO(x));
	}

	public ProductDTO findById(Long id) {
		Optional<Product> obj = ProductRepository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Product Not Found!!!"));

		return new ProductDTO(entity, entity.getCategories());
	}

	public ProductDTO save(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = ProductRepository.save(entity);

		return new ProductDTO(entity, entity.getCategories());
	}

	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = ProductRepository.findById(id).get();

			copyDtoToEntity(dto, entity);
			entity = ProductRepository.save(entity);

			return new ProductDTO(entity, entity.getCategories());

		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Product Not Found!");
		}
	}

	public void delete(Long id) {
		try {
			ProductRepository.delete(ProductRepository.findById(id).get());
			// ProductRepository.deleteById(id);
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("Product Not Found!");
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Product linked Product");
		}

	}

	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		
		entity.getCategories().clear();
		for (CategoryDTO catDto : dto.getCategories()) {
			Category category = CategoryRepository.findById(catDto.getId()).get();
			entity.getCategories().add(category);
		}
	}
	
}
