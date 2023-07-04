package io.mservice.todo;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * To pass runtime hints for native build
 */
@RegisterForReflection(classNames = { "java.util.UUID[]" })
public class TodoRuntimeHints {

}
