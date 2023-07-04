package io.mservice.todo;

import javax.sql.DataSource;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@ContextConfiguration(classes = { TodoApplicationRepositoryTest.Config.class })
@AutoConfigureTestDatabase(replace = NONE)
@Testcontainers
@ActiveProfiles("test")
class TodoApplicationRepositoryTest extends AbstractTodoApplicationTest {

	@Configuration
	@EnableTransactionManagement
	@EnableJpaRepositories
	@EntityScan
	static class Config {

	}

	@Autowired
	private DataSource dataSource;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ITodoRepository todoRepository;

	private Todo element;

	@Test
	void injectedComponentsAreNotNull() {
		assertThat(dataSource).isNotNull();
		assertThat(entityManager).isNotNull();
		assertThat(todoRepository).isNotNull();
	}

	@BeforeEach
	void init() {
		element = Todo.builder().build();
	}

	@Test
	void shouldCreateOneRecord() {
		final var todo = todoRepository.save(element);
		assertThat(todoRepository.findById(todo.getId()).get()).isEqualTo(todo);
	}

	@Test
	void shouldFetchAllRecords() {
		assertThat(todoRepository.findAll().size()).isEqualTo(5);
	}

	@Test
	void shouldUpdateOneRecord() {
		String task = "Using test-containers";
		final var todo = todoRepository.save(element.setTask(task));
		assertThat(todoRepository.findById(todo.getId()).get().getTask()).isEqualTo(task);
	}

	@Test
	void shouldDeleteOneRecord() {
		final var todo = todoRepository.save(element);
		todoRepository.delete(todo);
		assertThat(todoRepository.findById(todo.getId()).isEmpty());
	}

}
