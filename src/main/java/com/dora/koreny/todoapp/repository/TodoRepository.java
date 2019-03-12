package com.dora.koreny.todoapp.repository;

import com.dora.koreny.todoapp.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
