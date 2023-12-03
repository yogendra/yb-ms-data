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

	// 40001 - optimistic locking or leader changes abort
	// 40P01 - deadlock
	// 08006 - connection issues
	// 57P01 - broken pool conn (invalidated connections because of node failure, etc.)
	private final String SQL_STATE = "^(40001)|(40P01)|(57P01)|(08006)";

	private final RetryPolicy sp = new SimpleRetryPolicy(3);

	private final RetryPolicy np = new NeverRetryPolicy();

	@PostConstruct
	public void init() {
		this.setExceptionClassifier(cause -> {
			do {
				if (cause instanceof SQLRecoverableException || cause instanceof SQLTransientConnectionException) {
					return sp;
				}
				else if (cause instanceof SQLException exception
						&& (exception.getSQLState() != null && exception.getSQLState().matches(SQL_STATE))) {
					return sp;
				}
				cause = cause.getCause();
			}
			while (cause != null);
			return np;
		});
	}

}
