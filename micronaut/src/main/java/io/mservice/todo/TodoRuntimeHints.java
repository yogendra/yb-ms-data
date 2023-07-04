package io.mservice.todo;

import com.yugabyte.PGProperty;
import io.micronaut.core.annotation.TypeHint;

@TypeHint(value = { PGProperty.class },
		accessType = { TypeHint.AccessType.ALL_PUBLIC_FIELDS, TypeHint.AccessType.ALL_DECLARED_FIELDS })
public class TodoRuntimeHints {

}
