package com.spring.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.backend.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

}
