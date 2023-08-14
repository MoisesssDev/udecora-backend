package com.spring.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.backend.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
