package io.mservice.todo;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import io.agroal.api.AgroalDataSource;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.flywaydb.core.Flyway;

//@ApplicationScoped
public class TodoMigrate {

	private final AgroalDataSource dataSource;

	TodoMigrate(AgroalDataSource dataSource) {
		this.dataSource = dataSource;
	}

	@ConfigProperty(name = "todo.flyway.migrate", defaultValue = "true")
	boolean runMigration;

	public void runFlywayMigration(@Observes StartupEvent event) {
		if (runMigration) {
			Flyway.configure().dataSource(dataSource).load().migrate();
		}
	}

}
