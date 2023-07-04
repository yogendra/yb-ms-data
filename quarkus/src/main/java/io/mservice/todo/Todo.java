package io.mservice.todo;

import java.util.UUID;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "todo")
@NamedQuery(name = "Todo.findAll", query = "SELECT t FROM Todo t ORDER BY t.id desc")
@Cacheable
public class Todo {

	@Id
	@UuidGenerator
	private UUID id;

	private String task;

	private boolean status;

	public Todo() {
	}

	public Todo(UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
