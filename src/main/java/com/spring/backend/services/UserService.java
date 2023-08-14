package com.spring.backend.services;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.backend.dto.RoleDTO;
import com.spring.backend.dto.UserDTO;
import com.spring.backend.dto.UserInsertDTO;
import com.spring.backend.dto.UserUpdateDTO;
import com.spring.backend.entities.Role;
import com.spring.backend.entities.User;
import com.spring.backend.repositories.RoleRepository;
import com.spring.backend.repositories.UserRepository;
import com.spring.backend.services.exceptions.DataBaseException;
import com.spring.backend.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> pages = userRepository.findAll(pageable);

		return pages.map(x -> new UserDTO(x));
	}

	public UserDTO findById(Long id) {
		Optional<User> obj = userRepository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("User Not Found!!!"));

		return new UserDTO(entity);
	}

	public UserDTO save(UserInsertDTO dto) {
		User entity = new User();
		copyDtoToEntity(dto, entity);
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));

		entity = userRepository.save(entity);
		return new UserDTO(entity);

	}

	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User entity = userRepository.findById(id).get();
			copyDtoToEntity(dto, entity);

			entity = userRepository.save(entity);

			return new UserDTO(entity);

		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("User Not Found!");
		}
	}

	public void delete(Long id) {
		try {
			userRepository.delete(userRepository.findById(id).get());
			// UserRepository.deleteById(id);
		} catch (NoSuchElementException e) {
			throw new ResourceNotFoundException("User Not Found!");
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("User linked User");
		}

	}

	private void copyDtoToEntity(UserDTO dto, User entity) {

		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());

		entity.getRoles().clear();
		for (RoleDTO roleDto : dto.getRoles()) {
			Role role = roleRepository.findById(roleDto.getId()).get();
			entity.getRoles().add(role);
		}
	}

}
