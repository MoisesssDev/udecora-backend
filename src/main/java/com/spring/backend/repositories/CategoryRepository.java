package com.spring.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.backend.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}
