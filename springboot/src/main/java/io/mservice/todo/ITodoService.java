package io.mservice.todo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface ITodoService {

	List<Todo> findAllBySort(Sort sortOrder);

	Page<Todo> findByLimit(int limit);

	Optional<Todo> findById(UUID id);

	Todo save(Todo resource);

	void deleteById(UUID id);

}
