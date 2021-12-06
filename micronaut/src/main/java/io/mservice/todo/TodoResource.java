package io.mservice.todo;

import java.util.List;
import java.util.UUID;

import javax.persistence.PersistenceException;

import com.yugabyte.util.PSQLException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.Put;
import io.micronaut.retry.annotation.Retryable;

import static io.micronaut.http.HttpResponse.ok;
import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static io.micronaut.http.HttpStatus.NOT_FOUND;
import static io.micronaut.http.HttpStatus.OK;
import static io.micronaut.http.MediaType.APPLICATION_JSON;

@Controller("/v1/todo")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class TodoResource {

	private final ITodoService todoService;

	public TodoResource(ITodoService todoService) {
		this.todoService = todoService;
	}

	@Get
	public List<Todo> findAll() {
		return todoService.findAll();
	}

	@Get("/{id}")
	@Retryable(value = {PersistenceException.class, PSQLException.class})
	public HttpResponse<Todo> findById(@PathVariable UUID id) {
		Todo todo = todoService.findById(id);
		return ok(todo).status((todo != null) ? OK : NOT_FOUND);
	}

	@Post
	@Retryable(value = {PersistenceException.class, PSQLException.class})
	public Todo create(Todo todo) {
		todo.setId(null); // let the entity layer auto-generate it.
		return todoService.save(todo);
	}

	@Put
	@Retryable(value = {PersistenceException.class, PSQLException.class})
	public HttpResponse<?> update(Todo todo) {
		return (todo.getId() == null) ? ok("Todo id is empty").status(BAD_REQUEST)
				: ok(todoService.update(todo));
	}

	@Delete("/{id}")
	@Retryable(value = {PersistenceException.class, PSQLException.class})
	public HttpResponse<Void> deleteById(@PathVariable UUID id) {
		return (todoService.deleteById(id)) ? ok() : HttpResponse.status(NOT_FOUND);
	}

}
