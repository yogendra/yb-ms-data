package io.mservice.todo;

import com.yugabyte.PGProperty;
import io.micronaut.core.annotation.TypeHint;
import org.hibernate.id.uuid.UuidGenerator;

@TypeHint(value = { PGProperty.class, UuidGenerator.class },
		accessType = { TypeHint.AccessType.ALL_PUBLIC_FIELDS, TypeHint.AccessType.ALL_DECLARED_FIELDS,
				TypeHint.AccessType.ALL_DECLARED_CONSTRUCTORS, TypeHint.AccessType.ALL_PUBLIC_CONSTRUCTORS })
public class TodoRuntimeHints {

}
