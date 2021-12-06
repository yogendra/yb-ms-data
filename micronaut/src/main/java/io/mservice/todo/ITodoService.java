package io.mservice.todo;

import java.util.List;
import java.util.UUID;

public interface ITodoService {

	List<Todo> findAll();

	Todo findById(UUID id);

	Todo save(Todo resource);

	Todo update(Todo resource);

	boolean deleteById(UUID id);

}
