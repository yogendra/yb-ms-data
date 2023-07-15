package io.mservice.todo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;

@Singleton
public class TodoService implements ITodoService {

	private final EntityManager entityManager;

	public TodoService(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@ReadOnly
	public List<Todo> findAll() {
		return entityManager.createNamedQuery("Todo.findAll", Todo.class).getResultList();
	}

	@Override
	@ReadOnly
	public Todo findById(UUID id) {
		return entityManager.find(Todo.class, id);
	}

	@Override
	@Transactional
	public Todo save(Todo resource) {
		entityManager.persist(resource);
		return resource;
	}

	@Override
	@Transactional
	public Todo update(Todo resource) {
		return entityManager.merge(resource);
	}

	@Override
	@Transactional
	public boolean deleteById(UUID id) {
		Optional<Todo> todo = Optional.ofNullable(findById(id));
		todo.ifPresent(t -> entityManager.remove(t));
		return todo.isPresent();
	}

}
