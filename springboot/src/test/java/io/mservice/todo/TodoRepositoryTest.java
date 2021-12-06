package io.mservice.todo;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit5.FlywayTestExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.YugabyteYSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ExtendWith(SpringExtension.class)
@ExtendWith(FlywayTestExtension.class)
@Testcontainers
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class TodoRepositoryTest {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ITodoRepository todoRepository;

	@MockBean
	private ITodoService todoService;

	@Container
	public static YugabyteYSQLContainer container = new YugabyteYSQLContainer("yugabytedb/yugabyte:2.9.1.0-b140")
			.withDatabaseName("yugabyte").withUsername("yugabyte").withPassword("yugabyte").withReuse(true);

	@DynamicPropertySource
	static void datasourceProps(final DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", container::getJdbcUrl);
		registry.add("spring.datasource.username", container::getUsername);
		registry.add("spring.datasource.password", container::getPassword);
		registry.add("spring.datasource.driver-class-name", () -> "com.yugabyte.Driver");
		registry.add("spring.flyway.driver-class-name", () -> "com.yugabyte.Driver");
		registry.add("spring.flyway.url", container::getJdbcUrl);
		registry.add("spring.flyway.user", container::getUsername);
		registry.add("spring.flyway.password", container::getPassword);
	}

	@Test
	void injectedComponentsAreNotNull() {
		assertThat(dataSource).isNotNull();
		assertThat(jdbcTemplate).isNotNull();
		assertThat(entityManager).isNotNull();
		assertThat(todoRepository).isNotNull();
	}

	@BeforeEach
	void init() {
		todoRepository.deleteAll();
	}

	@Test
	@FlywayTest
	void shouldCreateOneRecord() {
		final var todo = todoRepository.save(new Todo());
		assertThat(todoRepository.findById(todo.getId()).get()).isEqualTo(todo);
	}

}
