package io.mservice.todo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
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

	private final RetryTemplate retryTemplate;

	TodoController(ITodoService todoService, RetryTemplate retryTemplate) {
		this.todoService = todoService;
		this.retryTemplate = retryTemplate;
	}

	@GetMapping
	public List<Todo> findAll() {
		return retryTemplate.execute(context -> todoService.findAllBySort(Sort.by(Sort.Direction.DESC, "id")));
	}

	@GetMapping("/{id}")
	public Optional<Todo> findById(@PathVariable("id") UUID id) {
		return retryTemplate.execute(context -> todoService.findById(id));
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody Todo resource) {
		if (resource.getId() == null) {
			return ResponseEntity.badRequest().body("Todo id is empty");
		}
		return ResponseEntity.ok(retryTemplate.execute(context -> todoService.save(resource)));
	}

	@PostMapping
	public Todo create(@RequestBody Todo resource) {
		return retryTemplate.execute(context -> todoService.save(resource));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
		todoService.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
