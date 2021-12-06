package io.mservice.todo;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.yugabyte.util.PSQLException;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@RestController
//@RequestMapping("/v1/todo")
public class TodoController {

	private final ITodoService todoService;

	TodoController(ITodoService todoService) {
		this.todoService = todoService;
	}

	@GetMapping
	public List<Todo> findAll() {
		return todoService.findAllBySort(Sort.by(Sort.Direction.DESC, "id"));
	}

	@GetMapping("/{id}")
	@Retryable(value = { PSQLException.class, SQLException.class }, maxAttempts = 2)
	public Optional<Todo> findById(@PathVariable("id") UUID id) {
		return todoService.findById(id);
	}

	@PutMapping
	@Retryable(value = { PSQLException.class, SQLException.class }, maxAttempts = 2)
	public ResponseEntity<?> update(@RequestBody Todo resource) {
		if (resource.getId() == null) {
			return ResponseEntity.badRequest().body("Todo id is empty");
		}
		return ResponseEntity.ok(todoService.save(resource));
	}

	@PostMapping
	@Retryable(value = { PSQLException.class, SQLException.class }, maxAttempts = 2)
	public Todo create(@RequestBody Todo resource) {
		return todoService.save(resource);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
		todoService.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
