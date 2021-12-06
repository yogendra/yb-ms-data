package io.mservice.todo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoService implements ITodoService {

	private final ITodoRepository todoRepository;

	TodoService(ITodoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}

	@Transactional(readOnly = true)
	public List<Todo> findAllBySort(Sort sortOrder) {
		List<Todo> todoList = Collections.emptyList();
		Iterable<Todo> listIterable = todoRepository.findAll(sortOrder);
		if (listIterable.iterator().hasNext()) {
			todoList = new ArrayList<>();
			listIterable.forEach(todoList::add);
		}
		return todoList;
	}

	@Transactional(readOnly = true)
	public Page<Todo> findByLimit(int limit) {
		return todoRepository.findAll(PageRequest.ofSize(limit));
	}

	@Transactional(readOnly = true)
	public Optional<Todo> findById(UUID id) {
		return todoRepository.findById(id);
	}

	@Transactional
	public Todo save(Todo resource) {
		return todoRepository.save(resource);
	}

	@Transactional
	public void deleteById(UUID id) {
		todoRepository.deleteById(id);
	}

}
