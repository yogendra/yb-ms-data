package io.mservice.todo;

import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.sql.SQLTransientConnectionException;

import jakarta.annotation.PostConstruct;

import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.stereotype.Component;

@Component
public class TodoRetryPolicy extends ExceptionClassifierRetryPolicy {

	// 40001 - optimistic concurrency or serialization_failure
	// 40P01 - deadlock
	// 08006 - connection issues (error sending data back)
	// 57P01 - broken pool conn (invalidated connections because of node failure, etc.)
	// XX000 - RPC failures (could be intermittent)
	private final String SQL_STATE = "^(40001)|(40P01)|(57P01)|(08006)|(XX000)";

	// oom-killer or a process crash in the middle of a tx
	private final String SQL_MSG = "^(connection is closed)";

	private final RetryPolicy sp = new SimpleRetryPolicy(3);

	private final RetryPolicy np = new NeverRetryPolicy();

	@PostConstruct
	public void init() {
		this.setExceptionClassifier(cause -> {
			do {
				// SQLTransientConnectionException - connection is refused; intermittent issues because of a backend failure
				if (cause instanceof SQLRecoverableException || cause instanceof SQLTransientConnectionException) {
					return sp;
				}
				else if (cause instanceof SQLException exception) {
						if ((exception.getSQLState() != null && exception.getSQLState().matches(SQL_STATE)) ||
								(exception.getMessage() != null && exception.getMessage().toLowerCase().matches(SQL_MSG))) {
					return sp;
				}
						}
				cause = cause.getCause();
			}
			while (cause != null);
			return np;
		});
	}

}
