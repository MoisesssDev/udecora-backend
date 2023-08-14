package com.spring.backend.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spring.backend.dto.UserDTO;
import com.spring.backend.dto.UserInsertDTO;
import com.spring.backend.dto.UserUpdateDTO;
import com.spring.backend.services.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserResource {

	@Autowired
	private UserService userService;

	@GetMapping
	@Transactional
	public ResponseEntity<Page<UserDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,

			@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,

			@RequestParam(value = "direction", defaultValue = "DESC") String direction,
			
			@RequestParam(value = "orderBy", defaultValue = "firstName") String orderBy

	) {

		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<UserDTO> list = userService.findAllPaged(pageRequest);

		return ResponseEntity.ok(list);
	}

	@GetMapping("/{id}")
	@Transactional
	public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.findById(id));
	}

	@PostMapping
	public ResponseEntity<UserDTO> save(@Valid @RequestBody UserInsertDTO dto) {
		UserDTO newDto = userService.save(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDto.getId()).toUri();

		return ResponseEntity.created(uri).body(newDto);
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
		UserDTO newDto = userService.update(id, dto);

		return ResponseEntity.ok().body(newDto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		userService.delete(id);

		return ResponseEntity.noContent().build();
	}
}
