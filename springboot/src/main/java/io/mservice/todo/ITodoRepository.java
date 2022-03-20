package io.mservice.todo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ITodoRepository extends JpaRepository<Todo, UUID> {

}
