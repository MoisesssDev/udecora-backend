package com.spring.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.backend.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);
}
