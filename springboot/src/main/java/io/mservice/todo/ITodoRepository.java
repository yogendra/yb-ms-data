package io.mservice.todo;

import java.util.UUID;

import com.yugabyte.data.jdbc.repository.YsqlRepository;

import org.springframework.stereotype.Repository;

@Repository
interface ITodoRepository extends YsqlRepository<Todo, UUID> {

}
