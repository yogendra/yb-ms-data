package io.mservice.todo;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import com.yugabyte.PGProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.postgresql.util.PGobject;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.data.domain.Sort;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.servlet.function.ServerResponse.created;
import static org.springframework.web.servlet.function.ServerResponse.ok;
import static org.springframework.web.servlet.function.ServerResponse.status;

@SpringBootApplication(proxyBeanMethods = false)
@EnableRetry
@ImportRuntimeHints(TodoApplication.TodoRuntimeHints.class)
public class TodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}

	@Bean
	public RetryTemplate retryTemplate(TodoRetryPolicy todoRetryPolicy) {
		RetryTemplate retryTemplate = new RetryTemplate();
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setMaxInterval(5000); // 10s
		retryTemplate.setRetryPolicy(todoRetryPolicy);
		retryTemplate.setBackOffPolicy(backOffPolicy);
		return retryTemplate;
	}

	@RouterOperations({
			@RouterOperation(path = "/v1/todo", beanClass = TodoService.class, beanMethod = "findAllBySort",
					method = RequestMethod.GET),
			@RouterOperation(path = "/v1/todo/{id}", beanClass = TodoService.class, beanMethod = "findById",
					method = RequestMethod.GET,
					operation = @Operation(operationId = "findById",
							parameters = { @Parameter(in = PATH, name = "id", description = "todo-id to find") })),
			@RouterOperation(path = "/v1/todo", beanClass = TodoService.class, beanMethod = "save",
					method = { POST, PUT }),
			@RouterOperation(path = "/v1/todo/{id}", beanClass = TodoService.class, beanMethod = "deleteById",
					method = DELETE,
					operation = @Operation(operationId = "deleteById",
							parameters = { @Parameter(in = PATH, name = "id", description = "todo-id to delete") })) })
	@Bean
	RouterFunction<ServerResponse> routeHandler(ITodoService todoService, RetryTemplate retryTemplate) {
		return RouterFunctions.route().path("/v1/todo", builder -> builder.GET("/{id}", req -> {
			Optional<Todo> todo = retryTemplate
					.execute(context -> todoService.findById(UUID.fromString(req.pathVariable("id"))));
			return status(todo.isPresent() ? OK : NOT_FOUND).contentType(APPLICATION_JSON).body(todo);
		}).POST(req -> {
			Todo todo = req.body(Todo.class);
			return created(new URI("/v1/todo")).contentType(APPLICATION_JSON)
					.body(retryTemplate.execute(context -> todoService.save(todo)));
		}).PUT(req -> {
			Todo todo = req.body(Todo.class);
			return (todo.getId() == null) ? status(BAD_REQUEST).contentType(TEXT_PLAIN).body("Todo id is empty")
					: ok().contentType(APPLICATION_JSON).body(retryTemplate.execute(context -> todoService.save(todo)));
		}).DELETE("/{id}", req -> {
			todoService.deleteById(UUID.fromString(req.pathVariable("id")));
			return ok().build();
		}).GET("/page/{limit}",
				req -> ok().contentType(APPLICATION_JSON)
						.body(retryTemplate.execute(
								context -> todoService.findByLimit(Integer.parseInt(req.pathVariable("limit"))))))
				.GET(req -> ok().contentType(APPLICATION_JSON)
						.body(retryTemplate
								.execute(context -> todoService.findAllBySort(Sort.by(Sort.Direction.DESC, "id"))))))
				.build();
	}

	static class TodoRuntimeHints implements RuntimeHintsRegistrar {

		@Override
		public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
			for (Field element : PGProperty.class.getDeclaredFields()) {
				hints.reflection().registerField(element);
			}
			hints.reflection().registerType(PGobject.class, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
					MemberCategory.INTROSPECT_PUBLIC_METHODS);
		}

	}

}
